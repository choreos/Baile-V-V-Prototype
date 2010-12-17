	package br.usp.ime.booktrip.okc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.openk.core.OKC.impl.OKCFacadeImpl;
import org.openk.core.module.interpreter.Argument;

import br.ime.usp.ws.traveler.Request;
import br.ime.usp.ws.traveler.TravelerWS;
import br.ime.usp.ws.traveler.TravelerWSService;
import br.usp.ime.booktrip.utils.MessageTrace;
import br.usp.ime.booktrip.utils.MessageTraceQueue;



public class TravelerOKC extends OKCFacadeImpl
{
	TravelerWSService service;
	TravelerWS stub;
	ArrayList<String> orders = new ArrayList<String>();
	ArrayList<String> reserves = new ArrayList<String>();
	ArrayList<String> books = new ArrayList<String>();
	ArrayList<String> cancellations = new ArrayList<String>();
	
	public TravelerOKC(){
		 service = new TravelerWSService();
		 stub = service.getTravelerWSPort();
	}
	
	private void requestCollector(){
		Request incommingRequest = getRequest();
		
		if(incommingRequest.getOperation().equals("order"))
			orders.add(incommingRequest.getMessage());
		
		if(incommingRequest.getOperation().equals("reserve"))
			reserves.add(incommingRequest.getMessage());
		
		if(incommingRequest.getOperation().equals("book"))
			books.add(incommingRequest.getMessage());
		
		if(incommingRequest.getOperation().equals("cancel"))
			cancellations.add(incommingRequest.getMessage());

	}
	

	public boolean userInitiatesOrderTrip(Argument Destination, Argument Date, Argument Name, Argument CcNumber)
	{
		requestCollector();
		
		if(orders.isEmpty())
			return false;
			
		String order = orders.remove(0);
		
		StringTokenizer data = new StringTokenizer(order,"|");
			
		Destination.setValue(data.nextToken());
		Date.setValue(data.nextToken());
		Name.setValue(data.nextToken());
		CcNumber.setValue(data.nextToken());
				
		return true;
	}
	
	
	public boolean userMakesAReservation(Argument Flights)
    {
		if(reserves.isEmpty())
			return false;
		
		String reserve = reserves.remove(0);
        Flights.setValue(reserve);
   
        return true; 
    }
	
	public boolean userMakesACancellation(Argument Reserve){
		
		if(cancellations.isEmpty())
			return false;
		
		String cancellation = cancellations.remove(0);
		Reserve.setValue(cancellation);
		
		return true;
	}
	
	public boolean userMakesABook(Argument Reserve)
	{
		if(books.isEmpty())
			return false;
		
		String book = books.remove(0);
		Reserve.setValue(book);
		
		return true;
	}

	public boolean flights(Argument FlightStatus, Argument FlightID, Argument FlightPrice, Argument AirlineName) throws RemoteException
	{
		setResponse(FlightStatus.getValue().toString() + "|" + FlightID.getValue().toString() + "|" + FlightPrice.getValue().toString() + "|" + AirlineName.getValue().toString());
		return true;
	}

	public boolean reserve(Argument Reserve) throws RemoteException
	{
		setResponse(Reserve.getValue().toString());
		return true;
	}
	
	public boolean cancellation(Argument Confirmation) throws RemoteException
	{
		setResponse(Confirmation.getValue().toString());
		return true;
	}

	public boolean statement(Argument Statement) throws RemoteException
	{

		MessageTrace message = new MessageTrace("travelAgency", "traveler",
				"statement", Statement.getValue().toString());

		MessageTraceQueue queue = new MessageTraceQueue();
		queue.add(message);
		
		setResponse(Statement.getValue().toString());
		return true;
	}

	public boolean eTicket(Argument Eticket) throws RemoteException
	{
		setResponse(Eticket.getValue().toString());
		return true;
	}
	
	public Request getRequest(){ 
		
		Request incommingMessage = null;		

		while(true){
			try {
				Thread.sleep(2000);
				incommingMessage = stub.getInData();
				if(incommingMessage.getMessage() != null)
					break;
			}

			catch (InterruptedException e) { e.printStackTrace(); }
		}
		return incommingMessage;
	}
	
	public void setResponse(String content) throws RemoteException{		
		stub.addOut(content);
	}
	
}
