package br.usp.ime.ws;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

import br.usp.ime.ws.airline.AirlineWS;
import br.usp.ime.ws.traveler.TravelerWS;


public class ChoreWS {
	
	public void publishTravelerWS() throws SQLException, ClassNotFoundException{
		TravelerWS service = new TravelerWS();
		Endpoint endpoint = Endpoint.create(service);
		endpoint.publish("http://localhost:9880/traveler");
	}
	
	public void publishTravelAgencyWS() throws SQLException, ClassNotFoundException, 
	IllegalArgumentException, IOException{
		 final String baseUri = "http://localhost:9881/";
		 final Map<String, String> initParams = new HashMap<String, String>();
		 
		 initParams.put("com.sun.jersey.config.property.packages", 
		 "br.usp.ime.ws.travelagency");

		 System.out.println("Starting grizzly...");
		 GrizzlyWebContainerFactory.create(baseUri, initParams);
	}

	public void publishAirlineWS() throws SQLException, ClassNotFoundException{
		AirlineWS service = new AirlineWS();
		Endpoint endpoint = Endpoint.create(service);
		endpoint.publish("http://localhost:9882/airline");
	}
	
	private void publishAcquirerWS() throws IllegalArgumentException, IOException {
		final String baseUri = "http://localhost:9883/";
		 final Map<String, String> initParams = new HashMap<String, String>();
		 
		 initParams.put("com.sun.jersey.config.property.packages", 
		 "br.usp.ime.ws.acquirer");

		 System.out.println("Starting grizzly...");
		 GrizzlyWebContainerFactory.create(baseUri, initParams);		
	}
	
	public void publishAll() throws SQLException, ClassNotFoundException, IllegalArgumentException, IOException{
		publishTravelerWS();
		publishTravelAgencyWS();
		publishAirlineWS();
		publishAcquirerWS();
	}

	
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalArgumentException, IOException {
		ChoreWS services = new ChoreWS();
		
		String option = args[1];
		
		if(option.equals("traveler"))
			services.publishTravelerWS();
		else if(option.equals("airline"))
			services.publishAirlineWS();
		else if(option.equals("travelagency"))
			services.publishTravelAgencyWS();
		else if(option.equals("acquirer"))
			services.publishAcquirerWS();
		else if(option.equals("all"))
			services.publishAll();
		else
			throw new IllegalArgumentException("invalid option");
	}


}
