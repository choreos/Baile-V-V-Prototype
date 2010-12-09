package br.ime.usp.test.integration;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.restclient.RESTClient;
import br.ime.usp.test.utils.Bash;
import br.ime.usp.ws.airline.AirlineWS;
import br.ime.usp.ws.airline.AirlineWSService;
import br.ime.usp.ws.traveler.Flight;
import br.ime.usp.ws.traveler.TravelerWS;
import br.ime.usp.ws.traveler.TravelerWSService;

public class ReserveEticketFlowTest {

	private static TravelerWSService travelerService;
	private static TravelerWS travelerStub;
	private static Flight flight;
	private static String reserveInChore;
	private static RESTClient travelAgencyclient;
	
	final static String DESTINATION = "London";
	final static String NAME = "Saywer";
	final static String DATE = "12-23-2010";
	final static String CREDIT_CARD_NUMBER = "789653-2";

	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		travelAgencyclient = new RESTClient();
		travelAgencyclient.setBaseURL("http://localhost:9881/travelagency");
		
		flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		reserveInChore = travelerStub.reserveTicket(flight.getId());
	}
	
	@AfterClass
	public static void tearDown(){
		Bash.cleanAllDataBases();
	}
	
	@Test
	public void airlineWSShouldReturnAReserveForAuthorizedTravelAgencies(){
		String authorizedTA = travelAgencyclient.GET("/name");
		
		AirlineWSService airlineService = new AirlineWSService();
		AirlineWS airlineStub = airlineService.getAirlineWSPort();
		
		String reserveOutChore = airlineStub.reserveTicket(flight.getId(), authorizedTA);
		
		assertTrue(reserveOutChore.contains(reserveInChore));
	}
	
	@Test
	public void travelAgencyShouldStoreReserveAsAResource(){
		String reserveId = reserveInChore.toLowerCase();
		
		String resourceStored = travelAgencyclient.GET("/reserves/"+reserveId);
		
		assertFalse(resourceStored.isEmpty());
	}
	
	@Test
	public void travelAgencyShouldStoreReserveCorrectly(){
		String reserveId = reserveInChore.toLowerCase();
		
		String resourceStored = travelAgencyclient.GET("/reserves/"+reserveId);
		String resourceExpected = reserveId + "|" + 
								  NAME + "|" + 
								  (Integer.parseInt(flight.getPrice()) + 100);
		
		assertEquals(resourceExpected, resourceStored);
	}
	
}
