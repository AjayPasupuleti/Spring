package com.infy.ekart.service.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.TransactionDTO;
import com.infy.ekart.dto.TransactionStatus;
import com.infy.ekart.entity.Card;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CardRepository;
import com.infy.ekart.repository.TransactionRepository;
import com.infy.ekart.service.PaymentService;
import com.infy.ekart.service.PaymentServiceImpl;

@SpringBootTest
public class CustomerCardsServiceTest {
	
	@Mock
	private CardRepository cardRepository;
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	PaymentService paymentService = new PaymentServiceImpl();

	@Test
	void addCustomerCardInvalidTest1() throws EKartException {
		String email = "abc@gamil.com";
		Integer id = 1234;
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(id);
		Mockito.when(cardRepository.findByCustomerEmailId(email)).thenReturn(new ArrayList<>());
		EKartException e = Assertions.assertThrows(EKartException.class,
				() -> paymentService.addCustomerCard(email, cardDTO));
		Assertions.assertEquals("PaymentService.CUSTOMER_NOT_FOUND", e.getMessage());
	}
	
	@Test
	void addCustomerCardInValidTest2() throws EKartException {
		CardDTO cardDTO = new CardDTO();
		String emailId = "tom@infosys.com";
		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(new ArrayList<>());
		Exception exp = Assertions.assertThrows(EKartException.class,
				() -> paymentService.addCustomerCard(emailId, cardDTO));
		Assertions.assertEquals("PaymentService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void updateCustomerCardValidTest() throws EKartException {
		Card card = new Card();
		card.setCardId(122);
		card.setNameOnCard("AIM");
		card.setCvv("466");
		card.setCardType("6642150005012186");
		card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
		card.setExpiryDate(LocalDate.of(2028,02,24));
		//card.setExpiryDate("2028-02-24");
		card.setCustomerEmailId("tom@infosys.com");
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(100);
		cardDTO.setCvv(466);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		Assertions.assertDoesNotThrow(() -> paymentService.updateCustomerCard(cardDTO));
	}

	@Test
	void updateCustomerCardInValidTest() throws EKartException {
		CardDTO cardDTO = new CardDTO();
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		EKartException exp = Assertions.assertThrows(EKartException.class,
				() -> paymentService.updateCustomerCard(cardDTO));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteCustomerCardValidTest() throws EKartException {
		List<Card> cards = new ArrayList<>();
		Card card = new Card();
		card.setCustomerEmailId("tom@infosys.com");
		card.setCardId(122);
		cards.add(card);
		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(cards);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		Assertions.assertDoesNotThrow(() -> paymentService.deleteCustomerCard("tom@infosys.com", 122));
	}

	@Test
	void deleteCustomerCardInValidTest1() throws EKartException {
		String customerEmailId = "tom@infosys.com";
		Integer cardId = 100;
		Card card1 = new Card();
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card1));
		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(new ArrayList<>());
		Exception exp = Assertions.assertThrows(EKartException.class,
				() -> paymentService.deleteCustomerCard(customerEmailId, cardId));
		Assertions.assertEquals("PaymentService.CUSTOMER_NOT_FOUND", exp.getMessage());
	}

	@Test
	void deleteCustomerCardInValidTest2() throws EKartException {
		String customerEmailId = "tom@infosys.com";
		Integer cardId = 100;
		List<Card> cards = new ArrayList<>();
		Card card = new Card();
		cards.add(card);

		Mockito.when(cardRepository.findByCustomerEmailId(Mockito.anyString())).thenReturn(cards);
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EKartException.class,
				() -> paymentService.deleteCustomerCard(customerEmailId, cardId));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}

	@Test
	void getCardValidTest() throws EKartException {
		Card card = new Card();
		card.setCardID(100);
		card.setCvv("466");
		card.setExpiryDate(LocalDate.of(2028,02,24));
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));
		CardDTO cardDTO = paymentService.getCard(card.getCardID());
		Assertions.assertEquals(card.getCardID(), cardDTO.getCardId());
	}
	
	@Test
	void getCardInValidTest() throws EKartException {
		Integer cardId = 100;
		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Exception exp = Assertions.assertThrows(EKartException.class, () -> paymentService.getCard(cardId));
		Assertions.assertEquals("PaymentService.CARD_NOT_FOUND", exp.getMessage());
	}
	
	@Test
	void getCardsOfCustomerValidTest() throws EKartException {
		
		// write your logic here
		
	}

	@Test
	void getCardsOfCustomerInValidTest() throws EKartException {
		
		// write your logic here
		
	}
	

	@Test
	void addTransactionInValidTest1() throws EKartException {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);
		Exception exp = Assertions.assertThrows(EKartException.class,
				() -> paymentService.addTransaction(transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING", exp.getMessage());
	}

	@Test
	void addTransactionInvalidTest2() throws EKartException {
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Assertions.assertDoesNotThrow(() -> paymentService.addTransaction(transactionDTO));
	}

	@Test
	void authenticatePaymentOnInValidTest1() throws EKartException {
		String customerEmailId = "tom@infosys.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		order.setCustomerEmailId("tom@infosys.com");
		order.setOrderStatus("CANCELLED");
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Exception e = Assertions.assertThrows(EKartException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());
	}

	@Test
	void authenticatePaymentOnInValidTest2() throws EKartException {
		String customerEmailId = "tom@infosys.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		CardDTO card = new CardDTO();
		card.setCardId(100);
		transactionDTO.setCard(card);
		OrderDTO order = new OrderDTO();
		order.setOrderId(111);
		order.setCustomerEmailId("tom@infosys.com");
		order.setOrderStatus("CANCELLED");
		transactionDTO.setOrder(order);
		transactionDTO.setTotalPrice(100.5d);
		transactionDTO.setTransactionDate(LocalDateTime.now());
		transactionDTO.setTransactionId(112);
		transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		Exception e = Assertions.assertThrows(EKartException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.TRANSACTION_ALREADY_DONE", e.getMessage());

	}

	@Test
	void authenticatePaymentInValidTest3() throws EKartException {
		String customerEmailId = "tom@infosys.com";
		TransactionDTO transactionDTO = new TransactionDTO();
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setCustomerEmailId("tom@infosys.com");
		orderDTO.setOrderStatus("PLACED");
		transactionDTO.setOrder(orderDTO);
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(1234);
		cardDTO.setCustomerEmailId("Customer@infy.com");
		transactionDTO.setCard(cardDTO);
		cardDTO.setCustomerEmailId("Customer@infy.com");
		transactionDTO.setCard(cardDTO);

		Card card = new Card();
		card.setCardType("CREDIT_CARD");
		card.setExpiryDate(LocalDate.of(2028,02,24));
		card.setNameOnCard("AIM");
		card.setCvv("466");
		card.setCardType("6642150005012186");
		card.setCardNumber("0c658eb5d61e88c86f37613342bbce6cbf278a9a86ba6514dc7e5c205f76c99f");
		card.setExpiryDate(LocalDate.of(2028,02,24));
		card.setCustomerEmailId("Customer@infy.com");

		Mockito.when(cardRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(card));

		Exception e = Assertions.assertThrows(EKartException.class,
				() -> paymentService.authenticatePayment(customerEmailId, transactionDTO));
		Assertions.assertEquals("PaymentService.CARD_DOES_NOT_BELONGS", e.getMessage());

	}
}
