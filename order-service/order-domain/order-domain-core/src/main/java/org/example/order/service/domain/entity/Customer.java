package org.example.order.service.domain.entity;

import org.example.domain.entity.AggregrateRoot;
import org.example.domain.valueobject.CustomerId;

public class Customer extends AggregrateRoot<CustomerId> {
    public Customer(){}

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
