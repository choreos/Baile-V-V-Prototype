package br.ime.usp.test.integration;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.test.utils.Bash;
import br.ime.usp.ws.traveler.Flight;
import br.ime.usp.ws.traveler.TravelerWS;
import br.ime.usp.ws.traveler.TravelerWSService;
import br.usp.ime.booktrip.utils.MessageTraceQueue;

public class TravelAgencyIntegrationTest {

	
	private static TravelerWSService travelerService;
	private static TravelerWS travelerStub;
	private static MessageTraceQueue queue;
	
	
	final static String DESTINATION = "London";
	final static String NAME = "Saywer";
	final static String DATE = "12-23-2010";
	final static String CREDIT_CARD_NUMBER = "789653-2";
	final static String TA_NAME = "United Airlines";
	
	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		queue = new MessageTraceQueue();
	}
	
	@Test
	public void shouldPassFlightInputDataToAirlineCorrectly(){
		travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String actualContent = queue.get("travelAgency", "airline", "search");
		
		assertEquals(DESTINATION + "|" + DATE, actualContent);
	}
	
	@Test
	public void shouldPassReserveInputDataToAirlineCorrectly(){
		Flight flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		travelerStub.reserveTicket(flight.getId());
		String actualContent = queue.get("travelAgency", "airline", "reserve");
		
		assertEquals(flight.getId() + "|" + TA_NAME, actualContent);
	}
	
	@Test
	public void shouldContactAcquireAndTravelerWhenBookIsSuccess(){
		Flight flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String reserve = travelerStub.reserveTicket(flight.getId());
		travelerStub.bookReserve(reserve);
		
		
		//Checking message sent to Acquire
		String actualContent = queue.get("travelAgency", "acquire", "check");
		int totalPrice = Integer.parseInt(flight.getPrice()) + 100;
		assertEquals(reserve + "|" + NAME + "|" + CREDIT_CARD_NUMBER + "|"
							 + totalPrice, actualContent);
		
		
		//Checking message sent to Traveler
		String expectedContent = "Name: " + NAME + "\n" +
		   "Credit card: " + CREDIT_CARD_NUMBER + "\n" +
		   "Value discounted: $" + totalPrice;
		actualContent = queue.get("travelAgency", "traveler", "statement");
		assertEquals(expectedContent, actualContent);
	}

}
