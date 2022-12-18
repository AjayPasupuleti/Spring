package com.infy.ekart.service.test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.entity.CartProduct;
import com.infy.ekart.entity.CustomerCart;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CustomerCartRepository;
import com.infy.ekart.service.CustomerCartService;
import com.infy.ekart.service.CustomerCartServiceImpl;

@SpringBootTest
class CustomerCartServiceTest {
	
	// Write testcases here
	
	@Mock
	private CustomerCartRepository customerCartRepository;

	@Mock
	private CustomerCartRepository CartProductRepository;

	@InjectMocks
	CustomerCartService customerCartService = new CustomerCartServiceImpl();

	public void getAllProductsValid1() throws EKartException {

		// write your logic here

	}
	
	public void getAllProductsValid2() throws EKartException {
		
		// write your logic here
		
	}

	public void addProductToCartValidTest() throws EKartException {

		// write your logic here

	}

	public void getProductFromCartInvalidTest1() throws EKartException {

		// write your logic here

	}

	public void getProductFromCartInvalidTest2() throws EKartException {

		// write your logic here

	}
	
	public void deleteProductFromCartValid1() throws EKartException {

		// write your logic here
		
	}

	public void deleteProductFromCartValid2() throws EKartException {

		// write your logic here

	}

	public void deleteProductFromCartInValidTest1() {

		// write your logic here

	}

	public void deleteProductFromCartInValidTest2() {

		// write your logic here

	}
	
	@Test
	public void modifyQuantityOfProductInCartInValidTest1() {
		String email = "name@infosys.com";
		Integer productId = 135;
		Integer quantity = 1;
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}
	
	@Test
	public void modifyQuantityOfProductInCartInvalidTest2() {
		String email = "name@infosys.com";
		Integer productId = 135;
		Integer quantity = 4;
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		CartProduct cartProduct = new CartProduct();
		cartProduct.setProductId(132);
		cartProduct.setQuantity(87);
		cartProductSet.add(cartProduct);
		customerCart.setCartProducts(cartProductSet);
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.PRODUCT_ALREADY_NOT_AVAILABLE", exp.getMessage());

	}

	@Test
	public void modifyQuantityOfProductInCartInvalidTest3() {

		String email = "name@infosys.com";
		Integer productId = 135;
		Integer quantity = 4;
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

	@Test
	public void modifyQuantityOfProductInCartInValidTest4() {
		String email = "name@infosys.com";
		Integer productId = 135;
		Integer quantity = 1;
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

	@Test
	public void deleteAllProductsFromCartValidTest() {
		String email = "name@infosys.com";
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

	@Test
	public void deleteAllProductFromCartInValidTest1() {
		String email = "name@infosys.com";
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		customerCart.setCartProducts(cartProductSet);
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());

	}

	@Test
	public void modifyQuantityOfProductInCartInValidTest2() {
		String email = "name@infosys.com";
		Integer productId = 135;
		Integer quantity = 4;
		CustomerCart customerCart = new CustomerCart();
		customerCart.setCustomerEmailId(email);
		Set<CartProduct> cartProductSet = new HashSet<>();
		customerCart.setCartProducts(cartProductSet);
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString()))
				.thenReturn(Optional.of(customerCart));
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(email, productId, quantity));
		Assertions.assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exp.getMessage());

	}	

	@Test
	public void deleteAllProductsFromCartIValidTest3() {
		String email = "name@infosys.com";
		Mockito.when(customerCartRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.deleteAllProductsFromCart(email));
		Assertions.assertEquals("CustomerCartService.NO_CART_FOUND", exp.getMessage());
	}

}
