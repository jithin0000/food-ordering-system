package org.example.order.domain.service.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
public class OrderAddress {

    @NotNull
    private final String street;
    @NotNull
    private final String postalCode;
    @NotNull
    private final String city;
}
