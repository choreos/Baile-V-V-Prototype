package br.usp.ime.ws.traveler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMessagesConnection {
	private Connection conn;

	private static DBMessagesConnection db;
	
	private DBMessagesConnection(String file) throws SQLException,
			ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");
		this.conn = DriverManager.getConnection("jdbc:hsqldb:file:" + file, "sa", "");
	}

	public static DBMessagesConnection getInstance(String file)
			throws SQLException, ClassNotFoundException {
		if (db == null) {
			db = new DBMessagesConnection(file);
			db.initDB();
		}
		return db;
	}

	public void shutdown() throws SQLException {

		Statement st = conn.createStatement();

		// db writes out to files and performs clean shuts down
		// otherwise there will be an unclean shutdown
		// when program ends
		st.execute("SHUTDOWN");
		conn.close(); // if there are no other open connection
	}

	// use for SQL command SELECT
	public synchronized Request queryIn(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		Request requestIn = new Request();
		String id = null;

		if (rs.next()) {
			requestIn.setMessage(rs.getString("message"));
			requestIn.setOperation(rs.getString("operation"));
			id = rs.getString("id");
		}
		rs.close();
		if (id != null)
			removeMessage(id, "MessagesIn");

		// do something with the result set.
		//dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.

		return requestIn;
	}

	public synchronized String queryOut(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

		String response = null;
		String id = null;

		if (rs.next()) {
			response = rs.getString("message");
			id = rs.getString("id");
		}
		rs.close();
		if (id != null)
			removeMessage(id, "MessagesOut");

		// do something with the result set.
		//dump(rs);
		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.

		return response;
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

	public static void dump(ResultSet rs) throws SQLException {

		// the order of the rows in a cursor
		// are implementation dependent unless you use the SQL ORDER statement
		ResultSetMetaData meta = rs.getMetaData();
		int colmax = meta.getColumnCount();
		int i;
		Object o = null;

		// the result set is a cursor into the data. You can only
		// point to one row at a time
		// assume we are pointing to BEFORE the first row
		// rs.next() points to next row and returns true
		// or false if there is no next row, which breaks the loop
		for (; rs.next();) {
			for (i = 0; i < colmax; ++i) {
				o = rs.getObject(i + 1); // Is SQL the first column is indexed

				// with 1 not 0
				System.out.print(o.toString() + " ");
			}

			System.out.println(" ");
		}
	}

	public void closeConnection() throws SQLException {
		conn.close();
	}

	private void initDB() {

			try {
				update("DROP TABLE IF EXISTS MessagesIn");
				update("DROP TABLE IF EXISTS MessagesOut");
				update("CREATE TABLE MessagesIn ( id INTEGER IDENTITY, message VARCHAR(50), operation VARCHAR(50))");
				update("CREATE TABLE MessagesOut ( id INTEGER IDENTITY, message VARCHAR(50))");
			}

			catch (SQLException e) {
				e.printStackTrace();
			}
		
		
	}

	public void addMessageOut(String message) {
		try {
			update("INSERT INTO MessagesOut  (message) VALUES ('" + message
					+ "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addMessageIn(String message, String operation) {
		try {
			update("INSERT INTO MessagesIn (message, operation) VALUES ('"
					+ message + "', '" + operation + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void removeMessage(String id, String table) {
		try {
			update("DELETE FROM " + table + " WHERE " + "id='" + id + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Request getMessageIn() {
		Request requestIn = new Request();
		try {
			requestIn = queryIn("SELECT * FROM MessagesIn");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return requestIn;
	}

	public String getMessageOut() {
		String response = null;

		try {
			response = queryOut("SELECT * FROM MessagesOut");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return response;
	}
	
}