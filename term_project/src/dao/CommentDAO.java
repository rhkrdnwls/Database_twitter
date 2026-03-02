package dao;

import model.Comment;
import java.sql.*;

public class CommentDAO {
	
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String newCommentId(Connection conn) throws SQLException {
		String maxCmtIdQuery = "SELECT MAX(CAST(SUBSTRING(comment_id, 2) AS SIGNED)) AS "
				+ "max_id FROM comment WHERE comment_id LIKE 'c%'";
		
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxCmtIdQuery)){
			// Statement stmt를 통해 SQL 쿼리 실행이 가능한 객체 stmt를 만든다.
			// 그 stmt를 통해 maxCmtIdQuery의 쿼리문을 실행시킨다.
			// Select 문의 결과(maxCmtIdQuery)를 ResultSet의 객체 rs에 저장한다. 
			
			int maxIdNum = 0;
		    if (rs.next() && rs.getString("max_id") != null) {
		        maxIdNum = rs.getInt("max_id"); 
		    }
		    return "c" + (maxIdNum + 1);
		}
	}
	
	public boolean insertComment(Comment comment) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
	    String sql = "INSERT INTO comment VALUES (?, ?, ?, ?, ?)";
	    
	    try(PreparedStatement pstm = conn.prepareStatement(sql)) {
		    pstm.setString(1, comment.getCommentId());     
		    pstm.setString(2, comment.getContent()); 
		    pstm.setString(3, comment.getWriterId());  
		    pstm.setString(4, comment.getPostId()); 
		    pstm.setInt(5, comment.getNumOfLikes());
			
		    return pstm.executeUpdate() > 0;	
	    }
	}

}