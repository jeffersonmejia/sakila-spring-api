package com.sakila.api.domain.port.in;

import com.sakila.api.domain.model.Customer;
import com.sakila.api.domain.model.PageQuery;
import com.sakila.api.domain.model.PageResult;
import com.sakila.api.domain.model.Rental;

public interface CustomerUseCases {

    PageResult<Customer> listCustomers(PageQuery query);

    PageResult<Customer> searchCustomers(String search, PageQuery query);

    Customer getCustomer(Integer id);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Integer id, Customer customer);

    Customer changeCustomerStatus(Integer id, Boolean active);

    PageResult<Rental> getCustomerRentals(Integer id, PageQuery query);
}
