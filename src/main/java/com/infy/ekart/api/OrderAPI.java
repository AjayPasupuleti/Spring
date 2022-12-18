package com.infy.ekart.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.ekart.dto.CartProductDTO;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.dto.OrderedProductDTO;
import com.infy.ekart.dto.PaymentThrough;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.CustomerOrderService;

//add the missing annotations
@RestController
@RequestMapping(value = "/order-api")
@CrossOrigin(origins="http://localhost:4200")
public class OrderAPI {
	
	@Autowired
	private CustomerOrderService orderService;

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate template;

	@PostMapping(value = "/place-order")
	public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderDTO order) throws EKartException {

		ResponseEntity<CartProductDTO[]> cartProductDTOsResponse = template.getForEntity(
				"http://localhost:3333/Ekart/cart-api/customer/" + order.getCustomerEmailId() + "/products",
				CartProductDTO[].class);

		CartProductDTO[] cartProductDTOs = cartProductDTOsResponse.getBody();
		template.delete(
				"http://localhost:3333/Ekart/cart-api/customer/" + order.getCustomerEmailId() + "/products");
		List<OrderedProductDTO> orderedProductDTOs = new ArrayList<>();

		for (CartProductDTO cartProductDTO : cartProductDTOs) {
			OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
			orderedProductDTO.setProduct(cartProductDTO.getProduct());
			orderedProductDTO.setQuantity(cartProductDTO.getQuantity());
			orderedProductDTOs.add(orderedProductDTO);
		}
		order.setOrderedProducts(orderedProductDTOs);

		Integer orderId = orderService.placeOrder(order);
		String modificationSuccessMsg = environment.getProperty("OrderAPI.ORDERED_PLACE_SUCCESSFULLY");

		return new ResponseEntity<String>(modificationSuccessMsg + orderId, HttpStatus.OK);

	}

	@GetMapping(value = "order/{orderId}")
	public ResponseEntity<OrderDTO> getOrderDetails(
			@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId) throws EKartException {
		OrderDTO orderDTO = orderService.getOrderDetails(orderId);
		for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

			ResponseEntity<ProductDTO> productResponse = template.getForEntity(
					"http://localhost:3333/Ekart/product-api/product/" + orderedProductDTO.getProduct().getProductId(),
					ProductDTO.class);
			orderedProductDTO.setProduct(productResponse.getBody());

		}

		return new ResponseEntity<OrderDTO>(orderDTO, HttpStatus.OK);

	}

	@GetMapping(value = "customer/{customerEmailId}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersOfCustomer(
			@NotNull(message = "{email.absent}") @PathVariable String customerEmailId) throws EKartException {
		List<OrderDTO> orderDTOs = orderService.findOrdersByCustomerEmailId(customerEmailId);
		for (OrderDTO orderDTO : orderDTOs) {
			for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

				ResponseEntity<ProductDTO> productResponse = template.getForEntity(
						"http://localhost:3333/Ekart/product-api/product/" + orderedProductDTO.getProduct().getProductId(),
						ProductDTO.class);
				orderedProductDTO.setProduct(productResponse.getBody());

			}

		}
		return new ResponseEntity<List<OrderDTO>>(orderDTOs, HttpStatus.OK);

	}

	@PutMapping(value = "order/{orderId}/update/order-status")
	public void updateOrderAfterPayment(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
			@RequestBody String transactionStatus) throws EKartException {
		if (transactionStatus.equals("TRANSACTION_SUCCESS")) {
			orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
			OrderDTO orderDTO = orderService.getOrderDetails(orderId);
			for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

				template.put(
						"http://localhost:3333/Ekart/product-api/update/" + orderedProductDTO.getProduct().getProductId(),
						orderedProductDTO.getQuantity());

			}

		} else {
			orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
		}
	}

	@PutMapping(value = "order/{orderId}/update/payment-through")
	public void updatePaymentOption(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
			@RequestBody String paymentThrough) throws EKartException {
		if (paymentThrough.equals("DEBIT_CARD")) {
			orderService.updatePaymentThrough(orderId, PaymentThrough.DEBIT_CARD);
		} else {

			orderService.updatePaymentThrough(orderId, PaymentThrough.CREDIT_CARD);
		}
	}

}
