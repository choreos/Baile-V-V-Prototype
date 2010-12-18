package br.usp.ime.test.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.restclient.RESTClient;
import br.usp.ime.test.utils.Bash;
import br.usp.ime.ws.airline.AirlineWS;
import br.usp.ime.ws.airline.AirlineWSService;
import br.usp.ime.ws.traveler.Flight;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class BookEticketFlowTest {

	private static TravelerWSService travelerService;
	private static TravelerWS travelerStub;
	private static AirlineWSService airlineService;
	private static AirlineWS airlineStub;
	private static Flight flight;
	private static String reserveInChore;
	private static RESTClient travelAgencyClient;
	private static RESTClient acquirerClient;
	private static List<String> response;
	
	final static String DESTINATION = "London";
	final static String NAME = "Saywer";
	final static String DATE = "12-23-2010";
	final static String CREDIT_CARD_NUMBER = "789653-2";

	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		airlineService = new AirlineWSService();
		airlineStub = airlineService.getAirlineWSPort();
		
		travelAgencyClient = new RESTClient();
		acquirerClient = new RESTClient();
		
		travelAgencyClient.setBaseURL("http://localhost:9881/travelagency");
		acquirerClient.setBaseURL("http://localhost:9883/acquirer");		
		
		flight = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
		reserveInChore = travelerStub.reserveTicket(flight.getId());
		response = travelerStub.bookReserve(reserveInChore);
	}
	
	@AfterClass
	public static void tearDown(){
		Bash.cleanAllDataBases();
	}
	
	@Test
	public void acquirerShouldHadApprovedThisCreditCard(){
		String response = acquirerClient.GET("/check?number="+CREDIT_CARD_NUMBER);
		assertEquals("Credit card approved", response);
	}
	
	@Test
	public void airlineShouldReturnTheSameEticket(){
		String actualEticket = airlineStub.emitEticket(reserveInChore, NAME);
		
		assertTrue(response.contains(actualEticket));
	}
	
	@Test
	public void travelAgencyShouldReturnTheSameStatement(){
		String reserveCode = reserveInChore.toLowerCase();
		
		String response = travelAgencyClient.GET("/reserve/" + reserveCode );
		
		assertTrue(response.contains(response));
	}
	
}
