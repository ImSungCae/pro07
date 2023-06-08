package sec02.ex01;

import java.sql.Connection; // 특정 데이터베이스에 연결
import java.sql.PreparedStatement; // SQL문을 미리 컴파일하기 위한 클래스
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {

	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public MemberDAO() {
		// JNDI 방식의 연결로서 MemberDAO 객체를 초기화

		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			System.out.println("톰캣의 context.xml에 정의되어 있는 이름부분에서 미정확 에러");
//			e.printStackTrace();
		}
	}

	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<>();
		try {
			con = dataFactory.getConnection();
			String query = "select * from t_member";
			System.out.println("prepareStatement : " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberVO vo = new MemberVO();
				vo.setId(rs.getString("id"));
				vo.setPwd(rs.getString("pwd"));
				vo.setName(rs.getString("name"));
				vo.setEmail(rs.getString("email"));
				vo.setJoinDate(rs.getDate("joinDate"));
				System.out.printf("%s, %s, %s, %s, %s\n", vo.getId(), vo.getPwd(), vo.getName(), vo.getEmail(),
						vo.getJoinDate());
				list.add(vo);
			}
			con.close();
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

//	커넥션 DB이므로 미리 연결객체를 만들어 놨기 때문에 필요없음
//	private void connDB() {  
//	}
}
