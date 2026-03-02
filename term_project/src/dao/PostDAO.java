package dao;

import model.Post;
import java.sql.*;

public class PostDAO {
	
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String newPostId(Connection conn) throws SQLException {
		 String maxIdQuery = "SELECT MAX(CAST(SUBSTRING(post_id, 2) AS SIGNED)) AS max_id FROM posts WHERE post_id LIKE 'p%'";
		 
		 try(Statement stmt = conn.createStatement();
				 ResultSet rs = stmt.executeQuery(maxIdQuery)) {
			 
			 int maxIdNum = 0;
			 
			 if(rs.next() && rs.getString("max_id") != null) {
				 maxIdNum = rs.getInt("max_id");
			 }
			 
			 return "p" + (maxIdNum + 1);
		 }
	}
	
	public boolean insertPost(Post post) throws SQLException, ClassNotFoundException{
		Connection conn = getConnection();
		
		String sql = "insert into posts (postId, text, id, like, dateStr) values (?, ?, ?, ?, ?)"; 
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, post.getPostId());
			pstm.setString(2, post.getText());
			pstm.setString(3, post.getId());
			pstm.setInt(4, post.getLike());
			pstm.setString(5, post.getDateStr());
			
			return pstm.executeUpdate() > 0;
		}
		
	}
}