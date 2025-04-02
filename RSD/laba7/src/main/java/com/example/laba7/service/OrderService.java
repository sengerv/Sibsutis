package com.example.laba7.service;

import com.example.laba7.model.Order;
import com.example.laba7.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findOrdersByCriteria(String street, String zipcode, LocalDateTime startDate, LocalDateTime endDate, String paymentType, String status) {
        return orderRepository.findOrdersByCriteria(street, zipcode, startDate, endDate, paymentType, status);
    }
}