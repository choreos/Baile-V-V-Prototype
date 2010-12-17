package br.usp.ime.booktrip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class MessageTraceQueue {
	private Connection conn;
	private  static Statement stm;
	
		

	public MessageTraceQueue() {
		try {
			Class.forName("org.sqlite.JDBC");
			this.conn = DriverManager.getConnection("jdbc:sqlite:messageTraceQueue.db");
			stm = this.conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void initDB() {
		try {
			stm.executeUpdate("DROP TABLE IF EXISTS MessageTraceQueue");
			stm.executeUpdate("CREATE TABLE MessageTraceQueue (emissor varchar(20), receptor varchar(20),"
					+ "name varchar(30), content varchar(50));");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void add(MessageTrace messageTrace) {
		try {

			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO MessageTraceQueue  (emissor, receptor, name, content) VALUES (\""
					+ messageTrace.getEmissor()
					+ "\", \""
					+ messageTrace.getReceptor()
					+ "\", \""
					+ messageTrace.getName()
					+ "\", \""
					+ messageTrace.getContent() + "\")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void remove(String emissor, String receptor, String name) {
		try {
			stm = this.conn.createStatement();
			stm.executeUpdate("DELETE FROM MessageTraceQueue WHERE "
					+ "emissor=\"" + emissor + "\" and name=\"" + name + "\" and receptor=\"" + receptor + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String get(String emissor, String receptor, String name) {
		String content = null;
		ResultSet rs;

		try {
			rs = stm
					.executeQuery("SELECT * FROM MessageTraceQueue Where emissor=\""
							+ emissor + "\" and name=\"" + name + "\" and receptor=\"" + receptor + "\"");
			if (rs.next())
				content = rs.getString("content");

			rs.close();
			if (content != null)
				remove(emissor, receptor, name);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return content;
	}
	
	public static void main(String args[]){
		MessageTraceQueue queue = new MessageTraceQueue();
		queue.initDB();
	}

}