package com.sakila.api.mapper;

import com.sakila.api.adapter.in.web.dto.AddressResponse;
import com.sakila.api.adapter.in.web.dto.CityResponse;
import com.sakila.api.adapter.in.web.dto.CountryResponse;
import com.sakila.api.adapter.in.web.dto.CustomerRequest;
import com.sakila.api.adapter.in.web.dto.CustomerResponse;
import com.sakila.api.adapter.in.web.dto.CustomerUpdateRequest;
import com.sakila.api.domain.model.Address;
import com.sakila.api.domain.model.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(customer.id(), customer.firstName(), customer.lastName(), customer.email(),
                customer.active(), customer.storeId(), customer.createDate(),
                customer.address() != null ? toAddressResponse(customer.address()) : null);
    }

    public static AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(address.id(), address.address(), address.district(), address.postalCode(),
                address.phone(),
                address.city() != null ? toCityResponse(address.city()) : null);
    }

    public static CityResponse toCityResponse(com.sakila.api.domain.model.City city) {
        return new CityResponse(city.id(), city.city(), toCountryResponse(city.country()));
    }

    public static CountryResponse toCountryResponse(com.sakila.api.domain.model.Country country) {
        return new CountryResponse(country.id(), country.country());
    }

    public static Customer toNewDomain(CustomerRequest request) {
        return new Customer(null, request.firstName(), request.lastName(), request.email(), true, request.storeId(),
                new Address(request.addressId(), null, null, null, null, null), null);
    }

    public static Customer toUpdateDomain(Integer id, Customer current, CustomerUpdateRequest request) {
        return new Customer(id, request.firstName(), request.lastName(), request.email(), current.active(),
                current.storeId(), new Address(request.addressId(), null, null, null, null, null), current.createDate());
    }
}
