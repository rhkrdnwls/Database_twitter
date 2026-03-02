package database;
import java.sql.*;

public class TwitterDBService {
	private static Connection conn = null;
	
	private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/twitter";
	private static final String DB_USER = "root";
	private static final String DB_PASSWD = "12345";
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if (conn == null || conn.isClosed()) {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
		}
		
		return conn;
	}
	
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
				conn = null;
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}