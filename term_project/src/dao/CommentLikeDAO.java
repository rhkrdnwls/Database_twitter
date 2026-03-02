package dao;

import model.CommentLike;
import java.sql.*;

public class CommentLikeDAO {
	
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String newCmtid(Connection conn) throws SQLException {
		String sql = "SELECT MAX(CAST(SUBSTRING(clid, 2) AS SIGNED)) AS max_id FROM comment_like WHERE clid LIKE 'c%'";
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql))  {
			
			int maxNumId = 0;
			if(rs.next() && rs.getString("max_id") != null) {
				maxNumId = rs.getInt("max_id");
			}
			
			return "c" + (maxNumId + 1);	
		}
	}
	
	public boolean checkLike(String cmtId, String liker_id) throws SQLException, ClassNotFoundException { // 좋아요 눌러있는지 확인
		Connection conn = getConnection();
		String sql = "SELECT clid FROM comment_like WHERE comment_id = ? AND liker_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, cmtId);
			pstm.setString(2, liker_id);
			
			return pstm.executeUpdate() > 0;
		}
	}
	
	public boolean cmtLike(CommentLike commentlike) throws SQLException, ClassNotFoundException { // 댓글 좋아요
		Connection conn = getConnection();
		
		String sql = "INSERT INTO comment_like (clid, cmtid, liker_id) VALUES (?, ?, ?)"; 
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, commentlike.getClid());
			pstm.setString(2, commentlike.getCmtid());
			pstm.setString(3, commentlike.getLiker_id());
			
			return pstm.executeUpdate() > 0;
		}
	}
	
	public boolean increaseCommentLike(String cmtId) throws SQLException, ClassNotFoundException{
		Connection conn = getConnection();
		
		String sql = "UPDATE comment SET nums_of_likes = nums_of_likes + 1 WHERE comment_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, cmtId);
			
			return pstm.executeUpdate() > 0;
		}
		
	}

	
}