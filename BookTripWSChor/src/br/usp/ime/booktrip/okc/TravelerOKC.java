package br.usp.ime.booktrip.okc;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import org.openk.core.OKC.impl.OKCFacadeImpl;
import org.openk.core.module.interpreter.Argument;

import br.usp.ime.booktrip.utils.MessageTrace;
import br.usp.ime.booktrip.utils.MessageTraceQueue;
import br.usp.ime.booktrip.utils.OKMessagesQueue;
import br.usp.ime.ws.traveler.Request;
import br.usp.ime.ws.traveler.TravelerWS;
import br.usp.ime.ws.traveler.TravelerWSService;

public class TravelerOKC extends OKCFacadeImpl {
	private TravelerWSService service;
	private TravelerWS stub;
	private ArrayList<String> orders = new ArrayList<String>();
	private BlockingQueue<String> flights = new LinkedBlockingQueue<String>();
	private ArrayList<String> reserves = new ArrayList<String>();
	private ArrayList<String> books = new ArrayList<String>();
	private ArrayList<String> cancellations = new ArrayList<String>();
	private OKMessagesQueue okQueue;
	
	public TravelerOKC() {
		service = new TravelerWSService();
		stub = service.getTravelerWSPort();
		okQueue = new OKMessagesQueue();
		

	}

	private void requestCollector() {
		Request incommingRequest = getRequest();

		if (incommingRequest.getOperation().equals("order"))
			orders.add(incommingRequest.getMessage());
		
		if (incommingRequest.getOperation().equals("flights"))
			flights.add(incommingRequest.getMessage());	

		if (incommingRequest.getOperation().equals("reserve"))
			reserves.add(incommingRequest.getMessage());

		if (incommingRequest.getOperation().equals("book"))
			books.add(incommingRequest.getMessage());

		if (incommingRequest.getOperation().equals("cancel"))
			cancellations.add(incommingRequest.getMessage());

	}

	public boolean userInitiatesOrderTrip(Argument Destination, Argument Date,
			Argument Name, Argument CcNumber) {
		requestCollector();

		if (orders.isEmpty())
			return false;

		String order = orders.remove(0);

		StringTokenizer data = new StringTokenizer(order, "|");

		Destination.setValue(data.nextToken());
		Date.setValue(data.nextToken());
		Name.setValue(data.nextToken());
		CcNumber.setValue(data.nextToken());

		return true;
	}

	public boolean userMakesAReservation(Argument Flights) {
		if (reserves.isEmpty())
			return false;

		String reserve = reserves.remove(0);
		Flights.setValue(reserve);

		return true;
	}

	public boolean userMakesACancellation(Argument Reserve) {

		if (cancellations.isEmpty())
			return false;

		String cancellation = cancellations.remove(0);
		Reserve.setValue(cancellation);

		return true;
	}

	public boolean userMakesABook(Argument Reserve) {
		if (books.isEmpty())
			return false;

		String book = books.remove(0);
		Reserve.setValue(book);

		return true;
	}

	public boolean flights(Argument FlightStatus, Argument FlightID,
			Argument FlightPrice, Argument AirlineName) throws RemoteException {
		if (flights.isEmpty())
			return false;
				
		flights.remove(0);
		
		setResponse(FlightStatus.getValue().toString() + "|"
				+ FlightID.getValue().toString() + "|"
				+ FlightPrice.getValue().toString() + "|"
				+ AirlineName.getValue().toString());
		

		return true;
	}

	public boolean reserve(Argument Reserve) throws RemoteException {
		setResponse(Reserve.getValue().toString());
		return true;
	}

	public boolean cancellation(Argument Confirmation) throws RemoteException {
		setResponse(Confirmation.getValue().toString());
		return true;
	}

	public boolean statement(Argument Statement) throws RemoteException {

		MessageTrace message = new MessageTrace("travelAgency", "traveler",
				"statement", Statement.getValue().toString());

		MessageTraceQueue queue = new MessageTraceQueue();
		queue.add(message);

		setResponse(Statement.getValue().toString());
		return true;
	}

	public boolean eTicket(Argument Eticket) throws RemoteException {
		String content = Eticket.getValue().toString();

			addMessageTraceQueue("airline", "traveler", "responseEticket",
					content);
			setResponse(content);

		
		return true;
	}

	public Request getRequest() {

		Request incommingMessage = null;

		while (true) {
			try {
				Thread.sleep(2000);
				incommingMessage = stub.getInData();
				if (incommingMessage.getMessage() != null)
					break;
				
				incommingMessage = okQueue.getInData();
				if (incommingMessage.getMessage() != null)
					break;
			}

			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return incommingMessage;
	}

	public void setResponse(String content) throws RemoteException {
		stub.addOut(content);
	}

	private void addMessageTraceQueue(String emissor, String receptor,
			String name, String content) {
		MessageTrace message = new MessageTrace(emissor, receptor, name,
				content);

		MessageTraceQueue queue = new MessageTraceQueue();
		queue.add(message);
	}

}
