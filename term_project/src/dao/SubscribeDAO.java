package dao;

import model.Subscribe;
import java.sql.*;

public class SubscribeDAO {
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String newSubId(Connection conn) throws SQLException, ClassNotFoundException {
		String maxSubIdQuery = "SELECT MAX(CAST(SUBSTRING(sub_id, 3) AS SIGNED)) AS max_id FROM user_subscribe WHERE sub_id LIKE 'us%'";
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxSubIdQuery)) {
			
			int maxIdNum = 0;
			if(rs.next() && rs.getString("max_id") != null) {
				maxIdNum = rs.getInt("max_id");
			}
			
			return "us" + (maxIdNum + 1);
		}
	}
	public int checkDuration(String planId) throws SQLException, ClassNotFoundException{
		Connection conn = getConnection();
		
		String sql = "select duration from subscribe_plan where plan_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)){
			pstm.setString(1, planId);
			
			try(ResultSet rs = pstm.executeQuery()){
				if(rs.next()) {
					return rs.getInt("duration");
				}else {
					return 0;
				}		
			}
			
		}
	}
	
	public boolean insertSubscribe(Subscribe subscribe) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "insert into user_subscribe (sub_id, plan_id, user_id, end_date) values (? , ?, ?, DATE_ADD(NOW(), INTERVAL ? MONTH))";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)){
			pstm.setString(1, subscribe.getSubId());
			pstm.setString(2, subscribe.getPlanId());
			pstm.setString(3, subscribe.getUserId());
			pstm.setInt(4, subscribe.getDuration());
			
			return pstm.executeUpdate() > 0;
		}
	}

}