package br.usp.ime.test.integration;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.booktrip.utils.MessageTraceQueue;
import br.usp.ime.test.utils.Bash;
import br.usp.ime.ws.traveler.Flight;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class AcquireIntegrationTest {

	
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
	}
	
	
	@Test
	public void shouldPayTravelAgencyAndPayAirline(){
		Flight flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		String reserve = travelerStub.reserveTicket(flight.getId());
		travelerStub.bookReserve(reserve);
		
		//Checking message send to Travel Agency
		String actualContent = queue.get("acquire", "travelAgency", "approval");
		assertEquals("Credit card approved" + "|" + 100, actualContent);
		
		//Checking message send to Airline
		actualContent = queue.get("acquire", "airline", "payment");
		assertEquals(flight.getPrice() + "|" + reserve + "|" + 
										NAME + "|" + CREDIT_CARD_NUMBER , actualContent);
	}
	
	@Test
	public void shouldNotifyFailureTiTravelAgencyAndAirline(){
		Flight flight = travelerStub.orderTrip(DESTINATION, DATE, "Jack", "XXXXXX");
		String reserve = travelerStub.reserveTicket(flight.getId());
		travelerStub.bookReserve(reserve);
		
		//Checking message send to Travel Agency
		String actualContent = queue.get("acquire", "travelAgency", "notificationTA");
		assertEquals("Credit card not approved", actualContent);
		
	}
	
}
