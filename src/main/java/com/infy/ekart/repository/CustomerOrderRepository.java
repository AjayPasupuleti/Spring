package com.infy.ekart.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.Order;


public interface CustomerOrderRepository extends CrudRepository<Order, Integer> {
	
	List<Order> findByCustomerEmailId(String customerEmailId);

}
