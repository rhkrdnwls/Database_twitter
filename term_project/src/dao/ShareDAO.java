package dao;

import model.Share;
import java.sql.*;

public class ShareDAO {
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String NewShareId(Connection conn) throws SQLException, ClassNotFoundException {
		String maxShareIdQuery = "SELECT MAX(CAST(SUBSTRING(share_id, 2) AS SIGNED)) AS max_id FROM shared WHERE share_id LIKE 's%'";
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxShareIdQuery)){
			
			int maxIdNum = 0;
			if(rs.next() && rs.getString("max_id") != null) {
				maxIdNum = rs.getInt("max_id");
			}
			
			return "s" + (maxIdNum + 1);
		}
	}
	
	public boolean checkShare(String postId) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "select content from posts where post_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, postId);
			
			try(ResultSet rs = pstm.executeQuery()){
				return rs.next();
			}
		}
		
	}
	
	public boolean insertShare(Share share) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "insert into shared (share_id, post_id, sharer_id, shared_to, shared_at) values (?, ?, ?, ?, ?)";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, share.getShareId());
			pstm.setString(2, share.getPostId());
			pstm.setString(3, share.getSharingId());
			pstm.setString(4, share.getSharedTo());
			pstm.setString(5, share.getSharedAt());
			
			return pstm.executeUpdate() > 0;
		}
	}
	
	

}