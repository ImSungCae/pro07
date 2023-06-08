package sec01.ex01;

import java.sql.Connection;	// 특정 데이터베이스에 연결
import java.sql.Date;
import java.sql.DriverManager;	// JDBC 드라이버를 관리하기 위한 기본 클래스
import java.sql.PreparedStatement;	// SQL문을 미리 컴파일하기 위한 클래스
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	// jdbc:사용할 JDBC 드라이버 (oracle, mysql):드라이버타입:@서버이름(ip):포트번호:식별자(SID)
	private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private static final String user = "scott";
	private static final String pwd = "12341234";
	private Connection con;
	private PreparedStatement pstmt;
	// ResultSet은 객체이므로 다시 풀어주는 과정을 코딩 ( 클래스 )
	// ResultSet은 데이터베이스 내부적으로 수행된 SQL문의 처리 결과를 JDBC에서 쉽게 관리할수 있도록 해줌
	private ResultSet rs;

	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			connDB();
			String query = "select * from t_member";
			// prepareStatement(String sql)
			// Creates a PreparedStatement object for sendingparameterized SQL statements to
			// the database.

			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO vo = new MemberVO();
//				vo.setId(rs.getString(1)); 	// 컬럼에 col 해당하는 값을 가져옴
				vo.setId(rs.getString("id")); // 컬럼 이름(ID)에 해당하는 값을 가져옴
				vo.setPwd(rs.getString("pwd"));
				vo.setName(rs.getString("name"));
				vo.setEmail(rs.getString("email"));
				vo.setJoinDate(rs.getDate("joinDate"));
				System.out.printf("%s, %s, %s, %s, %s\n", vo.getId(),vo.getPwd(),vo.getName(),vo.getEmail(),vo.getJoinDate());
				list.add(vo);
			}
			rs.close();		//
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void connDB() {
		try {
			Class.forName(driver);
			System.out.println("Oracle 드라이버 로딩 성공");
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connection 생성 성공");
//			pstmt = con.createStatement();
			System.out.println("Statement 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
