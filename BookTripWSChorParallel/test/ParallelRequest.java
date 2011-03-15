import br.usp.ime.ws.traveler.Flight;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;


public class ParallelRequest implements Runnable  {

	private String destination;
	
	public ParallelRequest(String destination) {
		this.destination = destination;
	}
	
	public void run() {

		TravelerWSService service = new TravelerWSService();
		TravelerWS stub = service.getTravelerWSPort();
		
		Flight flight = stub.orderTrip(destination, "12-20-2010", "John Locke", "435067869");
		System.out.println(flight.getId());
	}

}
