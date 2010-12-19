package br.usp.ime.ws.acquirer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

public class DBAcquirerConnection {
	private Connection conn;
	private Statement stm;

	public DBAcquirerConnection(String file) throws SQLException,
			ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + file);
		this.stm = this.conn.createStatement();
	}

	public void initDB() {
		try {
			this.stm.executeUpdate("DROP TABLE IF EXISTS Accounts");
			this.stm.executeUpdate("CREATE TABLE Accounts (ccNumber varchar(20), "
					+ "name varchar(100), credit Integer);");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String createAccount(String userData) {
		StringTokenizer parser = new StringTokenizer(userData, "|");
		String ccNumber = parser.nextToken();
		String name = parser.nextToken();
		int credit = Integer.parseInt(parser.nextToken());

		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("INSERT INTO Accounts VALUES (\"" + ccNumber
					+ "\", \"" + name + "\"," + credit + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ccNumber;
	}

	public String getAccount(String id) {
		String name = null;
		int credit = 0;
		ResultSet rs;

		try {
			rs = this.stm
					.executeQuery("SELECT * FROM Accounts WHERE ccNumber =\""
							+ id + "\"");
			if (rs.next()) {
				name = rs.getString("name");
				credit = rs.getInt("credit");
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id + "|" + name + "|" + credit;
	}

	private int getCreditAvailable(String id) {
		int credit = 0;
		ResultSet rs;

		try {
			rs = this.stm
					.executeQuery("SELECT * FROM Accounts WHERE ccNumber =\""
							+ id + "\"");
			if (rs.next()) {
				credit = rs.getInt("credit");
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return credit;
	}

	public String checkCreditCard(String number) {
		int credit = getCreditAvailable(number);

		if (number.equals("XXXXXX") || credit == 0)
			return "Credit card not approved";

		return "Credit card approved";
	}

	public String discountValue(String number, String value) {

		int valueToDiscount = Integer.parseInt(value);

		int newCredit = getCreditAvailable(number) - valueToDiscount;

		if (newCredit > 0)
			update(number, newCredit);

		return number;
	}

	private void update(String number, int newCredit) {

		try {
			this.stm = this.conn.createStatement();
			this.stm.executeUpdate("Update Accounts SET credit = " + newCredit
					+ " WHERE ccNumber =\"" + number + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean exists(String number){
		ResultSet rs = null;

		try {
			rs = this.stm
					.executeQuery("SELECT * FROM Accounts WHERE ccNumber =\""
							+ number + "\"");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (rs !=null);
	}

	public String updateAccount(String number, String name, String value) {

		if (exists(number))
			createAccount(number + "|" + name + "|" + value);
		
		
		else {
			try {
				this.stm = this.conn.createStatement();
				this.stm.executeUpdate("Update Accounts SET credit = " + value
						+ " and name = \"" + name + "\" WHERE ccNumber =\""
						+ number + "\"");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return number;
	}

}