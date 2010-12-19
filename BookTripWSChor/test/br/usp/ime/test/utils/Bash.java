package br.usp.ime.test.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Bash {

	public static void deployService(String target) {
		Runtime proc = Runtime.getRuntime();
		try {
			proc.exec("java -jar lib/chore-ws.jar ChoreWS " + target);
			Thread.sleep(3000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void undeployService(String target) {

		Process proc;
		try {
			proc = Runtime.getRuntime().exec("/bin/bash", null, new File("."));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(proc.getOutputStream()), 1024), true);
			out.println("ps aux | grep \"java -jar lib/chore-ws.jar ChoreWS "
					+ target
					+ "\" | grep -v grep | awk '{print $2}' > a; kill -9 $(cat a); rm a");
			out.println("exit");
			proc.waitFor();
			in.close();
			out.close();
			proc.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void cleanAcquirerDatabase() {
		Connection conn;
		Statement stm;
		if (new File("./db/acquirer.db").exists()) {

			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:db/acquirer.db");
				stm = conn.createStatement();

				stm.executeUpdate("Delete from Accounts");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void cleanAirlineDatabase() {

		Connection conn;
		Statement stm;
		if (new File("./db/airline.db").exists()) {

			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager.getConnection("jdbc:sqlite:db/airline.db");
				stm = conn.createStatement();

				stm.executeUpdate("Delete from Flights");
				stm.executeUpdate("Delete from TravelAgencies");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void cleanTravelAgencyDatabase() {
		Connection conn;
		Statement stm;
		if (new File("./db/travelagency.db").exists()) {

			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager
						.getConnection("jdbc:sqlite:db/travelagency.db");
				stm = conn.createStatement();

				stm.executeUpdate("Delete from Users");
				stm.executeUpdate("Delete from Reserves");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void cleanMessageTraceQueueDatabase() {
		Connection conn;
		Statement stm;
		if (new File("./db/messageTraceQueue.db").exists()) {

			try {
				Class.forName("org.sqlite.JDBC");
				conn = DriverManager
						.getConnection("jdbc:sqlite:db/messageTraceQueue.db");
				stm = conn.createStatement();

				stm.executeUpdate("Delete from MessageTraceQueue");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	
	public static void cleanAllDataBases() {
		cleanAcquirerDatabase();
		cleanTravelAgencyDatabase();
		cleanMessageTraceQueueDatabase();
	}


}
