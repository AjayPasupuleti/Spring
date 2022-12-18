package com.infy.ekart.api;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;

import com.infy.ekart.service.CustomerProductService;

// add the missing annotations
@RestController
@RequestMapping(value = "/product-api")
@CrossOrigin(origins="http://localhost:4200")
public class ProductAPI {

	@Autowired
	private CustomerProductService customerProductService;

	@Autowired
    private Environment  environment;    
    
    Log logger = LogFactory.getLog(ProductAPI.class);
    
    
    // Get all the product details by calling getAllProducts() of
    // CustomerProductService and return the same
    
	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws EKartException {
        
		// write your logic here
		logger.info("Received a request to get All product details");
		List<ProductDTO> lpd=customerProductService.getAllProducts();
		return new ResponseEntity<>(lpd,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/product/{productId}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer productId) throws EKartException {

		logger.info("Received a request to get product details for product with productId as "+productId);
		ProductDTO product = customerProductService.getProductById(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}
	
	
	@PutMapping(value = "/update/{productId}")
	
	public ResponseEntity<String> reduceAvailableQuantity(@PathVariable Integer productId , @RequestBody String quantity) throws EKartException {

		logger.info("Received a request to update the available quantity  for product with productId as "+productId );
	    customerProductService.reduceAvailableQuantity(productId, Integer.parseInt(quantity));
		return new ResponseEntity<>(environment.getProperty("ProductAPI.REDUCE_QUANTITY_SUCCESSFULL"),HttpStatus.OK);

	}
}
