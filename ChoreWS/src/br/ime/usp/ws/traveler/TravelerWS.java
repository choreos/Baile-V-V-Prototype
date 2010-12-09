package br.ime.usp.ws.traveler;

import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public class TravelerWS {
	private DBMessagesConnection dbCon;

	
	public TravelerWS() throws SQLException, ClassNotFoundException {
			dbCon = new DBMessagesConnection("messagesqueue.db");
			dbCon.initDB();
	}
	
	
	@WebMethod
	public Flight orderTrip(String destination, String date, String name, String ccNumber){
		String trip = destination +  "|" + 
					  		  date + "|" +
					          name + "|" +
					          ccNumber;
		addIn(trip, "order");
		
		String result = getOutData();
		
		return this.parseFlightsResponse(result);
	}
	
	
	@WebMethod
	public String reserveTicket(String flight){
		addIn(flight, "reserve");
		
		return getOutData();
	}	
	
	
	@WebMethod
	public String cancelReserve(String reserve){
		addIn(reserve, "cancel");
		
		return getOutData();
	}
	
	
	@WebMethod
	public String[] bookReserve(String reserve){
		String out[] = new String[2];
		addIn(reserve, "book");
		
		out[0] = getOutData();
		out[1] = getOutData();
		
		return out;
	}
	
	
	private String getOutData(){
		while(true){
			try {
				Thread.sleep(1000);
				String message = getOut();
				if(message != null)
					return message;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
	}
	
	private void addIn(String string, String operation) {
		dbCon.addMessageIn(string, operation);
	}
	
	private Flight parseFlightsResponse(String response){
		StringTokenizer parser = new StringTokenizer(response, "|");
		Flight flight = new Flight();
		flight.setStatus(parser.nextToken());
		flight.setId(parser.nextToken());
		flight.setPrice(parser.nextToken());
		flight.setAirline(parser.nextToken());
		
		return flight;
	}

	@WebMethod
	public Request getInData() {
		return dbCon.getMessageIn();
	}
	
	
	@WebMethod
	public void addOut(String string) {
		dbCon.addMessageOut(string);
	}

	private String getOut() {
		return dbCon.getMessageOut();
	}
	
	
}