package bbs;

import java.sql.Connection;//디비 접근
import java.sql.DriverManager; //디비 매니저(접속 가능하게 하는 매개체)
import java.sql.PreparedStatement; //sql 구문 실행
import java.sql.ResultSet;//결과값 출력
import java.util.ArrayList;

public class BbsDAO {
	
	private Connection conn = null;//디비 접근 객체
	//private PreparedStatement pstmt = null;//sql 구문 실행 객체
	private ResultSet rs = null;//결과값 출력 객체

	
	public BbsDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/practice1?serverTimezone=Asia/Seoul&useSSL=false";
			String dbID = "practiceuser";
			String dbPassword = "alwl96";
			
			Class.forName("com.mysql.cj.jdbc.Driver"); //mysql server connection
			
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public String getDate(){
		//게시판 작성 시 현재 시간을 넣어주는 함수
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return rs.getString(1);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "";//데이터베이스 오류
	}
	
	public int getNext() { //다음에 쓸 게시글 번호 가져옴
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			
			return 1; //첫번 째 게시물입니다.
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1; //데이터베이스 오류
		
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO bbs VALUES (?,?,?,?,?,?)";
		
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			
			return pstmt.executeUpdate(); //return 6이 나옴
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1; //데이터베이스 오류
		
	}
	
	public ArrayList<Bbs> getList(int pageNumber){ //페이지에 따른 총 10개의 게시글 가져오기
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setInt(1,  getNext() - (pageNumber - 1) * 10);
			//1. 게시글이 총 16개, 페이지번호 2 ->7이 ?대입됨
			//2. 게시글 총 8개, 페이지번호1 ->9가 ?안으로 대입되서 게시글 8개가 전부 출력됨
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				list.add(bbs);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) { //특정 페이지가 존재하는지
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
		
	}
	
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
				
				try {
					PreparedStatement pstmt = conn.prepareStatement(SQL); //게시글 번호 리턴
					
					pstmt.setInt(1,bbsID);

					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						Bbs bbs = new Bbs();
						bbs.setBbsID(rs.getInt(1));
						bbs.setBbsTitle(rs.getString(2));
						bbs.setUserID(rs.getString(3));
						bbs.setBbsDate(rs.getString(4));
						bbs.setBbsContent(rs.getString(5));
						bbs.setBbsAvailable(rs.getInt(6));
						
						return bbs;
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				return null;
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) { //게시글 수정
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; //업데이트 쿼리문
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1,  bbsTitle);
			pstmt.setString(2,  bbsContent);
			pstmt.setInt(3,  bbsID);
			
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int delete(int bbsID) { //게시글 삭제
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; //삭제 쿼리문
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setInt(1,  bbsID);
			
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return -1;	
	}
}
