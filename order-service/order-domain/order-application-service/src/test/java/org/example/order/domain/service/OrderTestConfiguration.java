package org.example.order.domain.service;

import org.example.order.domain.service.ports.output.message.publisher.payment.OrderCancelPaymentRequestMessagePublisher;
import org.example.order.domain.service.ports.output.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import org.example.order.domain.service.ports.output.message.publisher.restuarant.OrderPaidRestaurantRequestMessagePublisher;
import org.example.order.domain.service.ports.output.repository.CustomerRepository;
import org.example.order.domain.service.ports.output.repository.OrderRepository;
import org.example.order.domain.service.ports.output.repository.RestaurantRepository;
import org.example.order.service.domain.service.OrderDomainService;
import org.example.order.service.domain.service.OrderDomainServiceImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.example")
public class OrderTestConfiguration {

    @Bean
    public OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher(){
        return Mockito.mock(OrderCreatePaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelPaymentRequestMessagePublisher orderCancelPaymentRequestMessagePublisher(){
        return Mockito.mock(OrderCancelPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher(){
       return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository(){
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository(){
        return Mockito.mock(CustomerRepository.class);
    }


    @Bean
    public RestaurantRepository restaurantRepository(){
        return Mockito.mock(RestaurantRepository.class);
    }


    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceImpl();
    }






}
