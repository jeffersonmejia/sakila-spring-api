package com.sakila.api.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sakila.api.common.exception.ConflictException;
import com.sakila.api.common.exception.NotFoundException;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.Inventory;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;
import com.sakila.api.domain.model.SortDirection;
import com.sakila.api.domain.model.Staff;
import com.sakila.api.domain.port.out.CustomerRepository;
import com.sakila.api.domain.port.out.InventoryRepository;
import com.sakila.api.domain.port.out.RentalRepository;
import com.sakila.api.domain.port.out.StaffRepository;
import com.sakila.api.domain.service.RentalService;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private RentalService rentalService;

    private final PageQuery query = PageQuery.of(0, 20, "id", SortDirection.ASC);
    private final Customer active = new Customer(1, "MARY", "SMITH", "mary@sakila.com", true, 1, null, null);
    private final Customer inactive = new Customer(2, "PEDRO", "GOMEZ", "pedro@sakila.com", false, 1, null, null);
    private final Inventory copy = new Inventory(10, 1, "ACADEMY", 1, true);
    private final Staff staff = new Staff(3, "MIKE", "HILL", "mike@sakila.com", "mike", true);
    private final Rental newRental = new Rental(null, null, null, 1, 10, null, 3);
    private final Rental activeRental = new Rental(50, Instant.now(), null, 1, 10, "ACADEMY", 3);
    private final Rental returnedRental = new Rental(60, Instant.now(), Instant.now(), 1, 10, "ACADEMY", 3);

    @Test
    void createRentalSuccess() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(active));
        when(inventoryRepository.findById(10)).thenReturn(Optional.of(copy));
        when(rentalRepository.hasActiveRental(10)).thenReturn(false);
        when(staffRepository.findById(3)).thenReturn(Optional.of(staff));
        when(rentalRepository.save(newRental)).thenReturn(activeRental);

        Rental result = rentalService.createRental(newRental);

        assertSame(activeRental, result);
        verify(rentalRepository).save(newRental);
    }

    @Test
    void createRentalWithMissingCustomerThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> rentalService.createRental(newRental));

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void createRentalWithInactiveCustomerThrows() {
        Rental rentalForInactive = new Rental(null, null, null, 2, 10, null, 3);
        when(customerRepository.findById(2)).thenReturn(Optional.of(inactive));

        ConflictException ex = assertThrows(ConflictException.class,
                () -> rentalService.createRental(rentalForInactive));

        assertEquals("El cliente no está activo", ex.getMessage());
        verify(inventoryRepository, never()).findById(anyInt());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void createRentalWithMissingCopyThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(active));
        when(inventoryRepository.findById(10)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> rentalService.createRental(newRental));

        assertEquals("Copia no encontrada", ex.getMessage());
        verify(staffRepository, never()).findById(anyInt());
    }

    @Test
    void createRentalWithOccupiedCopyThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(active));
        when(inventoryRepository.findById(10)).thenReturn(Optional.of(copy));
        when(rentalRepository.hasActiveRental(10)).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class, () -> rentalService.createRental(newRental));

        assertEquals("La copia seleccionada no está disponible", ex.getMessage());
        verify(staffRepository, never()).findById(anyInt());
    }

    @Test
    void createRentalWithMissingStaffThrows() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(active));
        when(inventoryRepository.findById(10)).thenReturn(Optional.of(copy));
        when(rentalRepository.hasActiveRental(10)).thenReturn(false);
        when(staffRepository.findById(3)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> rentalService.createRental(newRental));

        assertEquals("Empleado no encontrado", ex.getMessage());
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void getRentalReturnsExisting() {
        when(rentalRepository.findById(50)).thenReturn(Optional.of(activeRental));

        Rental result = rentalService.getRental(50);

        assertSame(activeRental, result);
    }

    @Test
    void getRentalThrowsWhenMissing() {
        when(rentalRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> rentalService.getRental(999));

        assertEquals("Alquiler no encontrado", ex.getMessage());
    }

    @Test
    void listActiveDelegates() {
        PageResult<Rental> page = PageResult.of(List.of(activeRental), 0, 20, 1);
        when(rentalRepository.findActive(query)).thenReturn(page);

        PageResult<Rental> result = rentalService.listActive(query);

        assertSame(page, result);
    }

    @Test
    void listOverdueDelegates() {
        PageResult<Rental> page = PageResult.of(List.of(activeRental), 0, 20, 1);
        when(rentalRepository.findOverdue(query)).thenReturn(page);

        PageResult<Rental> result = rentalService.listOverdue(query);

        assertSame(page, result);
    }

    @Test
    void customerRentalsDelegates() {
        PageResult<Rental> page = PageResult.of(List.of(activeRental), 0, 20, 1);
        when(customerRepository.findById(1)).thenReturn(Optional.of(active));
        when(rentalRepository.findByCustomerId(1, query)).thenReturn(page);

        PageResult<Rental> result = rentalService.customerRentals(1, query);

        assertSame(page, result);
    }

    @Test
    void customerRentalsThrowsWhenCustomerMissing() {
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> rentalService.customerRentals(999, query));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void returnRentalSuccess() {
        when(rentalRepository.findById(50)).thenReturn(Optional.of(activeRental));
        Rental returned = new Rental(50, activeRental.rentalDate(), Instant.now(), 1, 10, "ACADEMY", 3);
        when(rentalRepository.markReturned(anyInt(), any(Instant.class))).thenReturn(returned);

        Rental result = rentalService.returnRental(50);

        assertSame(returned, result);
        verify(rentalRepository).markReturned(org.mockito.ArgumentMatchers.eq(50), any(Instant.class));
    }

    @Test
    void returnRentalAlreadyReturnedThrows() {
        when(rentalRepository.findById(60)).thenReturn(Optional.of(returnedRental));

        ConflictException ex = assertThrows(ConflictException.class, () -> rentalService.returnRental(60));

        assertEquals("El alquiler ya fue devuelto", ex.getMessage());
        verify(rentalRepository, never()).markReturned(anyInt(), any());
    }

    @Test
    void returnRentalThrowsWhenMissing() {
        when(rentalRepository.findById(999)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> rentalService.returnRental(999));

        assertEquals("Alquiler no encontrado", ex.getMessage());
    }
}
