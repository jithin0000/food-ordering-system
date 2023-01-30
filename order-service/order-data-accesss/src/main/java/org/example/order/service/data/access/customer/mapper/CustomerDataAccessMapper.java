package org.example.order.service.data.access.customer.mapper;

import org.example.domain.valueobject.CustomerId;
import org.example.order.service.data.access.customer.entity.CustomerEntity;
import org.example.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity entity)
    {
        return new Customer(new CustomerId(entity.getId()));
    }

}