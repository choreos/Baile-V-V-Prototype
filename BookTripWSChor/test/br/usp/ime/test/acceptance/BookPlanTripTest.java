package br.usp.ime.test.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.usp.ime.test.utils.Bash;
import br.usp.ime.ws.traveler.Flight;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class BookPlanTripTest {
	
	private static TravelerWSService service;
	private static TravelerWS stub;

	@BeforeClass
	public static void startService(){
		service = new TravelerWSService();
		stub = service.getTravelerWSPort();
	}
	
	@Before
	public void setUp(){
		Bash.cleanAllDataBases();
	}
	

	@Test
	public void shouldBookAndPlanTrip() throws RemoteException{
		
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
		List<String> response = stub.bookReserve(reserve);
		
		String statement = "Name: John Locke" + "\n" +
		   "Credit card: 435067869" + "\n" +
		   "Value discounted: $2100";
		
		String eTicket =   "e-ticket for flight "+ flight.getId() + "\n" + 
		   "passenger: John Locke";
		
		assertTrue(response.contains(eTicket));
		assertTrue(response.contains(statement));

	}
	
	@Test
	public void shouldNotApprovedCreditCard() throws RemoteException{		
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "XXXXXX");
		String reserve = stub.reserveTicket(flight.getId());
		List<String> response = stub.bookReserve(reserve);
		
		String statement = "Credit card not approved";
		String eTicket = "Purchased cancelled";
		
		assertTrue(response.contains(eTicket));
		assertTrue(response.contains(statement));
	}
	
	
	
	
	@Test
	public void shouldNotFoundAFlight() throws RemoteException{		
		Flight flight = stub.orderTrip("Lisbon", "12-20-2010", "John Locke", "435067869");
		assertEquals("not found", flight.getStatus());
	}
	
	
	@Test
	public void shouldCancelReservation() throws RemoteException{
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
		String cancelConfirmation = stub.cancelReserve(reserve);
		
		assertEquals(reserve + " cancelled", cancelConfirmation);	
	}
}
