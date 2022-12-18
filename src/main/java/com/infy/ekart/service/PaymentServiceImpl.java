package com.infy.ekart.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.TransactionDTO;
import com.infy.ekart.dto.TransactionStatus;
import com.infy.ekart.entity.Card;
import com.infy.ekart.entity.Transaction;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CardRepository;
import com.infy.ekart.repository.TransactionRepository;
import com.infy.ekart.utility.HashingUtility;

//add the missing annotations
@Service("PaymentService")
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	public Integer addCustomerCard(String customerEmailId, CardDTO cardDTO)
			throws EKartException, NoSuchAlgorithmException {

		List<Card> listOfCustomerCards = cardRepository.findByCustomerEmailId(customerEmailId);
		if (listOfCustomerCards.isEmpty())
			throw new EKartException("PaymentService.CUSTOMER_NOT_FOUND");
		cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
		Card newCard = new Card();
		newCard.setCardId(cardDTO.getCardId());
		newCard.setNameOnCard(cardDTO.getNameOnCard());
		;
		newCard.setCardNumber(cardDTO.getCardNumber());
		newCard.setCardType(cardDTO.getCardType());
		newCard.setExpiryDate(cardDTO.getExpiryDate());
		newCard.setCvv(cardDTO.getHashCvv());
		newCard.setCustomerEmailId(cardDTO.getCustomerEmailId());

		cardRepository.save(newCard);
		return newCard.getCardID();
	}

	@Override
	public void updateCustomerCard(CardDTO cardDTO) throws EKartException, NoSuchAlgorithmException {

		Optional<Card> optionalCard = cardRepository.findById(cardDTO.getCardId());
		Card card = optionalCard.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));
		cardDTO.setHashCvv(HashingUtility.getHashValue(cardDTO.getCvv().toString()));
		card.setCardId(cardDTO.getCardId());
		card.setNameOnCard(cardDTO.getNameOnCard());
		card.setCardNumber(cardDTO.getCardNumber());
		card.setCardType(cardDTO.getCardType());
		card.setCvv(cardDTO.getHashCvv());
		card.setExpiryDate(cardDTO.getExpiryDate());
		card.setCustomerEmailId(cardDTO.getCustomerEmailId());

	}

	@Override
	public void deleteCustomerCard(String customerEmailId, Integer cardId) throws EKartException {

		List<Card> listOfCustomerCards = cardRepository.findByCustomerEmailId(customerEmailId);
		if (listOfCustomerCards.isEmpty())
			throw new EKartException("PaymentService.CUSTOMER_NOT_FOUND");

		Optional<Card> optionalCards = cardRepository.findById(cardId);
		Card card = optionalCards.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));
		cardRepository.delete(card);

	}

	@Override
	public CardDTO getCard(Integer cardId) throws EKartException {

		Optional<Card> optionalCards = cardRepository.findById(cardId);
		Card card = optionalCards.orElseThrow(() -> new EKartException("PaymentService.CARD_NOT_FOUND"));
		CardDTO cardDTO = new CardDTO();
		cardDTO.setCardId(card.getCardID());
		cardDTO.setNameOnCard(card.getNameOnCard());
		cardDTO.setCardNumber(card.getCardNumber());
		cardDTO.setCardType(card.getCardType());
		cardDTO.setHashCvv(card.getCvv());
		cardDTO.setExpiryDate(card.getExpiryDate());
		cardDTO.setCustomerEmailId(card.getCustomerEmailId());
		return cardDTO;
	}

	@Override
	public List<CardDTO> getCustomerCardOfCardType(String customerEmailId, String cardType) throws EKartException {

		List<Card> cards = cardRepository.findByCustomerEmailIdAndCardType(customerEmailId, cardType);

		if (cards.isEmpty()) {
			throw new EKartException("PaymentService.CARD_NOT_FOUND");
		}
		List<CardDTO> cardDTOs = new ArrayList<CardDTO>();
		for (Card card : cards) {
			CardDTO cardDTO = new CardDTO();
			cardDTO.setCardId(card.getCardID());
			cardDTO.setNameOnCard(card.getNameOnCard());
			cardDTO.setCardNumber(card.getCardNumber());
			cardDTO.setCardType(card.getCardType());
			cardDTO.setHashCvv("XXX");
			cardDTO.setExpiryDate(card.getExpiryDate());
			cardDTO.setCustomerEmailId(card.getCustomerEmailId());

			cardDTOs.add(cardDTO);
		}
		return cardDTOs;
	}
	
	
	// Get the list of card details by using the customerEmailId and cardType
    // If the obtained list is empty throw EKartException with message
    // PaymentService.CARD_NOT_FOUND
    // Else populate the obtained card details and return

	@Override
	public List<CardDTO> getCardsOfCustomer(String customerEmailId, String cardType) throws EKartException {


		List<Card> cards = cardRepository.findByCustomerEmailId(customerEmailId);

		if (cards.isEmpty()) {
			throw new EKartException("PaymentService.CARD_NOT_FOUND");
		}
		List<CardDTO> cardDTOs = new ArrayList<CardDTO>();
		for (Card card : cards) {
			CardDTO cardDTO = new CardDTO();
			cardDTO.setCardId(card.getCardID());
			cardDTO.setNameOnCard(card.getNameOnCard());
			cardDTO.setCardNumber(card.getCardNumber());
			cardDTO.setCardType(card.getCardType());
			cardDTO.setHashCvv("XXX");
			cardDTO.setExpiryDate(card.getExpiryDate());
			cardDTO.setCustomerEmailId(card.getCustomerEmailId());

			cardDTOs.add(cardDTO);
		}
		return cardDTOs;
		
	}

	@Override
	public Integer addTransaction(TransactionDTO transactionDTO) throws EKartException {
		if (transactionDTO.getTransactionStatus().equals(TransactionStatus.TRANSACTION_FAILED)) {
			throw new EKartException("PaymentService.TRANSACTION_FAILED_CVV_NOT_MATCHING");
		}
		Transaction transaction = new Transaction();
		transaction.setCardId(transactionDTO.getCard().getCardId());

		transaction.setOrderId(transactionDTO.getOrder().getOrderId());
		transaction.setTotalPrice(transactionDTO.getTotalPrice());
		transaction.setTransactionDate(transactionDTO.getTransactionDate());
		transaction.setTransactionStatus(transactionDTO.getTransactionStatus());
		transactionRepository.save(transaction);

		return transaction.getTransactionId();
	}

	@Override
	public TransactionDTO authenticatePayment(String customerEmailId, TransactionDTO transactionDTO)
			throws EKartException, NoSuchAlgorithmException {
		if (!transactionDTO.getOrder().getCustomerEmailId().equals(customerEmailId)) {
			throw new EKartException("PaymentService.ORDER_DOES_NOT_BELONGS");

		}

		if (!transactionDTO.getOrder().getOrderStatus().equals("PLACED")) {
			throw new EKartException("PaymentService.TRANSACTION_ALREADY_DONE");

		}
		CardDTO cardDTO = getCard(transactionDTO.getCard().getCardId());
		if (!cardDTO.getCustomerEmailId().matches(customerEmailId)) {

			throw new EKartException("PaymentService.CARD_DOES_NOT_BELONGS");
		}
		if (!cardDTO.getCardType().equals(transactionDTO.getOrder().getPaymentThrough())) {

			throw new EKartException("PaymentService.PAYMENT_OPTION_SELECTED_NOT_MATCHING_CARD_TYPE");
		}
		if (cardDTO.getHashCvv().equals(HashingUtility.getHashValue(transactionDTO.getCard().getCvv().toString()))) {

			transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_SUCCESS);
		} else {

			transactionDTO.setTransactionStatus(TransactionStatus.TRANSACTION_FAILED);

		}

		return transactionDTO;
	}
}
