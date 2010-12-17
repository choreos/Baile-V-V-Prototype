package br.ime.usp.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.test.utils.Bash;
import br.ime.usp.ws.airline.AirlineWS;
import br.ime.usp.ws.airline.AirlineWSService;
import br.ime.usp.ws.airline.FlightResult;

public class AirlineWSTest {
	
	private AirlineWSService service;
	private AirlineWS stub;

	final String TA_NAME = "United Airlines";
	final String FLIGHT_ID = "3153";
	final String RESERVE = "R3153-1|2000";
	final String USER = "John Locke";
	
	@BeforeClass
	public static void publishAirlineService() {
		Bash.deployService("airline");
	}
	
	@AfterClass
	public static void unpublishAirlineService() {
		Bash.undeployService("airline");
	}
	
	@Before
	public void setUp(){
		service = new AirlineWSService();
		stub = service.getAirlineWSPort();
	}
	
	@Test
	public void shouldFindFlight() throws RemoteException{
		String destination = "Milan";
		String date = "12-21-2010";
		
		FlightResult flight = stub.getFlight(destination, date);
		
		assertEquals("3153", flight.getId());
		assertEquals("Milan", flight.getDestination());
		assertEquals("12-21-2010", flight.getDate());
		assertEquals("09:15", flight.getTime());
	}
	
	@Test
	public void shouldBeAnAuthorizedTravelAgency() throws RemoteException{
		assertTrue(stub.isTravelAgencyAuthorized(TA_NAME));
	}
	
	
	@Test
	public void shouldReturnAReserve() throws RemoteException{	
		String reserve = stub.reserveTicket(FLIGHT_ID, TA_NAME);
		
		assertEquals("R3153-1|2000", reserve);
	}
	
	@Test
	public void shouldReturnAnEticket() throws RemoteException{
		String eTicket =   "e-ticket for flight "+ FLIGHT_ID + "\n" + 
		   "passenger: John Locke";
		
		String response = stub.emitEticket(RESERVE, USER);
		
		assertEquals(eTicket, response);
	}
}
