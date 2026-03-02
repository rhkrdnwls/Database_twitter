package dao;

import model.Tag;
import java.sql.*;

public class TagDAO {
	private Connection getConnection() throws SQLException, ClassNotFoundException {
		return database.TwitterDBService.getConnection();
	}
	
	public String NewTagId(Connection conn) throws SQLException, ClassNotFoundException {
		String maxTagQuery = "SELECT MAX(CAST(SUBSTRING(tag_id, 2) AS SIGNED)) AS max_id FROM tag WHERE tag_id LIKE 't%'";
		
		try(Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(maxTagQuery)) {
			
			int maxIdNum = 0;
			if(rs.next() && rs.getString("max_id") != null) {
				maxIdNum = rs.getInt("max_id");
			}
			
			return "t" + (maxIdNum + 1);
		}	
	}
	
	public boolean checkTag(Tag tag) throws SQLException, ClassNotFoundException{
		Connection conn = getConnection();
		
		String sql = "select tag_id from tag where post_id = ? and tagging_date = ? and tagged_id = ?";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)) {
			pstm.setString(1, tag.getPost_id());
			pstm.setString(2, tag.getTagged_date());
			pstm.setString(3, tag.getTagged_id());
			
			try(ResultSet rs = pstm.executeQuery()) {
				return rs.next();
			}
			
		}
	}
	public boolean insertTag(Tag tag) throws SQLException, ClassNotFoundException {
		Connection conn = getConnection();
		
		String sql = "insert into tag (tag_id, post_id, tagging_date, tagger_id, tagged_id) values(?, ?, ?, ?, ?)";
		
		try(PreparedStatement pstm = conn.prepareStatement(sql)){
			pstm.setString(1, tag.getTag_id());
			pstm.setString(2, tag.getPost_id());
			pstm.setString(3, tag.getTagged_date());
			pstm.setString(4, tag.getTagging_id());
			pstm.setString(5, tag.getTagged_id());
			
			return pstm.executeUpdate() > 0; // insert나 delete가 성공하면 1이상의 값을 반환한다. 
			// 쿼리 실행 결과로 반환된 행의 개수가 0보다 큰지 확인하는 비교연산이다. 
		}
	}

}