package dao;

import model.PostLike;
import java.sql.*;

public class PostLikeDAO {
	
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String newLikeId(Connection conn) throws SQLException { // throw는 try-catch문 안써도 된다. 
		String maxLidQuery = "SELECT MAX(CAST(SUBSTRING(l_id, 2) AS SIGNED)) AS max_id FROM post_like WHERE l_id LIKE 'l%'";
		
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxLidQuery)){ // try-with-resources 구문은 try 블록 싱행이 왼료되거나
			// 예외가 발생해도 , stmt와 rs의 close를 자동으로 호출한다. 
			// conn 객체를 통해 SQL 쿼리를 데이터베이스로 보낼 수 있는 쿼리 실행기 객체를 생성한다. 
			
			int maxNum = 0;
		    if (rs.next() && rs.getString("max_id") != null) {
		        maxNum = rs.getInt("max_id"); 
		    }
		    
		    // 2. 새 ID 생성
		    return "l" + (maxNum + 1);
		}
	}
	public boolean checkLike(String postId, String likerId) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		String sql = "select l_id from post_like where liker_id = ? AND post_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, likerId);
			pstm.setString(2, postId);
			
			return pstm.executeUpdate() > 0;
		}
		
	}
	public boolean likePost(PostLike postlike) throws SQLException, ClassNotFoundException{
		Connection conn = getConnection();
		
		String sql = "INSERT INTO post_like (l_id, post_id, liker_id) VALUES (?, ?, ?)";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)){
			pstm.setString(1, postlike.getlikeId());
			pstm.setString(2, postlike.getpostId());
			pstm.setString(3, postlike.getLikerId());
			
			return pstm.executeUpdate() > 0;
		}
		
	}
	public boolean increasePostLikeCount(String postId) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "UPDATE posts SET num_of_likes = num_of_likes + 1 WHERE post_id = ?";
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, postId);
			return pstm.executeUpdate() > 0;
			
		}
	}

}