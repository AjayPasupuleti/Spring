package com.infy.ekart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}
