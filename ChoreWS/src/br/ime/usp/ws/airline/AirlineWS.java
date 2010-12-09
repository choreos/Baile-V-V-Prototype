package br.ime.usp.ws.airline;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class AirlineWS {
	private DBAirlineConnection dbCon;
	
	public AirlineWS() throws SQLException, ClassNotFoundException{
		dbCon = new DBAirlineConnection("airline.db");
		dbCon.initDB();
		loadDB();
	}
	
	private void loadDB(){
		FlightResult f1 = new FlightResult("2142","Paris", "12-20-2010", "17:45");
		FlightResult f2 = new FlightResult("3153","Milan", "12-21-2010", "09:15"); 
		FlightResult f3 = new FlightResult("4164","Brussels", "12-22-2010", "13:20"); 
		FlightResult f4 = new FlightResult("5175","London", "12-23-2010", "05:20");
		FlightResult f5 = new FlightResult("6186","Vienna", "12-25-2010", "22:30"); 
		
		dbCon.addFlight(f1);
		dbCon.addFlight(f2);
		dbCon.addFlight(f3);
		dbCon.addFlight(f4);
		dbCon.addFlight(f5);
	
		String tA1 = "United Airlines";
		dbCon.addTravelAgency(tA1);
	}
	
	@WebMethod
	public FlightResult getFlight(String destination, String date){
		FlightResult flightFound = dbCon.getFlight(destination, date);
		return flightFound;
	}
	
	@WebMethod
	public String reserveTicket(String flightID, String travelAgencyName ){
		if(!isTravelAgencyAuthorized(travelAgencyName))
			return travelAgencyName + " is not authorized";
		
		String reserve = "R" + flightID + "-1|2000";
		
		return reserve;
	}
	
	@WebMethod
	public String emitEticket(String reserve, String passengerName ){
		String flight = reserve.substring(1, 5);
		String eTicket = "e-ticket for flight " + flight + "\n" + 
		 "passenger: " + passengerName;
		
		return eTicket;
	}
	
	@WebMethod
	public boolean isTravelAgencyAuthorized(String travelAgency){
		ArrayList<String> travelAgencies = dbCon.getListOfTravelAgencies();
		
		return travelAgencies.contains(travelAgency);		
	}
	
	

}
