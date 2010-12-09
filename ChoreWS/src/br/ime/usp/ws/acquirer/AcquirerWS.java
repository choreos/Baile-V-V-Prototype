package br.ime.usp.ws.acquirer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Path("/acquirer")
public class AcquirerWS {
	
	private DBAcquirerConnection dbCon;
	
	public AcquirerWS() throws SQLException, ClassNotFoundException, IOException{
		
		File dataBase = new File("acquirer.db");
		
		if(!dataBase.exists()){ 
			dbCon = new DBAcquirerConnection("acquirer.db");
			dbCon.initDB();
		}
		
		else
			dbCon = new DBAcquirerConnection("acquirer.db");
	}
	
	@GET
	@Path("/account/{id}")
	public String getAccount(@PathParam("id") String id){
		return dbCon.getAccount(id);
	}
	
	@GET
	@Path("/check")
	public String getReserve(@QueryParam("number") String number){
		return dbCon.checkCreditCard(number);
	}
	
	
	@POST
	@Path("/account")
	@Consumes("text/plain")
	public Response createAccount(String userData) {
		String id = dbCon.createAccount(userData); 
		return Response.created(URI.create("/" + id)).build();
	}
	
	@PUT
	@Path("/account")
	@Consumes("text/plain")
	public Response updateAccount(String data) {
		StringTokenizer parser = new StringTokenizer(data,"|");
		String number = parser.nextToken();
		String name = parser.nextToken();
		String value = parser.nextToken();
		String id = dbCon.updateAccount(number, name, value);
		return Response.created(URI.create("/" + id)).build();
	}
	
	@PUT
	@Path("/discount")
	@Consumes("text/plain")
	public Response discountCredit(String data) {
		StringTokenizer parser = new StringTokenizer(data,"|");
		String number = parser.nextToken();
		String value = parser.nextToken();
		String id = dbCon.discountValue(number, value);
		return Response.created(URI.create("/" + id)).build();
	}
	

}
