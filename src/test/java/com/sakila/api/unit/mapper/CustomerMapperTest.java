package com.sakila.api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.sakila.api.adapter.in.web.dto.AddressResponse;
import com.sakila.api.adapter.in.web.dto.CityResponse;
import com.sakila.api.adapter.in.web.dto.CountryResponse;
import com.sakila.api.adapter.in.web.dto.CustomerRequest;
import com.sakila.api.adapter.in.web.dto.CustomerResponse;
import com.sakila.api.adapter.in.web.dto.CustomerUpdateRequest;
import com.sakila.api.domain.model.Address;
import com.sakila.api.domain.model.City;
import com.sakila.api.domain.model.Country;
import com.sakila.api.domain.model.Customer;
import com.sakila.api.mapper.CustomerMapper;

class CustomerMapperTest {

    private final Country country = new Country(1, "United States");
    private final City city = new City(2, "Austin", country);
    private final Address address = new Address(3, "47 MySakila Drive", "Alberta", "1400", "555", city);
    private final Customer customer = new Customer(4, "MARY", "SMITH", "mary@sakila.com", true, 1, address,
            LocalDate.of(2026, 1, 10));

    @Test
    void toResponseMapsAllFields() {
        CustomerResponse response = CustomerMapper.toResponse(customer);

        assertEquals(4, response.id());
        assertEquals("MARY", response.firstName());
        assertEquals("SMITH", response.lastName());
        assertEquals("mary@sakila.com", response.email());
        assertEquals(true, response.active());
        assertEquals(1, response.storeId());
        assertEquals(LocalDate.of(2026, 1, 10), response.createDate());
        assertEquals(3, response.address().id());
    }

    @Test
    void toResponseWithNullAddressMapsNull() {
        Customer withoutAddress = new Customer(4, "MARY", "SMITH", "mary@sakila.com", true, 1, null,
                LocalDate.of(2026, 1, 10));

        assertNull(CustomerMapper.toResponse(withoutAddress).address());
    }

    @Test
    void toAddressResponseMapsCityNested() {
        AddressResponse response = CustomerMapper.toAddressResponse(address);

        assertEquals(3, response.id());
        assertEquals("47 MySakila Drive", response.address());
        assertEquals("Alberta", response.district());
        assertEquals("1400", response.postalCode());
        assertEquals("555", response.phone());
        assertEquals(2, response.city().id());
    }

    @Test
    void toAddressResponseWithNullCityMapsNull() {
        Address withoutCity = new Address(3, "47 MySakila Drive", "Alberta", "1400", "555", null);

        assertNull(CustomerMapper.toAddressResponse(withoutCity).city());
    }

    @Test
    void toCityResponseMapsCountryNested() {
        CityResponse response = CustomerMapper.toCityResponse(city);

        assertEquals(2, response.id());
        assertEquals("Austin", response.city());
        assertEquals(1, response.country().id());
    }

    @Test
    void toCountryResponseMapsFields() {
        CountryResponse response = CustomerMapper.toCountryResponse(country);

        assertEquals(1, response.id());
        assertEquals("United States", response.country());
    }

    @Test
    void toNewDomainBuildsCustomerWithNullIdAndActive() {
        CustomerRequest request = new CustomerRequest("NICK", "WAHLBERG", "nick@sakila.com", 1, 2);

        Customer result = CustomerMapper.toNewDomain(request);

        assertNull(result.id());
        assertEquals("NICK", result.firstName());
        assertEquals("WAHLBERG", result.lastName());
        assertEquals("nick@sakila.com", result.email());
        assertEquals(true, result.active());
        assertEquals(1, result.storeId());
        assertEquals(2, result.address().id());
        assertNull(result.createDate());
    }

    @Test
    void toUpdateDomainKeepsActiveAndCreateDate() {
        CustomerUpdateRequest request = new CustomerUpdateRequest("NICK", "WAHLBERG", "nick@sakila.com", 2);

        Customer result = CustomerMapper.toUpdateDomain(4, customer, request);

        assertEquals(4, result.id());
        assertEquals("NICK", result.firstName());
        assertEquals("nick@sakila.com", result.email());
        assertEquals(true, result.active());
        assertEquals(1, result.storeId());
        assertEquals(2, result.address().id());
        assertEquals(LocalDate.of(2026, 1, 10), result.createDate());
    }
}
