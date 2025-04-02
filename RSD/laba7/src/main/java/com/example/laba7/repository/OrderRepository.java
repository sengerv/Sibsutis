package com.example.laba7.repository;

import com.example.laba7.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "JOIN o.customer c " +
            "JOIN o.payments p " +
            "WHERE (:street IS NULL OR c.address.street = :street) " +
            "AND (:zipcode IS NULL OR c.address.zipcode = :zipcode) " +
            "AND (:startDate IS NULL OR o.date >= :startDate) " +
            "AND (:endDate IS NULL OR o.date <= :endDate) " +
            "AND (:paymentType IS NULL OR p.class = :paymentType) " +
            "AND (:status IS NULL OR o.status = :status)")
    List<Order> findOrdersByCriteria(
            @Param("street") String street,
            @Param("zipcode") String zipcode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("paymentType") String paymentType,
            @Param("status") String status);
}