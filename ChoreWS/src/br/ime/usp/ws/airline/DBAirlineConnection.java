package br.ime.usp.ws.airline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBAirlineConnection {
	private Connection conn;
	private Statement stm;
	

	public DBAirlineConnection(String file) throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
		this.stm = this.conn.createStatement();
	}
	

	public void initDB() {
		try {
			this.stm.executeUpdate("DROP TABLE IF EXISTS Flights");
			this.stm.executeUpdate("CREATE TABLE Flights (id varchar(10) PRIMARY KEY," +
					"destination varchar(50), date varchar(10), time varchar(10));");
			this.stm.executeUpdate("DROP TABLE IF EXISTS TravelAgencies");
			this.stm.executeUpdate("CREATE TABLE TravelAgencies (id varchar(10) PRIMARY KEY," +
			"name varchar(50));");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addFlight(FlightResult flight) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO Flights (id, destination, date, time) VALUES (\"" + flight.getId() + "\", \"" +
					flight.getDestination() + "\", \"" + flight.getDate() + "\", \"" + flight.getTime() + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void addTravelAgency(String name) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO TravelAgencies (name) VALUES (\"" + name + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void removeFligth(String id) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("DELETE FROM Flights WHERE " +
					"id=\"" + id + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public  FlightResult getFlight(String destination, String date) {
		FlightResult flight = new FlightResult();
		ResultSet rs;

		try {
			rs = this.stm.executeQuery("SELECT * FROM Flights WHERE destination =\"" + destination + "\" and date =\"" + date + "\"");
			if(rs.next()) {
				flight.setId(rs.getString("id"));
				flight.setDestination(rs.getString("destination"));
				flight.setDate(rs.getString("date"));
				flight.setTime(rs.getString("time"));
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return flight;
	}
	
	public ArrayList<String> getListOfTravelAgencies(){
		ArrayList<String> travelAgencies = new ArrayList<String>();
		
		ResultSet rs;

		try {
			rs = this.stm.executeQuery("SELECT * FROM TravelAgencies");
			if(rs.next()) {
				travelAgencies.add(rs.getString("name"));
			}
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return travelAgencies;
		
	}


}