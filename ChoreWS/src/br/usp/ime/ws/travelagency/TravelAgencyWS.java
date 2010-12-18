package br.usp.ime.ws.travelagency;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/travelagency")
public class TravelAgencyWS {
	
	private DBTravelAgencyConnection dbCon;
	
	public TravelAgencyWS() throws SQLException, ClassNotFoundException, IOException{
		
		File dataBase = new File("travelagency.db");
		
		if(!dataBase.exists()){ 
			dbCon = new DBTravelAgencyConnection("travelagency.db");
			dbCon.initDB();
		}
		
		else
			dbCon = new DBTravelAgencyConnection("travelagency.db");
	}
	
	@GET
	@Path("/users/{id}")
	public String getUser(@PathParam("id") String id){
		return dbCon.getUser(id);
	}
	
	@GET
	@Path("/reserves/{id}")
	public String getReserve(@PathParam("id") String id){
		return dbCon.getReserve(id);
	}
	
	@GET
	@Path("/users")
	public String getUserByName(@QueryParam("name") String name){
		return dbCon.getCCnumberByName(name);
	}
	
	@GET
	@Path("/name")
	public String getUser(){
		return "United Airlines";
	}	
	
	@POST
	@Path("/users")
	@Consumes("text/plain")
	public Response addUsers(String userData) {
		String id = dbCon.addUser(userData); 
		return Response.created(URI.create("/" + id)).build();
	}
	
	@POST
	@Path("/reserves")
	@Consumes("text/plain")
	public Response addReserves(String reserveData) {
		String id = dbCon.addReserve(reserveData);
		return Response.created(URI.create("/" + id)).build();
	}
	

}
