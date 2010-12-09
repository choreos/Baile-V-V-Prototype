package br.ime.usp.ws.travelagency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

public class DBTravelAgencyConnection {
	private Connection conn;
	private Statement stm;

	public DBTravelAgencyConnection(String file) throws SQLException,
			ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
		this.stm = this.conn.createStatement();
	}

	public void initDB() {
		try {
			this.stm.executeUpdate("DROP TABLE IF EXISTS Users");
			this.stm.executeUpdate("CREATE TABLE Users (id INTEGER PRIMARY KEY,"
					+ "name varchar(100), ccNumber varchar(30));");
			this.stm.executeUpdate("DROP TABLE IF EXISTS Reserves");
			this.stm.executeUpdate("CREATE TABLE Reserves (id varchar(20),"
					+ "name varchar(100), value varchar(30));");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String addUser(String user) {
		StringTokenizer parser =  new StringTokenizer(user,"|");
		String name = parser.nextToken();
		String ccNumber = parser.nextToken();
		
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO Users VALUES (NULL, \""
					+ name + "\", \"" + ccNumber + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ((Integer)getNumberOfUsers()).toString();
	}

	public void removeUser(String id) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("DELETE FROM Users WHERE " + "id=" + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getUser(String id) {
		String name = null, ccNumber = null;
		ResultSet rs;

		try {
			rs = this.stm.executeQuery("SELECT * FROM Users WHERE id =" + id);
			if (rs.next()) {
				name = rs.getString("name");
				ccNumber = rs.getString("ccNumber");
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return name + "|" + ccNumber;
	}

	public int getNumberOfUsers() {
		ResultSet rs;
		int lastId = 0;
 		
		try {
			rs = this.stm.executeQuery("SELECT max(id) from Users");
			if (rs.next()) {
				lastId = rs.getInt("max(id)");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return lastId;
	}

	public String getCCnumberByName(String name) {
		String ccNumber = null;
		ResultSet rs;

		try {
			rs = this.stm.executeQuery("SELECT ccNumber FROM Users WHERE name='" + name + "'");
			if (rs.next()) {
				ccNumber = rs.getString("ccNumber");
			}
			rs.close();

		} 
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return ccNumber;
	}

	public String addReserve(String reserveData) {
		StringTokenizer parser = new StringTokenizer(reserveData, "|");
		String id = parser.nextToken().toLowerCase();
		String name = parser.nextToken().replace("%20"," ");
		String value = parser.nextToken();
		
		int status = 0;
		
		try {
			this.stm = this.conn.createStatement();
			status = this.stm.executeUpdate("INSERT INTO Reserves VALUES (\"" + id + "\", \""
					+ name + "\", \"" + value + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return  (status > 0) ?  id :  null;
	}

	public String getReserve(String id) {
		String reserve = null;
		ResultSet rs;

		try {
			rs = this.stm.executeQuery("SELECT * FROM Reserves WHERE id='" + id + "'");
			if (rs.next()) {
				reserve = rs.getString("id") + "|" + rs.getString("name") + "|" + rs.getString("value");
			}
			rs.close();

		} 
		
		catch (SQLException e) { e.printStackTrace(); }
		
		return reserve;
	}

} 