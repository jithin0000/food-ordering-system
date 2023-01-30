package org.example.order.domain.service;

import org.example.domain.valueobject.*;
import org.example.order.domain.service.dto.create.CreateOrderCommand;
import org.example.order.domain.service.dto.create.CreateOrderResponse;
import org.example.order.domain.service.dto.create.OrderAddress;
import org.example.order.domain.service.dto.create.OrderItem;
import org.example.order.domain.service.mapper.OrderDataMapper;
import org.example.order.domain.service.ports.output.repository.CustomerRepository;
import org.example.order.domain.service.ports.output.repository.OrderRepository;
import org.example.order.domain.service.ports.output.repository.RestaurantRepository;
import org.example.order.service.domain.entity.Customer;
import org.example.order.service.domain.entity.Order;
import org.example.order.service.domain.entity.Product;
import org.example.order.service.domain.entity.Restaurant;
import org.example.order.service.domain.exception.OrderDomainException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    private final UUID CUSTOMER_ID = UUID.fromString("d329c64a-353a-4ffd-9c53-9aae8f35c3c6");
    private final UUID RESTAURANT_ID = UUID.fromString("e2ddbc5a-3c6a-464b-802d-e7d9597d33a3");
    private final UUID PRODUCT_ID = UUID.fromString("3a57a32e-f7c0-466f-a1ed-24d8935b15a2");
    private final UUID ORDER_ID = UUID.fromString("90265cff-a910-4c48-ae98-f68c0085048f");
    private final BigDecimal PRICE = new BigDecimal("200.00");
    private final BigDecimal WRONG_PRICE = new BigDecimal("250.00");
    @Autowired
    private OrderApplicationService orderApplicationService;
    @Autowired
    private OrderDataMapper orderDataMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;

    @BeforeAll
    void beforeAll() {
        createOrderCommand = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("1004")
                        .city("kochi")
                        .build()
                )
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()


                ))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("1004")
                        .city("kochi")
                        .build()
                )
                .price(WRONG_PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()


                ))
                .build();


        createOrderCommandWrongProductPrice = CreateOrderCommand
                .builder()
                .customerId(CUSTOMER_ID).restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street")
                        .postalCode("1004")
                        .city("kochi")
                        .build()
                )
                .price(new BigDecimal("210.00"))
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()


                ))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.Builder.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(
                Optional.of(customer)
        );
        when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrdreCommandToRestaurant(createOrderCommand)
        )).thenReturn(Optional.of(restaurantResponse));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    void testCreateOrder() {

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertEquals(OrderStatus.PENDING, createOrderResponse.getOrderStatus());
        assertEquals("Order created successfully", createOrderResponse.getMessage());
        assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    void testCreateOrderWithWrongPrice() {

        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

        assertEquals("Total price: "+WRONG_PRICE+ "" + "is not equal to order item total:  200.00", orderDomainException.getMessage());

    }


    @Test
    void testCreateOrderWithWrongProductPrice() {
        OrderDomainException orderDomainException = assertThrows(
                OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice)
        );

        assertEquals("Order item price is not valid for product ", orderDomainException.getMessage() );
    }

    @Test
    void testCreatedWithPassiveRestaurant() {
        Restaurant restaurantResponse = Restaurant.Builder.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(false)
                .build();
        when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrdreCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));
        OrderDomainException orderDomainException = assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommand));
        assertEquals("Restuarant with id "+RESTAURANT_ID+ " is not active", orderDomainException.getMessage());


    }
}



















