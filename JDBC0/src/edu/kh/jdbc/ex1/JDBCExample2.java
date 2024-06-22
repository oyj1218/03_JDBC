package edu.kh.jdbc.ex1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample2 {

	public static void main(String[] args) {
		
		// [1단계] : JDBC 객체 참조 변수 선언 (java.sql 패키지)
		
		Connection conn = null;
		// Java와 DB 사이의 연결 통로
		Statement stmt = null;
		// Connection 객체를 통해 Java에서 작성된 SQL을 DB로 전달하여 수행한 후
		// 결과를 반환받아 다시 Java로 돌아오는 역할의 객체
		ResultSet rs = null;
		// SELECT 질의 성공 시 반환되는 결과 행의 집합(Result Set)을 나타내는 객체
		
		try {
			
			// [2단계] 참조변수에 알맞은 객체 대입하기
			
			// 1. DB 연결에 필요한 Oracle JDBC Driver 메모리 로드하기
			// -> Oracle JDBC Driver가 어디에 있는지만 알려 주면 알아서 메모리 로드
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// ClassNotFoundException 발생 가능성 있음
						
			// 2. 연결 정보를 담은 Connection 객체 생성
			// 이때 DriverManager 객체 필요함 (JDBC 드라이버를 통해 Connection 객체를 만드는 역할)			
			
			String type = "jdbc:oracle:thin:@"; // JDBC 드라이버 타입
			String ip = "localhost"; // DB 서버 컴퓨터 IP
			String port = ":1521";
			String sid = ":xe"; // DB 이름
			String user = "oyj"; // 사용자명
			String pw = "oyj1234"; // 비밀번호
			
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			// oracle.jdbc.driver.T4CConnection@5562c41e
			
			System.out.println(conn);
			
			// 3. Statement 객체에 적재할 SQL 작성하기
			
			// 1번의 결과를 이름 오름차순으로 정렬(DB)해서 조회
		    //    + 급여 합계를 구해서 출력(Java)
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY, DEPT_CODE FROM EMPLOYEE GROUP BY EMP_ID, EMP_NAME, SALARY, DEPT_CODE ORDER BY EMP_NAME";
		
			// 4. Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. SQL을 Statement에 적재 후 DB로 전달하여 수행 후 결과를 반환받아 와 rs 변수에 대입
			rs = stmt.executeQuery(sql);
			
			// [3단계] SELECT 수행 결과를 한 행씩 접근하여 원하는 컬럼 값 얻어오기
			
			int sum = 0; // 합계를 위한 변수
			while(rs.next()) {
				
				// rs.get[Type]("컬럼명") : 컬럼명의 값을 얻어옴
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				String deptCode = rs.getString("DEPT_CODE");
				
				// 조회 결과 출력
				System.out.printf("사번 : %d 이름 : %s 급여 : %7d 부서 코드 : %s\n",
									empId, empName, salary, deptCode);
				
				sum += salary;
			}
			
			System.out.println("급여 합계 : " + sum);
			
		} catch(SQLException e) {
			// SQLException : DB 연결 관련 예외 최상위 부모
			e.printStackTrace();
			
		} catch(ClassNotFoundException e) {
			System.out.println("OJDBC 라이브러리 미등록 또는 경로 오타");
			e.printStackTrace();
			
		} finally{
			
			// [4단계] 사용한 JDBC 객체 자원 반환(close)
			// 생성 순서 : Connection -> Statement -> ResultSet
			// 반환 순서 : ResultSet -> Statement -> Connection
			
			try {
				// NullPointerException 방지를 위한 if문 추가
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}