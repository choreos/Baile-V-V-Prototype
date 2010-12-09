	package br.usp.ime.booktrip.okc;

import org.openk.core.OKC.impl.OKCFacadeImpl;
import org.openk.core.module.interpreter.Argument;

import br.ime.usp.restclient.RESTClient;



public class AcquirerOKC extends OKCFacadeImpl
{
	RESTClient client;
	String name;
	
	public AcquirerOKC(){
		client = new RESTClient();
		client.setBaseURL("http://localhost:9883/acquirer");
	}

	public boolean countPayments(Argument Name, Argument CcNumber, Argument TotalPrice, Argument Status, Argument TravelAgencyPayment, Argument AirlinePayment){
		String body = CcNumber.getValue().toString() + "|" + Name.getValue().toString() + "|" + 10000;
		body = body.replace(" ", "%20");
		client.PUT("/account", body);
		
		String status = client.GET("/check?number="+ CcNumber.getValue().toString());
		Status.setValue(status);

		if(status.equals("Credit card not approved"))
			return false;

		body = CcNumber.getValue().toString() + "|" + TotalPrice.getValue().toString();


		client.PUT("/discount", body);
		
		double totalPrice = Integer.parseInt(TotalPrice.getValue().toString());

		double travelAgencyPayment = 100;
		double airlinePayment = totalPrice - travelAgencyPayment;
		TravelAgencyPayment.setValue(travelAgencyPayment);
		AirlinePayment.setValue(airlinePayment);
		
		return true;
	}
	
	
	public boolean verifyStatus(Argument Status){

		if(Status.getValue() != null)
				return true;
		return false;
	}
		
	}
	
