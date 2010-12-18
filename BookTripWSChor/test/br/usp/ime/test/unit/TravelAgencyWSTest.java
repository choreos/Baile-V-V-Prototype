package br.usp.ime.test.unit;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.restclient.EmptyBaseURLException;
import br.ime.usp.restclient.RESTClient;
import br.usp.ime.test.utils.Bash;

public class TravelAgencyWSTest {
	
	final static String BASE_URL = "http://localhost:9881/travelagency";
	private static RESTClient client;
	

	@BeforeClass
	public static void publishTravelAgencyService() {
		Bash.deployService("travelagency");
		client = new RESTClient();
		client.setBaseURL(BASE_URL);
		Bash.cleanTravelAgencyDatabase();
	}
	
	@AfterClass
	public static void unpublishTravelAgencyService() {
		Bash.undeployService("travelagency");
	}
	
	@After
	public void tearDown(){
		Bash.cleanTravelAgencyDatabase();
	}
	
	@Test
	public void shouldCreateUser() {
		String body = "John Locke|421543-2";
		String resourceLocation = client.POST("/users", body);
		
		assertEquals("http://localhost:9881/travelagency/users/1", resourceLocation);
	}
	
	@Test
	public void shouldRetrieveUserCreated() {
		String body = "John Locke|421543-2";
		String resourceLocation = client.POST("/users", body);
		String resourcePart = resourceLocation.substring(BASE_URL.length(), resourceLocation.length());
		String response = client.GET(resourcePart);
		
		assertEquals("John Locke|421543-2", response);
	}

	
	@Test
	public void shouldRetrieveCCNumberByName() throws EmptyBaseURLException {
		String body = "John Locke|421543-2";
		client.POST("/users", body);
		String response = client.GET("/users?name=John%20Locke");
		
		assertEquals("421543-2", response);
	}
	
	@Test
	public void shouldRetrieveTravelAgencyName() throws EmptyBaseURLException {
		String name = client.GET("/name");
		
		assertEquals("United Airlines", name);
	}
	
	@Test
	public void shouldStoreReserve() throws EmptyBaseURLException {
		String body = "RO815|John%20Locke|$2100";
		String response = client.POST("/reserves", body);
		
		assertEquals("http://localhost:9881/travelagency/reserves/ro815", response);
	}
	
	
	@Test
	public void shouldRetrieveReserve() throws EmptyBaseURLException {
		String body = "RO815|John%20Locke|$2100";
		client.POST("/reserves", body);
		
		String response = client.GET("/reserves/ro815");
		assertEquals("ro815|John Locke|$2100", response);
	}
	
}
