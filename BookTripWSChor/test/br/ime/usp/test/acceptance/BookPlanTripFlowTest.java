package br.ime.usp.test.acceptance;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.ime.usp.test.utils.Bash;
import br.ime.usp.ws.traveler.Flight;
import br.ime.usp.ws.traveler.OrderTrip;
import br.ime.usp.ws.traveler.TravelerWS;
import br.ime.usp.ws.traveler.TravelerWSService;

public class BookPlanTripFlowTest {
	
	private TravelerWSService service;
	private TravelerWS stub;
	static OrderTrip order;
	
	@Before
	public void setUp(){
		service = new TravelerWSService();
		stub = service.getTravelerWSPort();
		Bash.cleanAllDataBases();
	}
	
	@Test
	public void shouldOrderTrip() throws RemoteException{
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		
		assertEquals("available",flight.getStatus());
		assertEquals("2142", flight.getId());
		assertEquals("2000", flight.getPrice());
		assertEquals("United Airlines", flight.getAirline());
	}
	
	@Test
	public void shouldReserve() throws RemoteException{
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
		
		assertEquals("R2142-1", reserve);
	}
	
	
	@Test
	public void shouldBookAndReceiveAnStatement() throws RemoteException{	
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
				
		List<String> bookResponse = stub.bookReserve(reserve);
		
		String statement = "Name: John Locke" + "\n" +
		   "Credit card: 435067869" + "\n" +
		   "Value discounted: $2100";
			
		assertTrue(bookResponse.contains(statement));
		
	}
	

	@Test
	public void shouldBookAndReceiveAnEticket() throws RemoteException{	
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
				
		List<String> bookResponse = stub.bookReserve(reserve);
		
		String eTicket =   "e-ticket for flight "+ flight.getId() + "\n" + 
		
		   "passenger: John Locke";
			
		assertTrue(bookResponse.contains(eTicket));
		
	}
	
	
	@Test
	public void shouldCancelReserve() throws RemoteException{	
		Flight flight = stub.orderTrip("Paris", "12-20-2010", "John Locke", "435067869");
		String reserve = stub.reserveTicket(flight.getId());
		
		String cancellation = stub.cancelReserve(reserve);
		assertEquals("R2142-1 cancelled", cancellation);
		
	}

	
}
