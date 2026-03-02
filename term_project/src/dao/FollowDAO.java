package dao;

import model.Follow;
import java.sql.*;
public class FollowDAO {
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String getNewfId(Connection conn) throws SQLException, ClassNotFoundException {
		String maxFidQuery = "SELECT MAX(CAST(SUBSTRING(f_id, 2) AS SIGNED)) AS max_id FROM following WHERE f_id LIKE 'f%'";
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxFidQuery)) {
			
			int maxIdNum = 0;
			if(rs.next() && rs.getString("max_id") != null) {
				maxIdNum = rs.getInt("max_id");
			}
			
			return "f" + (maxIdNum + 1);
		}
	}
	
	public boolean isFollowing(String followerId, String followingId) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "SELECT following_id FROM following WHERE following_id = ? AND follower_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, followingId);
			pstm.setString(2,  followerId);
			
			try(ResultSet rs = pstm.executeQuery()) {
				return rs.next(); // if there is result, you are following him
			}
		}
	}
	
	public boolean insertFollow(Follow follow) throws SQLException, ClassNotFoundException { // can follow
		Connection conn = getConnection();
		
		String sql = "INSERT INTO following  (f_id, following_id, follower_id) VALUES (?, ?, ?)";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, follow.getfId());
			pstm.setString(2, follow.getFollowingId());
			pstm.setString(3, follow.getFollowerId());
			
			return pstm.executeUpdate() > 0;
		}
	}
	
	public boolean deleteFollow(Follow follow) throws SQLException, ClassNotFoundException { // already follow -> unfollow
		Connection conn = getConnection(); // 데이터베이스와 자바 코드를 연결하는 과정
		
		String sql = "DELETE FROM following WHERE following_id = ? AND follower_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, follow.getFollowingId());
			pstm.setString(2, follow.getFollowerId());
			
			return pstm.executeUpdate() > 0;
		}
	}
}