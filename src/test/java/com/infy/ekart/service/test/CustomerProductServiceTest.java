package com.infy.ekart.service.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.ProductRepository;
import com.infy.ekart.service.CustomerProductService;
import com.infy.ekart.service.CustomerProductServiceImpl;

@SpringBootTest
public class CustomerProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private CustomerProductService productService = new CustomerProductServiceImpl();

	void getAllProductsValid() throws EKartException {

		// write your logic here

	}

	@Test
	public void getProductByValidTest() throws EKartException {

		Product product = new Product();
		product.setProductId(1005);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
		ProductDTO productDTO = productService.getProductById(product.getProductId());
		Assertions.assertEquals(product.getProductId(), productDTO.getProductId());

	}

	@Test
	public void getProductByInValidTest() throws EKartException {
		Product product = new Product();
		product.setProductId(105);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EKartException excep = Assertions.assertThrows(EKartException.class,
				() -> productService.getProductById(product.getProductId()));
		Assertions.assertEquals("ProductService.PRODUCT_NOT_AVAILABLE", excep.getMessage());

	}

	@Test
	public void reduceAvailableQuantityInvalidTest() throws EKartException {
		Product product = new Product();
		product.setProductId(1005);
		product.setAvailableQuantity(3);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EKartException excep = Assertions.assertThrows(EKartException.class,
				() -> productService.reduceAvailableQuantity(product.getProductId(), product.getAvailableQuantity()));
		Assertions.assertEquals("ProductService.PRODUCT_NOT_AVAILABLE", excep.getMessage());

	}

	@Test
	public void reduceAvailableQuantityvalidTest() throws EKartException {
		Product product = new Product();
		product.setProductId(1005);
		product.setAvailableQuantity(3);

		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(product));
		Assertions.assertDoesNotThrow(
				() -> productService.reduceAvailableQuantity(product.getProductId(), product.getAvailableQuantity()));

	}

}
