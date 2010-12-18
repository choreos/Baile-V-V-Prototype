package br.usp.ime.restclient;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.usp.ime.restclient.EmptyBaseURLException;
import br.usp.ime.restclient.RESTClient;

public class RESTClientOperationsTest {
	
	private static RESTClient client;
	
	
	@Before
	public void setUp(){
		client = new RESTClient();
	}
		
	@Test
	public void shouldSetBaseURLCorrectly(){
		client.setBaseURL("http://localhost:8080/travelangency");
		
		assertEquals("http://localhost:8080/travelangency", client.getBaseURL());
	}
	
	@Test
	public void shouldCompleteURLWithBarCorrectly() throws EmptyBaseURLException{
		client.setBaseURL("http://localhost:8080/travelangency");
		String newURL = client.completeBaseURL("/orders");
		assertEquals("http://localhost:8080/travelangency/orders", newURL);
		
	}
	
	
	@Test
	public void shouldCompleteURLWithTwoBarsCorrectly() throws EmptyBaseURLException{
		client.setBaseURL("http://localhost:8080/travelangency");
		String newURL = client.completeBaseURL("/orders/1");
		assertEquals("http://localhost:8080/travelangency/orders/1", newURL);	
	}
	
	
	@Test (expected = EmptyBaseURLException.class)
	public void shouldNotCompleteEmptyURL() throws EmptyBaseURLException{
		client.completeBaseURL("/orders");
	}

}
