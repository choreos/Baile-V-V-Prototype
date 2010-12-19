package br.usp.ime.test.unit;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ime.usp.restclient.RESTClient;
import br.usp.ime.test.utils.Bash;

public class AcquirerWSTest {
	
	final static String BASE_URL = "http://localhost:9883/acquirer";
	static RESTClient client;

	@BeforeClass
	public static void publishAcquirerService(){
		Bash.deployService("acquirer");
		client = new RESTClient();
		client.setBaseURL(BASE_URL);
		Bash.cleanAcquirerDatabase();
	}
	
	@AfterClass
	public static void killTravelAgencyService() {
		Bash.undeployService("acquirer");
	}
	
	@After
	public void tearDown(){
		Bash.cleanAcquirerDatabase();
	}
	
	@Test
	public void shouldAddNewAccount(){
		String body = "1234|Jacob|1000";
		String response = client.POST("/account", body);
		
		assertEquals("http://localhost:9883/acquirer/account/1234", response);
	}
	
	@Test
	public void shouldAddAndRetrieveTheAccount(){
		String body = "1234|Jacobs|1000";
		client.POST("/account", body);
		String response = client.GET("/account/1234");
		
		assertEquals("1234|Jacob|1000", response);
	}
	
	@Test
	public void shouldApproveCreditCard(){
		String body = "1234|Jacob|1000";
		client.POST("/account", body);
		
		String response = client.GET("/check?number=1234");
		
		assertEquals("Credit card approved", response);
	}
	
	@Test
	public void shouldNotApproveCreditCard(){
		String response = client.GET("/check?number=XXXXXX");
		
		assertEquals("Credit card not approved", response);
	}
	
	
	@Test
	public void shouldDiscountValueCorrectly(){
		String body = "1234|Jacob|1000";
		client.POST("/account", body);
		
		body = "1234|100";
		client.PUT("/discount", body);
		
		String response = client.GET("/account/1234"); 
		
		assertEquals("1234|Jacob|900", response);
	}
	
	@Test
	public void shouldNotDiscountValue(){
		String body = "1234|Jacob|100";
		client.POST("/account", body);
		
		body = "1234|200";
		client.PUT("/discount", body);
		
		String response = client.GET("/account/1234"); 
		
		assertEquals("1234|Jacob|100", response);
	}
}	

