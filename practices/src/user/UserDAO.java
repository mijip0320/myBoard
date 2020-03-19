package user;

import java.sql.Connection;//디비 접근
import java.sql.DriverManager; //디비 매니저(접속 가능하게 하는 매개체)
import java.sql.PreparedStatement; //sql 구문 실행
import java.sql.ResultSet;//결과값 출력
import java.lang.*;

//Data Access Object-데이터 접근 객체
public class UserDAO {
	private Connection conn = null;//디비 접근 객체
	private PreparedStatement pstmt = null;//sql 구문 실행 객체
	private ResultSet rs = null;//결과값 출력 객체

	
	public UserDAO() {
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
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE USERID=?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,userID); //1이 첫번째에 해당
			
			rs = pstmt.executeQuery(); //실행된 결과값 저장, result set 객체에 결과값을 담을 때 사용
			
			
			if(rs.next()) { //rs에 결과값이 존재한다면, 아이디가 db에 있다면
				if(rs.getString(1).equals(userPassword)) { //아이디에 해당하는 패스워드가 유저가 입력한 패스워드와 같다면
					return 1; //로그인 성공
					
				}
				else {
					return 0; //비번 불일치
				}
			}
			
			return -1; //아이디가 없음
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return -2;//데이터베이스 오류
	}
	
	public int join(User user) {
		
		String SQL = "INSERT INTO USER VALUES(?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			
			return pstmt.executeUpdate(); //결과가 몇개인지 반환, 여기선 return 5라는 결과가 나옴
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return -1;//이미 아이디가 존재할 때
		
	}
}
