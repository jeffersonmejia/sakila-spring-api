package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.common.exception.ConflictException;
import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Address;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.model.Store;
import com.sakila.api.domain.port.out.AddressRepository;
import com.sakila.api.domain.port.out.CustomerRepository;
import com.sakila.api.domain.port.out.RentalRepository;
import com.sakila.api.domain.port.out.StoreRepository;
import com.sakila.api.domain.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private CustomerService customerService;

    private final PageQuery query = PageQuery.of(0, 20, "id", SortDirection.ASC);
    private final Address address = new Address(5, null, null, null, null, null);
    private final Customer customer = new Customer(1, "MARY", "SMITH", "mary@sakila.com", true, 1, address,
            LocalDate.of(2026, 1, 1));
    private final Customer newCustomer = new Customer(null, "JUANA", "PEREZ", "juana@sakila.com", true, 1, address, null);

    @Test
    void listCustomersDelegates() {
        PageResult<Customer> page = PageResult.of(List.of(customer), 0, 20, 1);
        when(customerRepository.findAll(query)).thenReturn(page);

        PageResult<Customer> result = customerService.listCustomers(query);

        assertSame(page, result);
    }

    @Test
    void searchCustomersDelegates() {
        PageResult<Customer> page = PageResult.of(List.of(customer), 0, 20, 1);
        when(customerRepository.search("mary", query)).thenReturn(page);

        PageResult<Customer> result = customerService.searchCustomers("mary", query);

        assertSame(page, result);
    }

    @Test
    void getCustomerReturnsExisting() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomer(1);

        assertSame(customer, result);
    }

    @Test
    void getCustomerThrowsWhenMissing() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> customerService.getCustomer(999));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void createCustomerSuccess() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(new Store(1, 1, address)));
        when(addressRepository.findById(5)).thenReturn(Optional.of(address));
        when(customerRepository.existsByEmailIgnoreCase("juana@sakila.com")).thenReturn(false);
        Customer saved = new Customer(100, "JUANA", "PEREZ", "juana@sakila.com", true, 1, address, LocalDate.now());
        when(customerRepository.save(newCustomer)).thenReturn(saved);

        Customer result = customerService.createCustomer(newCustomer);

        assertSame(saved, result);
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void createCustomerWithMissingStoreThrows() {
        when(storeRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.createCustomer(newCustomer));

        assertEquals("Tienda no encontrada", ex.getMessage());
        verify(addressRepository, never()).findById(anyInt());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomerWithMissingAddressThrows() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(new Store(1, 1, address)));
        when(addressRepository.findById(5)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.createCustomer(newCustomer));

        assertEquals("Dirección no encontrada", ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomerWithDuplicateEmailThrows() {
        when(storeRepository.findById(1)).thenReturn(Optional.of(new Store(1, 1, address)));
        when(addressRepository.findById(5)).thenReturn(Optional.of(address));
        when(customerRepository.existsByEmailIgnoreCase("juana@sakila.com")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class,
                () -> customerService.createCustomer(newCustomer));

        assertEquals("El correo electrónico ya está registrado", ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomerSuccess() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmailIgnoreCase("new@sakila.com")).thenReturn(Optional.empty());
        when(addressRepository.findById(5)).thenReturn(Optional.of(address));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.updateCustomer(1,
                new Customer(1, "MARIA", "SMITH", "new@sakila.com", true, 1, address, LocalDate.of(2026, 1, 1)));

        assertEquals("new@sakila.com", result.email());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomerThrowsWhenCustomerMissing() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(999, customer));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void updateCustomerWithEmailUsedByOtherThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        Customer other = new Customer(2, "PEDRO", "GOMEZ", "mary@sakila.com", true, 1, address, LocalDate.now());
        when(customerRepository.findByEmailIgnoreCase("mary@sakila.com")).thenReturn(Optional.of(other));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> customerService.updateCustomer(1, customer));

        assertEquals("El correo electrónico ya está registrado", ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomerKeepingOwnEmailDoesNotThrow() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmailIgnoreCase("mary@sakila.com")).thenReturn(Optional.of(customer));
        when(addressRepository.findById(5)).thenReturn(Optional.of(address));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.updateCustomer(1, customer);

        assertEquals("mary@sakila.com", result.email());
    }

    @Test
    void updateCustomerWithMissingAddressThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.findByEmailIgnoreCase("mary@sakila.com")).thenReturn(Optional.of(customer));
        when(addressRepository.findById(5)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.updateCustomer(1, customer));

        assertEquals("Dirección no encontrada", ex.getMessage());
    }

    @Test
    void changeCustomerStatusSuccess() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = customerService.changeCustomerStatus(1, false);

        assertEquals(false, result.active());
        assertEquals("MARY", result.firstName());
        assertEquals(1, result.storeId());
        assertEquals(LocalDate.of(2026, 1, 1), result.createDate());
    }

    @Test
    void changeCustomerStatusThrowsWhenCustomerMissing() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.changeCustomerStatus(999, false));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void getCustomerRentalsSuccess() {
        PageResult<Rental> page = PageResult.of(List.of(), 0, 20, 0);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(rentalRepository.findByCustomerId(1, query)).thenReturn(page);

        PageResult<Rental> result = customerService.getCustomerRentals(1, query);

        assertSame(page, result);
    }

    @Test
    void getCustomerRentalsThrowsWhenCustomerMissing() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> customerService.getCustomerRentals(999, query));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(rentalRepository, never()).findByCustomerId(anyInt(), any());
    }
}
