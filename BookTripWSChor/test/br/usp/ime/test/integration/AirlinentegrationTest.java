package br.usp.ime.test.integration;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.booktrip.utils.MessageTraceQueue;
import br.usp.ime.test.utils.Bash;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class AirlinentegrationTest {

	
	private static TravelerWSService travelerService;
	private static TravelerWS travelerStub;
	private static MessageTraceQueue queue;
	
	
	final static String DESTINATION = "London";
	final static String NAME = "Saywer";
	final static String DATE = "12-23-2010";
	final static String CREDIT_CARD_NUMBER = "789653-2";
	final static String FLIGHT_ID = "5175";
	
	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		queue = new MessageTraceQueue();
	}
	
	@Test
	public void shouldReturnAFlightSearched() {
		travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String actualContent = queue.get("airline", "travelAgency", "response");
		
		assertEquals("available|" + FLIGHT_ID + "|2000|United Airlines", actualContent);
	}
	
	@Test
	public void shouldReturnThePriceCorrectlyAfterReservingAFlight(){
		travelerStub.reserveTicket(FLIGHT_ID);
		String actualContent = queue.get("airline", "travelAgency", "response");
		
		assertEquals("R" + FLIGHT_ID + "-1|2000", actualContent);
	}
	/*
	@Test
	public void shouldSendToTravelerTheCorrectETicket() {
		travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String reserve = travelerStub.reserveTicket(FLIGHT_ID);
		travelerStub.bookReserve(reserve);
		String actualContent = queue.get("airline", "traveler", "responseEticket");
		
		assertEquals("e-ticket for flight " + FLIGHT_ID + "\n" + "passenger: " + NAME, actualContent);
	}*/
	

}
