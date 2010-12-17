package br.usp.ime.booktrip.okc;

import java.util.StringTokenizer;

import org.openk.core.OKC.impl.OKCFacadeImpl;
import org.openk.core.module.interpreter.Argument;

import br.ime.usp.restclient.RESTClient;
import br.usp.ime.booktrip.utils.MessageTrace;
import br.usp.ime.booktrip.utils.MessageTraceQueue;


public class TravelAgencyOKC extends OKCFacadeImpl
{
	RESTClient client;
	String name;
	MessageTraceQueue queue;

	public TravelAgencyOKC(){
		client = new RESTClient();
		client.setBaseURL("http://localhost:9881/travelagency");
		queue = new MessageTraceQueue();
	}

	public boolean registerUser(Argument Name, Argument CcNumber){
		name = Name.getValue().toString();
		String body = Name.getValue() + "|" + CcNumber.getValue();
		client.POST("/users", body);	
		return true;
	}
	
	public boolean searchResult(Argument FlightStatus, Argument FlightID, Argument FlightPrice, Argument AirlineName){
		String received = FlightStatus.getValue() + "|" + 
						  FlightID.getValue() + "|" + 
						  FlightPrice.getValue() + "|" +
						  AirlineName.getValue();
		addMessageTraceQueue("airline", "travelAgency", "response", received);
		return true;
	}


	public boolean travelAgencyMakesAReservation(Argument FlightID, Argument TravelAgencyID){
		String id = client.GET("/name");
		TravelAgencyID.setValue(id);

		return true;
	}
	
	
	public boolean travelAgencyRegisterReservation(Argument ReserveCost, Argument Reserve){
		StringTokenizer parser = new StringTokenizer(ReserveCost.getValue().toString(), "|");
		String id = parser.nextToken();
		int value = Integer.parseInt(parser.nextToken()) + 100;
		
		String body = id + "|" + name + "|" + value; 
		client.POST("/reserves", body);
		
		Reserve.setValue(id);
		
		addMessageTraceQueue("airline", "travelAgency", "response", ReserveCost.getValue());
		
		return true;
	}


	public boolean catchUserData(Argument Reserve, Argument Name, Argument CcNumber, Argument TotalPrice)
	{
		
		String resource = name.replace(" ", "%20");
		String ccNumber = client.GET("/users?name="+resource);

		Name.setValue(name);
		CcNumber.setValue(ccNumber);
		
		String reserveCode = Reserve.getValue().toString().toLowerCase();
		String response = client.GET("/reserves/" + reserveCode);
		
		StringTokenizer parser = new StringTokenizer(response, "|");
		
		parser.nextToken();
		parser.nextToken();
		
		TotalPrice.setValue(parser.nextToken());		
		return true;
	}
	
	public boolean emitStatement(Argument Reserve, Argument Name, Argument AirlinePayment, Argument Statement){
		String resource = name.replace(" ", "%20");
		String ccNumber = client.GET("/users?name="+resource);
		int value = Integer.parseInt(AirlinePayment.getValue().toString());
		
		value  = value + 100;
		
		String statement = "Name: " + name + "\n" +
		   "Credit card: " + ccNumber + "\n" +
		   "Value discounted: $" + value;
		Statement.setValue(statement);
		return true;
	}
	
	
	public boolean emitNotification(Argument Status, Argument Statement){

		String notification = Status.getValue().toString();
		Statement.setValue(notification);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMessageTraceQueue("acquire", "travelAgency", "notificationTA", notification);
		
		return true;
	}
	
	
	public boolean confirmCancellation(Argument Reserve){
		String info = Reserve.getValue().toString();
		
		Reserve.setValue(info);
		return true;
	}
	
	public boolean registerPayment(Argument Status, Argument TravelAgencyPayment){
		String payment = Status.getValue() + "|" + TravelAgencyPayment.getValue();

		addMessageTraceQueue("acquire", "travelAgency",
				"approval", payment);
	
		return true;
	}
	
	
	private void addMessageTraceQueue(String emissor, String receptor, String name, Object content) {
		MessageTrace message = new MessageTrace(emissor, receptor, name, content.toString());

		queue.add(message);		
	}
	
}
