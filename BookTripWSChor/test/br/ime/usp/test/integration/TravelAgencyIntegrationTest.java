package br.ime.usp.test.integration;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.test.utils.Bash;
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
	
	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		queue = new MessageTraceQueue();
		queue.initDB();
	}
	
	@Test
	public void shouldPassFlightInputDataToAirlineCorrectly(){
		travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String actualContent = queue.get("travelAgency", "airline", "search");
		
		assertEquals(DESTINATION + "|" + DATE, actualContent);
	}
	
	@Test
	public void shouldPassReserveInputDataToAirlineCorrectly(){
		travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String actualContent = queue.get("travelAgency", "airline", "search");
		
		assertEquals(DESTINATION + "|" + DATE, actualContent);
	}

}
