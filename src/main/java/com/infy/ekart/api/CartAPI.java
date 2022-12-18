package com.infy.ekart.api;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.ekart.dto.CartProductDTO;
import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.CustomerCartService;

//add the missing annotations
@RestController
@RequestMapping(value = "/cart-api")
@CrossOrigin(origins="http://localhost:4200")
public class CartAPI {

	@Autowired
	private CustomerCartService customerCartService;

	@Autowired
	private Environment environment;

	private RestTemplate template;

	Log logger = LogFactory.getLog(CartAPI.class);
	
	
	// Add product to customer cart by calling addProductToCart() method of
    // CustomerCartService which in turn return the cartId
    // Set the appropriate success message with cartId and return the same

	@PostMapping(value = "/products")
	public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCartDTO customerCartDTO)
			throws EKartException {
		// write your logic here
		logger.info("Received a request to add product to customer cart");
		Integer i=customerCartService.addProductToCart(customerCartDTO);
		String s=environment.getProperty("CustomerCartAPI.PRODUCT_ADDED_TO_CART");
		String message=s+String.valueOf(i); 
		return new ResponseEntity<>(message,HttpStatus.OK);
		
	}

	@GetMapping(value = "/customer/{customerEmailId}/products")
	public ResponseEntity<Set<CartProductDTO>> getProductsFromCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		logger.info("Received a request to get products details from " + customerEmailId + " cart");

		Set<CartProductDTO> cartProductDTOs = customerCartService.getProductsFromCart(customerEmailId);
		for (CartProductDTO cartProductDTO : cartProductDTOs) {
			logger.info("Product call");
			ProductDTO productDTO = template.getForEntity(
					"http://localhost:3333/Ekart/product-api/product/" + cartProductDTO.getProduct().getProductId(),
					ProductDTO.class).getBody();
			cartProductDTO.setProduct(productDTO);
			logger.info("Product complete");

		}
		return new ResponseEntity<Set<CartProductDTO>>(cartProductDTOs, HttpStatus.OK);

	}
	
	
	// Delete the product details from the cart of customer by calling
    // deleteProductFromCart() method of CustomerCartService
    // Set the appropriate success or failure message and return the same

	@DeleteMapping(value = "/customer/{customerEmailId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId,
			@NotNull(message = "{invalid.email.format}") @PathVariable("productId") Integer productId)
			throws EKartException {
		
		// write your logic here
		customerCartService.deleteProductFromCart(customerEmailId, productId);
		String message = environment.getProperty("CustomerCartAPI.PRODUCT_DELETED_FROM_CART_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PutMapping(value = "/customer/{customerEmailId}/product/{productId}")
	public ResponseEntity<String> modifyQuantityOfProductInCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId,
			@NotNull(message = "{invalid.email.format}") @PathVariable("productId") Integer productId,
			@RequestBody String quantity) throws EKartException {
		logger.info("Received a request to modify the quantity of +" + productId + " prouct from  " + customerEmailId
				+ " cart");

		customerCartService.modifyQuantityOfProductInCart(customerEmailId, productId, Integer.parseInt(quantity));
		String message = environment.getProperty("CustomerCartAPI.PRODUCT_QUANTITY_UPDATE_FROM_CART_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customer/{customerEmailId}/products")
	public ResponseEntity<String> deleteAllProductsFromCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.customeremail.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		logger.info("Received a request to clear " + customerEmailId + " cart");

		customerCartService.deleteAllProductsFromCart(customerEmailId);
		String message = environment.getProperty("CustomerCartAPI.ALL_PRODUCTS_DELETED");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

}
