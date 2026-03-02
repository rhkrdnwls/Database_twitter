package database;
import java.sql.*;
import java.util.Scanner;

public class ActiveLearning {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		Statement stmt = null; // Statement는 SQL 쿼리 문자열을 받아 데이터베이스에 살행 요청
		ResultSet rs = null; // select와 같은 조회 결과 집합의 결과 보관
		PreparedStatement pstm = null;
		
		Connection con = null;
		String id = null;
		
		int pcnt = 0;
		int cmt = 0;
		 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/twitter";
			String user = "root", passwd = "12345";
			con = DriverManager.getConnection(url, user, passwd);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		try {
			while(true) {
				System.out.println("welcome! type 0 for sign up, type 1 for log in");
				stmt = con.createStatement();
				int opt;
				String pwd = null;
				String number = null;
				String email = null;
				opt = scanner.nextInt();
				
				if(opt == 0) {
					System.out.println("Signning up");
					System.out.println("id, password, phone number, email");
					String s1 = null;
					id = scanner.next();
					pwd = scanner.next();
					number = scanner.next();
					email = scanner.next();
					
					stmt = con.createStatement();
					String s2 = "select user_id from user where user_id = \"" + id + "\"";
					rs = stmt.executeQuery(s2);
					// rs은 select와 같은 조회 결과 집합의 결과를 보관한다. 
					
					if(rs.next()) {
						System.out.println("User name already exists. please try again");
					}
					else {
						s1 = "insert into user values ( '" + id + "', '" + pwd + "', '" + number + "', '" + email + "')";
						
						pstm = con.prepareStatement(s1);
						pstm.executeUpdate();
					}
					
				}
				
				else if(opt == 1) {
					System.out.println("type in userid / password");
					id = scanner.next();
					pwd = scanner.next();
					
					stmt = con.createStatement();
					String s1 = "select user_id from user where user_id = \"" + id + "\" and pwd = \"" + pwd + "\"";
					rs = stmt.executeQuery(s1);
					
					if(rs.next()) {
						System.out.println("Logged in!!");
						break;
					}
					else {
						System.out.println("wrong id/password. please log in again. ");
					}
					
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();	
		}
		System.out.println("Welcome " + id);
		try {
			while(true) {
				System.out.println("1: write posts, 2: like posts, 3: write comment, 4: like comment, 5: follow/unfollow, 6: view following"
						+ ", 7: view follower, 8: tag other users, 9: share posts, 10: subsribe list, 11: subscribe ");
				int opt = scanner.nextInt();
				String rub = scanner.nextLine();
				int like = 0;
				
				if(opt == 1) { // 게시글 작성\
					
				    String wid = id;
				    String pid = null;
				    
					System.out.println("내용과 날짜를 입력하세요:");
				    String text = scanner.nextLine(); 
				    String dateStr = scanner.nextLine();
				    

				    String maxPidQuery = "SELECT MAX(CAST(SUBSTRING(post_id, 2) AS SIGNED)) AS max_id FROM posts WHERE post_id LIKE 'p%'";
				    rs = stmt.executeQuery(maxPidQuery); 
				    
				    int maxIdNum = 0;
				    
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    int newIdNum = maxIdNum + 1;
				    pid = "p" + newIdNum; 

				    String s1 = "insert into posts values (?, ?, ?, ?, ?)";
				    pstm = con.prepareStatement(s1);
				    // prepareStatement는 SQL 쿼리를 미리 정의하고, 데이터가 들어갈 자리는 ?로 바인딩해 실행

				    pstm.setString(1, pid);
				    pstm.setString(2, text);
				    pstm.setString(3, id);
				    pstm.setInt(4, like);
				    pstm.setString(5, dateStr);

				    pstm.executeUpdate();
				    
				    System.out.println("게시글이 성공적으로 작성되었습니다. ID: " + pid);
				}
				
				else if(opt == 2) { // 게시글 좋아요
					
					System.out.println("좋아요할 게시물 ID:");
					String postid = null;
					String uid = id;
					String lid = null;
					
					postid = scanner.nextLine();
						
					String maxLidQuery = "SELECT MAX(CAST(SUBSTRING(l_id, 2) AS SIGNED)) AS max_id FROM post_like WHERE l_id LIKE 'l%'";
				    
				    rs = stmt.executeQuery(maxLidQuery); 
				    
				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    // 2. 새 ID 생성
				    int newIdNum = maxIdNum + 1;
				    lid = "l" + newIdNum;
					
					String s2 = "select l_id from post_like where liker_id = ? AND post_id = ?";
					pstm = con.prepareStatement(s2);
				    
				    pstm.setString(1, uid); 
				    pstm.setString(2, postid);   
				    rs = pstm.executeQuery();
				    
	        	    if(rs.next()) {
	        	        System.out.println("Already liked post. Please try again!"); // 이미 좋아요를 눌렀다. 
	        	    }
	        	    else {
	        	    	String s3 = "INSERT INTO post_like VALUES (?, ?, ?)";
	        	        pstm = con.prepareStatement(s3);
	        	        pstm.setString(1, lid);
	        	        pstm.setString(2, postid);
	        	        pstm.setString(3, uid);
	        	        pstm.executeUpdate();
	        	        
	        	        String s4 = "UPDATE posts SET num_of_likes = num_of_likes + 1 WHERE post_id = ?";
	        	        pstm = con.prepareStatement(s4);
	        	        pstm.setString(1, postid); // WHERE 절에 post_id 바인딩
	        	        pstm.executeUpdate();
	        	        
	        	        System.out.println("게시글에 좋아요를 눌렀습니다! (ID: " + lid + ")");
	        	    }
	        	}
				else if(opt == 3) { // 댓글 달기
					
					String cmtid = null;
					String text = null;
					String wid = null;
					String pid = null;

					System.out.println("post_id, writer_id, context");
					
					pid = scanner.nextLine();
					wid = scanner.nextLine();
					text = scanner.nextLine();
					
					String maxCmtIdQuery = "SELECT MAX(CAST(SUBSTRING(comment_id, 2) AS SIGNED))"
							+ " AS max_id FROM comment WHERE comment_id LIKE 'c%'";

				    rs = stmt.executeQuery(maxCmtIdQuery); 
				    
				    int maxIdNum = 0;

				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    int newIdNum = maxIdNum + 1;
				    cmtid = "c" + newIdNum;
					
				    String s5 = "INSERT INTO comment VALUES (?, ?, ?, ?, ?)";
				    
				    pstm = con.prepareStatement(s5);
				    pstm.setString(1, cmtid);     
				    pstm.setString(2, text); 
				    pstm.setString(3, wid);  
				    pstm.setString(4, pid); 
				    pstm.setInt(5, like);
					
				    pstm.executeUpdate();
				}
				else if(opt == 4) { // 댓글 좋아요
					
					String cmtid = null;
					String liker_id = null;
					String clid = null;
					
					System.out.println("댓글 ID, 사용자 ID");
					cmtid = scanner.nextLine();
					liker_id = scanner.nextLine();
					
					String maxClidQuery = "SELECT MAX(CAST(SUBSTRING(clid, 2) AS SIGNED)) AS max_id FROM comment_like WHERE clid LIKE 'c%'";
				    
				    rs = stmt.executeQuery(maxClidQuery); 
				    
				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    int newIdNum = maxIdNum + 1;
				    clid = "c" + newIdNum;
					
				    String s6 = "SELECT clid FROM comment_like WHERE comment_id = ? AND liker_id = ?";
				    pstm = con.prepareStatement(s6);
				    
				    pstm.setString(1, cmtid);
				    pstm.setString(2, liker_id);   
				    rs = pstm.executeQuery();
					
					if(rs.next()) {
						System.out.println("Already liked comment. please try again");
					}
					else {
						
				        String s7 = "INSERT INTO comment_like VALUES (?, ?, ?)"; 
				        pstm = con.prepareStatement(s7);
				        pstm.setString(1, clid);
				        pstm.setString(2, cmtid);
				        pstm.setString(3, liker_id);
				        pstm.executeUpdate();
				        

				        String s8 = "UPDATE comment SET nums_of_likes = nums_of_likes + 1 WHERE comment_id = ?";
				        pstm = con.prepareStatement(s8);
				        pstm.setString(1, cmtid);
				        pstm.executeUpdate();
				        
				        System.out.println("댓글에 좋아요를 눌렀습니다! (ID: " + clid + ")");
				    }
				}
				else if(opt == 5) { // 팔로잉/언팔로잉
					
					String maxFidQuery = "SELECT MAX(CAST(SUBSTRING(f_id, 2) AS SIGNED)) AS max_id FROM following WHERE f_id LIKE 'f%'";

				    rs = stmt.executeQuery(maxFidQuery); 

				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    // 2. 새 f_id 생성 (INSERT 시 사용)
				    int newIdNum = maxIdNum + 1;
				    String fId = "f" + newIdNum;
				    
					System.out.println("팔로우 하고 싶은 id 입력");
					String following_id = scanner.nextLine(); // 팔로우 하고 싶은 사람
					String follower_id = id;
					
					String checkSql = "SELECT following_id FROM following WHERE following_id = ? AND follower_id = ?";
				    pstm = con.prepareStatement(checkSql);
				    pstm.setString(1, following_id);
				    pstm.setString(2, follower_id);
				    rs = pstm.executeQuery();
					
				    if (rs.next()) {
				        // 이미 팔로우 중이면: 언팔로우 (DELETE)
				        String unfollowSql = "DELETE FROM following WHERE following_id = ? AND follower_id = ?";
				        pstm = con.prepareStatement(unfollowSql);
				        pstm.setString(1, following_id);
				        pstm.setString(2, follower_id);
				        pstm.executeUpdate();
				        System.out.println(following_id + "님을 언팔로우했습니다.");
				    }
				    else {
				        // 팔로우 중이 아니면: 팔로우 (INSERT)
				        String followSql = "INSERT INTO following  (f_id, following_id, follower_id) VALUES (?, ?, ?)";
				        pstm = con.prepareStatement(followSql);
				        pstm.setString(1, fId);
				        pstm.setString(2, following_id);
				        pstm.setString(3, follower_id);
				        pstm.executeUpdate();
				        System.out.println(following_id + "님을 팔로우하기 시작했습니다!");
				    }
					
				}
				else if(opt == 6) { // 팔로우 목록
					System.out.println(id + "님이 팔로우 하는 목록");
					String sql = "select f.following_id from following f inner join user u on f.following_id = u.user_id where f.follower_id = ?";
					pstm = con.prepareStatement(sql);
				    pstm.setString(1, id); // 현재 로그인된 사용자 ID를 바인딩
				    rs = pstm.executeQuery();
				    
				    // 2. 결과 출력
				    int count = 0;
				    while (rs.next()) {
				        String followeeId = rs.getString("following_id"); 
				        System.out.println("→ ID: " + followeeId);
				        count++;
				    }
				    
				    if (count == 0) {
				        System.out.println("아직 팔로우하는 사용자가 없습니다.");
				    } else {
				        System.out.println("총 " + count + "명을 팔로우 중입니다.");
				    }
				}
				else if(opt == 7) { // 팔로워 목록
					System.out.println(id + "님을 팔로잉 하는 목록");
					String sql = "select f.follower_id from following f inner join user u on f.follower_id = u.user_id where f.following_id = ?";
					pstm = con.prepareStatement(sql);
					pstm.setString(1, id);
					rs = pstm.executeQuery();
					
					int count = 0;
					while(rs.next()) {
						String followerId = rs.getString("follower_id");
						// follower_id의 컬럼 값을 문자열로 받아온다. 
						System.out.println("-> ID: " + followerId);
						count++;
					}
					if(count == 0) {
						System.out.println("아직 팔로워가 없다.");
					}else {
						System.out.println("총 " + count + "명의 팔로워가 있다." );
					}
				}	
				
				else if(opt == 8) {
					String maxTagQuery = "SELECT MAX(CAST(SUBSTRING(tag_id, 2) AS SIGNED)) AS max_id FROM tag WHERE tag_id LIKE 't%'";

				    rs = stmt.executeQuery(maxTagQuery); 

				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    // 2. 새 f_id 생성 (INSERT 시 사용)
				    int newIdNum = maxIdNum + 1;
				    String tId = "t" + newIdNum;
				    
					System.out.println("태그 게시물 ID, 태그 날짜, 태그 하고 싶은 사람");
					String post_id = scanner.nextLine();
					String tagged_date = scanner.nextLine();
					String tagged_id = scanner.nextLine();
					String tagger_id = id;
					
					String sql = "select tag_id from tag where post_id = ? and tagging_date = ? and tagged_id = ?";
					pstm = con.prepareStatement(sql);
					pstm.setString(1, post_id);
					pstm.setString(2, tagged_date);
					pstm.setString(3, tagged_id);
					
					rs = pstm.executeQuery();
					
					if(rs.next()) {
						System.out.println("이미 태그 되어 있습니다.");
					}else {
						String tagsql = "insert into tag (tag_id, post_id, tagging_date, tagger_id, tagged_id) values(?, ?, ?, ?, ?)";
						pstm = con.prepareStatement(tagsql);
						pstm.setString(1, tId);
						pstm.setString(2, post_id);
						pstm.setString(3, tagged_date);
						pstm.setString(4, tagger_id);
						pstm.setString(5, tagged_id);
						
						pstm.executeUpdate();
						System.out.println(tagged_id + "님을 " + post_id + "게시물에 태그했습니다." );
					}
				
					
				}
				else if(opt == 9) {
					System.out.println("공유할 게시글 ID, 공유할 사람, 공유 날짜");
					String postId = scanner.nextLine();
					String shared_to = scanner.nextLine();
					String shared_at = scanner.nextLine();
					
					String sharing_id = id;
					String sharedId = null;
					
					String maxShareIdQuery = "SELECT MAX(CAST(SUBSTRING(share_id, 2) AS SIGNED)) AS max_id FROM shared WHERE share_id LIKE 's%'";
				    rs = stmt.executeQuery(maxShareIdQuery); 
				    
				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    sharedId = "s" + (maxIdNum + 1);
				    
				    String InsertSQL = "insert into shared (share_id, post_id, sharer_id, shared_to, shared_at) values (?, ?, ?, ?, ?)";
				    pstm = con.prepareStatement(InsertSQL);
				    pstm.setString(1, sharedId);
				    pstm.setString(2, postId);
				    pstm.setString(3, sharing_id);
				    pstm.setString(4, shared_to);
				    pstm.setString(5, shared_at);
				    
				    pstm.executeUpdate();
				    
				    String contentSql = "select content from posts where post_id = ?";
				    pstm = con.prepareStatement(contentSql);
				    pstm.setString(1, postId);
				    rs = pstm.executeQuery();
				    
				    if(rs.next()) {
				    	String content = rs.getString("content");
				    	
				    	System.out.println(shared_to + "님에게 게시물을 공유했습니다.");
				    	System.out.println(content);
				    }
					
				}
				else if(opt == 10) {
					System.out.println("유료 구독 테이블");
					String sub_plan = "select plan_id, plan_name, duration, price from subscribe_plan";
					
					pstm = con.prepareStatement(sub_plan);
					rs = pstm.executeQuery();

					while(rs.next()) {
						String planId = rs.getString("plan_id");
						String planName = rs.getString("plan_name");
						String duration = rs.getString("duration");
						String planPrice = rs.getString("price");
						
						System.out.println(planId + " " + planName + " " + duration + " " + planPrice);
					}
				}
				else if(opt == 11) {					
					String maxSubIdQuery = "SELECT MAX(CAST(SUBSTRING(sub_id, 3) AS SIGNED)) AS max_id FROM user_subscribe WHERE sub_id LIKE 'us%'";
				    rs = stmt.executeQuery(maxSubIdQuery); 
				    
				    int maxIdNum = 0;
				    if (rs.next() && rs.getString("max_id") != null) {
				        maxIdNum = rs.getInt("max_id"); 
				    }
				    
				    System.out.println("구독하고 싶은 ID");
				    String subId = "us" + (maxIdNum + 1);
				    String userId = id;
				    String planId = scanner.nextLine();
				    
				    String durationsql = "select duration from subscribe_plan where plan_id = ?";
				    pstm = con.prepareStatement(durationsql);
				    pstm.setString(1, planId);
				    rs = pstm.executeQuery();
				    int duration = 0;
				    		
				    if(rs.next()) {
				    	duration = rs.getInt("duration");
				    }
				    
				    String subscribe = "insert into user_subscribe (sub_id, plan_id, user_id, end_date) values (? , ?, ?, DATE_ADD(NOW(), INTERVAL ? MONTH))";
				    pstm = con.prepareStatement(subscribe);
				    pstm.setString(1, subId);
				    pstm.setString(2, planId);
				    pstm.setString(3, userId);
				    pstm.setInt(4, duration);
				    
				    pstm.executeUpdate();
				    
				    System.out.println(planId + " 구독 완료");
				    System.out.println("기간: " + duration + "개월");
			    
				}
				else {
					System.out.println("프로그램 종료");					
					break;
				}
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
