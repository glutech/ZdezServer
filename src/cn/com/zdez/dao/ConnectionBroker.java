package cn.com.zdez.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.devdaily.opensource.database.DDConnectionBroker;

public class ConnectionBroker implements BuildConnection {

	private String driver = null;

	private String url = null;

	private String username = null;

	private String password = null;

	private int minConnections = 0;

	private int maxConnections = 0;

	private long timeout = 0;

	private long leaseTime = 0;

	private String logFile = null;

	private DDConnectionBroker broker = null;

	void setUp() {
		driver = "com.mysql.jdbc.Driver";
		url = "jdbc:mysql://localhost:3306/zdez?useUnicode=true&characterEncoding=UTF-8";
//		url = "jdbc:mysql://112.117.223.20:3306/zdez?useUnicode=true&characterEncoding=UTF-8";
		// url =
		// "jdbc:mysql://112.117.223.20:3306/zdez?useUnicode=true&characterEncoding=UTF-8";
		username = "root";
		password = "123";
		minConnections = 10;
		maxConnections = 100;
//		minConnections = 1;
//		maxConnections = 10;
		timeout = 100;
		leaseTime = 60000;
		logFile = "/root/ConnectionPool.log";
		broker = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see connectionbroker.database.BuildConnection#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		try {
			// construct the broker
			broker = new DDConnectionBroker(driver, url, username, password,
					minConnections, maxConnections, timeout, leaseTime, logFile);

		} catch (SQLException se) {
			// could not get a broker; not much reason to go on
			System.out.println(se.getMessage());
			System.out.println("Could not construct a broker, quitting.");

		}
		return broker.getConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see connectionbroker.database.BuildConnection#freeConnection()
	 */
	public void freeConnection(Connection conn) throws SQLException {
		try {
			broker.freeConnection(conn);
		} catch (Exception e) {
			System.out
					.println("Threw an exception trying to free my Connection: "
							+ e.getMessage());
		}
	}

	public int getNumberConnections() throws SQLException {
		if (broker != null)
			return broker.getNumberConnections();
		else
			return -1;
	}

	public ConnectionBroker() {
		super();
		setUp();
	}
}
