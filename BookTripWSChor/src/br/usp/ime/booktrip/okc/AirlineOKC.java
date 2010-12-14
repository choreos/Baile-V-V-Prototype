package br.usp.ime.booktrip.okc;

import java.rmi.RemoteException;

import org.openk.core.OKC.impl.OKCFacadeImpl;
import org.openk.core.module.interpreter.Argument;

import br.ime.usp.ws.airline.AirlineWS;
import br.ime.usp.ws.airline.AirlineWSService;
import br.ime.usp.ws.airline.FlightResult;
import br.usp.ime.booktrip.utils.MessageTrace;
import br.usp.ime.booktrip.utils.MessageTraceQueue;



public class AirlineOKC extends OKCFacadeImpl
{
	private AirlineWSService service;
	private AirlineWS stub;

	public boolean createResponse(Argument Destination, Argument Date, Argument FlightStatus, Argument FlightID, Argument FlightPrice, Argument AirlineName) throws RemoteException
	{		
		service = new AirlineWSService();
		stub = service.getAirlineWSPort();
		
		MessageTrace message = new MessageTrace("travelAgency", "airline", "search", Destination.getValue() + "|" + Date.getValue());

		MessageTraceQueue queue = new MessageTraceQueue();
		queue.add(message);
		
		String destination = Destination.getValue().toString();
		String date = Date.getValue().toString();
		
		FlightResult flightReturned = stub.getFlight(destination, date);
				
		if(flightReturned.getDestination() == null){
			FlightStatus.setValue("not found");
			FlightID.setValue(0);
			FlightPrice.setValue(0);
			AirlineName.setValue("empty");
		}
		else{
			FlightStatus.setValue("available");
			FlightID.setValue(flightReturned.getId());
			FlightPrice.setValue("2000");
			AirlineName.setValue("United Airlines");
		}
		return true;
	}

	public boolean createReserve(Argument FlightID, Argument TravelAgencyID, Argument ReserveCost) throws RemoteException{
				
		String flight = FlightID.getValue().toString();
		String tAId = TravelAgencyID.getValue().toString();
		
		String response = stub.reserveTicket(flight, tAId);
		
		ReserveCost.setValue(response);

		
		return true;	
	}
	
	public boolean cancelReserve(Argument Reserve, Argument Confirmation){
		String reserve = Reserve.getValue().toString();
		Confirmation.setValue(reserve + " cancelled" );
		
		return true;
	}

	public boolean createEticket(Argument Reserve,Argument Name,Argument CcNumber, Argument Eticket) throws RemoteException{

		String reserveId = Reserve.getValue().toString();
		String userName = Name.getValue().toString();

		String eTicket = stub.emitEticket(reserveId, userName);
		Eticket.setValue(eTicket);


		return true;
	}
	
	public boolean emitNotification(Argument Status, Argument Eticket){
		Eticket.setValue("Purchased cancelled");

		return true;
	}

}
