package br.usp.ime.ws.traveler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMessagesConnection {
	private Connection conn;
	private Statement stm;
	
	/**
	 * O construtor cria uma nova conexão com o banco de dados sqlite contido
	 * no arquivo passado como parâmetro. A conexão é possibilitada pelo driver
	 * JDBC, fornecido por SQLiteJDBC.
	 */
	public DBMessagesConnection(String file) throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
		this.stm = this.conn.createStatement();
	}
	

	public void initDB() {
		try {
			this.stm.executeUpdate("DROP TABLE IF EXISTS MessagesIn");
			this.stm.executeUpdate("CREATE TABLE MessagesIn (id INTEGER PRIMARY KEY," +
					"message varchar(50), operation varchar(50));");
			
			this.stm.executeUpdate("DROP TABLE IF EXISTS MessagesOut");
			this.stm.executeUpdate("CREATE TABLE MessagesOut (id INTEGER PRIMARY KEY," +
					"message varchar(50) NOT NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessageOut(String message) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO MessagesOut  (message) VALUES (\"" +
					message + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public void addMessageIn(String message, String operation) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO MessagesIn (message, operation) VALUES (\"" +
					message + "\", \"" + operation + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private void removeMessage(String id, String table) {
		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("DELETE FROM " + table + " WHERE " +
					"id=\"" + id + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	

	public Request getMessageIn() {
		Request requestIn = new Request();
		String id = null;
		ResultSet rs;
		
		try {
			rs = this.stm.executeQuery("SELECT * FROM MessagesIn ");
			if(rs.next()) {
				requestIn.setMessage(rs.getString("message"));
				requestIn.setOperation(rs.getString("operation"));
				id = rs.getString("id");
			}
			rs.close();
			if(id != null)
				removeMessage(id, "MessagesIn");
			
		} catch (SQLException e) { e.printStackTrace(); }
		
		return requestIn;
	}
	
	public String getMessageOut() {
		String string = null, id = null;
		ResultSet rs;
		try {
			rs = this.stm.executeQuery("SELECT * FROM MessagesOut");
			if(rs.next()) {
				string = rs.getString("message");
				id = rs.getString("id");
			}
			rs.close();
			if(id != null)
				removeMessage(id, "MessagesOut");
			
		} catch (SQLException e) { e.printStackTrace();	}
		
		return string;
	}	

}