package br.usp.ime.test.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.restclient.RESTClient;
import br.usp.ime.test.utils.Bash;
import br.usp.ime.ws.airline.AirlineWS;
import br.usp.ime.ws.airline.AirlineWSService;
import br.usp.ime.ws.airline.FlightResult;
import br.usp.ime.ws.traveler.Flight;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class OrderTripFlowTest {
	
	private static TravelerWSService travelerService;
	private static TravelerWS travelerStub;
	private static Flight flightInChore;
	
	final static String DESTINATION = "London";
	final static String NAME = "Saywer";
	final static String DATE = "12-23-2010";
	final static String CREDIT_CARD_NUMBER = "789653-2";

	@BeforeClass
	public static void setUp(){
		Bash.cleanAllDataBases();
		travelerService = new TravelerWSService();
		travelerStub = travelerService.getTravelerWSPort();
		flightInChore = travelerStub.orderTrip(DESTINATION, DATE, NAME, CREDIT_CARD_NUMBER);
	}
	
	@AfterClass
	public static void tearDown(){
		Bash.cleanAllDataBases();
	}
	
	
	@Test
	public void travelAgencyWSShouldSavePassengerInformation(){
		RESTClient client = new RESTClient();
		client.setBaseURL("http://localhost:9881/travelagency");
		String cc_number = client.GET("/users?name=" + NAME);
		
		assertEquals(CREDIT_CARD_NUMBER, cc_number);
	}
	
	@Test
	public void airlineShouldReturnTheSameFlightWhenInvokeByAnyUser(){
		AirlineWSService airlineService = new AirlineWSService();
		AirlineWS airlineStub = airlineService.getAirlineWSPort();
		FlightResult flightOutChore = airlineStub.getFlight(DESTINATION, DATE);
		
		assertEquals(flightInChore.getId(), flightOutChore.getId());
	}

}
