package dao;

import model.User;
import java.sql.*;

public class UserDAO {
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public boolean checkUser(String id) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "select user_id from user where user_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, id);
			
			try(ResultSet rs = pstm.executeQuery()){
				// executeQuery is for select, reading data
				// executeUpdate is for insert, update, delete, writing data(changing them)
				return rs.next(); // if there is result, return true
			}
		}
	}
	public boolean insertUser(User user) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "insert into user (user_id, pwd, number, email) values (?, ?, ?, ?) ";
		// PreparedStatement는 위처럼 SQL 템플릿과 데이터가 분리된 상태 "SELECT * FROM user WHERE id = ?"
		// Statement는 SQL과 데이터가 합쳐진 상태 "SELECT * FROM user WHERE id = '" + id + "'"
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)){
			pstm.setString(1, user.getUserId());
			pstm.setString(2, user.getPwd());
			pstm.setString(3, user.getPhoneNumber());
			pstm.setString(4, user.getEmail());
			
			return pstm.executeUpdate() > 0;
		}
	}
	
	public User checklogin(String id, String pwd) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "select user_id, pwd, number, email from user where user_id = ? and pwd = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, id);
			pstm.setString(2, pwd);
			
			try(ResultSet rs = pstm.executeQuery()) {
				if(rs.next()) {
					String userId = rs.getString("user_id");
					String email = rs.getString("email");
					String phone = rs.getString("number");
					
					return new User(userId, pwd, phone, email);
				}
				return null;
			}
		}
	}
}