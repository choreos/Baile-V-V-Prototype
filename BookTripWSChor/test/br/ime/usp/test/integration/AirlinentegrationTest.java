package br.ime.usp.test.integration;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.test.utils.Bash;
import br.ime.usp.ws.traveler.Flight;
import br.ime.usp.ws.traveler.TravelerWS;
import br.ime.usp.ws.traveler.TravelerWSService;
import br.usp.ime.booktrip.utils.MessageTraceQueue;

public class AirlinentegrationTest {

	
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
	public void shouldPassTheCorrectValueToTravelAgency(){
		Flight flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String reserve = travelerStub.reserveTicket(flight.getId());
		
		travelerStub.bookReserve(reserve);
		String expected = queue.get("airline", "travelagency", "value_paid");
		
		assertEquals("R$ 2000", expected);
	
	
	}
	

}
