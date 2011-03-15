import br.usp.ime.ws.airline.AirlineWS;
import br.usp.ime.ws.airline.AirlineWSService;
import br.usp.ime.ws.airline.FlightResult;


public class ParallelRequestAirline implements Runnable {

	
	private String destination;

	public ParallelRequestAirline(String string) {
		this.destination = string;
	}

	@Override
	public void run() {
		AirlineWSService service = new AirlineWSService();
		AirlineWS stub = service.getAirlineWSPort();
		
		String date = "12-21-2010";
		
		FlightResult flight = stub.getFlight(destination, date);
		
		System.out.println(flight.getId());
		
	}

}
