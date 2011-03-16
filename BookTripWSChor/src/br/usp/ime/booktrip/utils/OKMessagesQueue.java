package br.usp.ime.booktrip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.usp.ime.ws.traveler.Request;

public class OKMessagesQueue {
	private Connection conn;

	public OKMessagesQueue() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			this.conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/okQueue.hsqldb", "sa", "");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void shutdown() throws SQLException {

		Statement st = conn.createStatement();

		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		conn.close(); // if there are no other open connection
	}

	public synchronized String query(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		String name = null;

		if (rs.next()) {
			name = rs.getString("name");
		}
		rs.close();
		if (name != null)
			removeMessage(name);

		// do something with the result set.
		// dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet

		return name;
	}

	// use for SQL commands CREATE, DROP, INSERT and UPDATE
	public synchronized void update(String expression) throws SQLException {

		Statement st = null;

		st = conn.createStatement(); // statements

		int i = st.executeUpdate(expression); // run the query

		if (i == -1) {
			System.out.println("db error : " + expression);
		}

		st.close();
	}

	private void initDB() {
				
		try {
				update("DROP TABLE IF EXISTS Messages");
				update("CREATE TABLE Messages ( id INTEGER IDENTITY, name VARCHAR(50))");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addMessage(String name) {
		try {
			update("INSERT INTO Messages (name) VALUES ('" + name + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void removeMessage(String name) {
		try {
			update("DELETE FROM Messages" + " WHERE " + "name='" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Request getInData() {
		Request requestIn = new Request();
		try {
			String response = query("SELECT * FROM Messages");
			requestIn.setMessage(response);
			requestIn.setOperation(response);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return requestIn;
	}
	
	public static void main (String args[]){
		OKMessagesQueue okQueue = new OKMessagesQueue();
		okQueue.initDB();
	}

}