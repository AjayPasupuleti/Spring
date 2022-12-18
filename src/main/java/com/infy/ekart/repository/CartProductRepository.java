package com.infy.ekart.repository;




//import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.CartProduct;



public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {
	//List<CartProduct> findByCartId(Integer id);
}
