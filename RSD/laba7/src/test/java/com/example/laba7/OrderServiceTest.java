package com.example.laba7;

import com.example.laba7.model.*;
import com.example.laba7.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class OrderServiceTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        Customer customer = new Customer();
        customer.setName("John Doe");
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setZipcode("12345");
        customer.setAddress(address);

        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setCustomer(customer);

        Item item = new Item();
        item.setDescription("Test Item");
        Weight weight = new Weight();
        weight.setValue(new java.math.BigDecimal("1.5"));
        Measurement weightMeasurement = new Measurement();
        weightMeasurement.setName("kg");
        weight.setMeasurement(weightMeasurement);
        item.setShippingWeight(weight);

        OrderDetail orderDetail = new OrderDetail();
        Quantity quantity = new Quantity();
        quantity.setValue(2);
        Measurement quantityMeasurement = new Measurement();
        quantityMeasurement.setName("units");
        quantity.setMeasurement(quantityMeasurement);
        orderDetail.setQuantity(quantity);
        orderDetail.setTaxStatus("TAXABLE");
        orderDetail.setOrder(order);
        orderDetail.setItem(item);
        order.setOrderDetails(List.of(orderDetail));

        Cash cash = new Cash();
        cash.setAmount(100.0f);
        cash.setCashTendered(110.0f);
        cash.setOrder(order);

        order.setPayments(List.of(cash));
        orderRepository.save(order);
    }

    @Test
    void testFindOrdersByCriteria() {
        List<Order> orders = orderRepository.findOrdersByCriteria(
                "123 Main St", "12345", null, null, Cash.class.getSimpleName(), "PENDING");
        assertEquals(1, orders.size());
    }
}