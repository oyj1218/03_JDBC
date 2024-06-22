package edu.kh.jdbc.model.dao;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import edu.kh.jdbc.model.vo.Employee;

// DAO(Data Access Object) : 데이터 접근 객체
// - DB와 연결되어 SQL을 수행하고 결과를 반환 받는 역할
public class EmployeeDAO {

	// JDBC 객체 저장용 참조 변수 필드 선언
	private Connection conn;
	// DB 연결 정보를 담은 객체(Java - DB 사이의 통로 역할)

	private Statement stmt;
	// Connection을 통해 SQL을 수행하고 결과를 반환 받는 객체

	private PreparedStatement pstmt;
	// Statement의 자식으로 좀 더 향상된 기능을 제공
	// - ? (위치홀더)를 이용하여 SQL에 작성되어지는 리터럴을 동적으로 제어함
	// --> 오타 위험 감소, 가독성 상승

	private ResultSet rs;
	// SELECT 수행 후 반환되는 객체

	/**
	 * 
	 * 1. 전체 사원 정보 조회 DAO
	 * @return
	 * 
	 */
	public List<Employee> selectAll(){

		// 결과 저장용 변수 준비
		List<Employee> empList = new ArrayList<>();

		try {
			// 1) Oracle JDBC Driver 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2) DB 연결 작업(Conncection 얻어오기)
			String type = "jdbc:oracle:thin:@"; // JDBC 드라이버가 thin 타입
			String ip = "localhost"; // DB 서버 컴퓨터 IP
			String port = ":1521";
			String sid = ":xe"; // DB 이름
			String user = "oyj"; // 사용자명
			String pw = "oyj1234"; // 비밀번호

			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			// DriverManager : Connection 생성 메소드 제공


			// 3) Statement 객체에 적재할 SQL 작성하기
			// == 셔틀버스 만들기
			// ********* JAVA에서 작성된 SQL문은 마지막에 ;을 찍지 않는다

			String sql = "SELECT * FROM EMPLOYEE ORDER BY EMP_ID";

			// 4) Statement 객체 생성
			stmt = conn.createStatement(); // 커넥션을 왔다 갔다 하는 셔틀버스 같은 역할

			// 5) SQL 수행 후 결과(ResultSet) 반환 받기
			rs = stmt.executeQuery(sql);
			// executeQuery():select문 수행 후 ResultSet 결과 반환

			// 6) 결과를 List에 옮겨 담기
			// -> ResultSet에 한 행씩 접근하여 컬럼값을 얻어와 한 행의 정보가 담긴 Employee 객체를 생성학
			// 이를 empList에 추가
			while(rs.next()) {
				// rs.next() : 다음 행이 있으면 true, 호출 시 마다 다음 행으로 이동

				int empId = rs.getInt("EMP_ID"); // 현재 행의 EMP_ID 컬럼 값을 int 자료형으로 엳어옴
				String empName = rs.getString("EMP_NAME");
				String empNO = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				String salLevel = rs.getString("SAL_LEVEL");
				int salary = rs.getInt("SALARY");
				double bonus = rs.getDouble("BONUS");
				int managerId = rs.getInt("MANAGER_ID");
				Date hireDate = rs.getDate("Hire_DATE");
				Date entDate = rs.getDate("ENT_DATE");
				char entYn = rs.getString("ENT_YN").charAt(0);

				// rs.getChar()는 존재하지 않음
				// 왜? 자바에서는 문자 하나(char) 개념이 있지만
				// DB에서는 오로지 문자열 개념만 존재할
				// -> String.charAt(0)을 이용한다


				// 얻어온 컬럼 값으로 객체 생성 후 초기화
				Employee emp = new Employee(empId, empName, empNO, email, phone, deptCode, jobCode, salLevel, salary,
						bonus, managerId, hireDate, entDate, entYn);

				// empList에 추가
				empList.add(emp);
			}

		} catch (Exception e) {
			// Exception : 모든 예외의 최상위 부모
			// -> try에서 발생하는 모든 예외를 잡아서 처리
			e.printStackTrace();

		} finally {
			// 7) 사용한 JDBC 차원 반환

			try {
				// NullPinterException 방지를 위한 if문 추가
				if(rs !=null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();

			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		// 8) 호출부로 List 반환
		return empList;
	}

	/** 
	 * 2. 사번으로 사원 정보 조회 DAO
	 * @param input
	 * @return
	 */
	public Employee selectOne(int input) {
		// 결과 저장용 변수
		Employee emp = null;

		try {
			// 1) Oracle JDBC Driver 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2) DB 연결 작업(Conncection 얻어오기)
			String type = "jdbc:oracle:thin:@"; // JDBC 드라이버가 thin 타입
			String ip = "localhost"; // DB 서버 컴퓨터 IP
			String port = ":1521";
			String sid = ":xe"; // DB 이름
			String user = "oyj"; // 사용자명
			String pw = "oyj1234"; // 비밀번호

			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			// DriverManager : Connection 생성 메소드 제공


			// 3) Statement 객체에 적재할 SQL 작성하기
			// == 셔틀버스 만들기
			// ********* JAVA에서 작성된 SQL문은 마지막에 ;을 찍지 않는다

			String sql = "SELECT * FROM EMPLOYEE WHERE EMP_ID = " + input;

			// 4) Statement 객체 생성
			stmt = conn.createStatement(); // 커넥션을 왔다 갔다 하는 셔틀버스 같은 역할

			// 5) SQL 수행 후 결과(ResultSet) 반환 받기
			rs = stmt.executeQuery(sql);
			// executeQuery():select문 수행 후 ResultSet 결과 반환


			// 6) 조회 결과가 있으면 emp 객체 생성
			// 조회 결과 있다면 1행 밖에 나오지 않으므로 while 대신 if문을 사용한다
			if(rs.next()) {
				// rs.next() : 다음 행이 있으면 true, 호출 시 마다 다음 행으로 이동
				// 조회 결과가 있으면 rs.next() == true --> if문 수행
				// 조회 결과가 없으면 rs.next() == false --> if문 수행 X --> Employee 객체 생성 X

				int empId = rs.getInt("EMP_ID"); // 현재 행의 EMP_ID 컬럼 값을 int 자료형으로 엳어옴
				String empName = rs.getString("EMP_NAME");
				String empNO = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				String salLevel = rs.getString("SAL_LEVEL");
				int salary = rs.getInt("SALARY");
				double bonus = rs.getDouble("BONUS");
				int managerId = rs.getInt("MANAGER_ID");
				Date hireDate = rs.getDate("Hire_DATE");
				Date entDate = rs.getDate("ENT_DATE");
				char entYn = rs.getString("ENT_YN").charAt(0);

				// 얻어온 컬럼 값으로 객체 생성 후 초기화
				emp = new Employee(empId, empName, empNO, email, phone, deptCode, jobCode, salLevel, salary,
						bonus, managerId, hireDate, entDate, entYn);

			}

		} catch (Exception e) {
			// Exception : 모든 예외의 최상위 부모
			// -> try에서 발생하는 모든 예외를 잡아서 처리
			e.printStackTrace();

		} finally {
			// 7) 사용한 JDBC 차원 반환
			try {
				// NullPinterException 방지를 위한 if문 추가
				if(rs !=null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();

			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		// 조회 결과가 있으면 Employee 객체 주소
		// 없으면 null 반환
		return emp;
	}

	/**
	 * 3. 입력받은 급여 이상으로 모든 직원 조회 DAO
	 * @param input
	 * @return
	 */
	public List<Employee> selectSalary(int input) {

		List<Employee> empList = new ArrayList<Employee>();

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리 로드

			String type = "jdbc:oracle:thin:@"; 
			String ip = "localhost"; // 접속할 아이피
			String port = ":1521"; 
			String sid = ":xe"; // 접속할 DB 이름
			String user = "oyj"; // 사용자 계정명
			String pw = "oyj1234"; // 사용자 계정 비밀번호

			// 커넥션 생성
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);

			String sql = "SELECT * FROM EMPLOYEE2 WHERE SALARY >=" + input;

			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				// rs.next(): 다음 행이 있으면 true. 호출 시마다 다음 행으로 이동

				int empId = rs.getInt("EMP_ID"); // 현재 행의 EMP_ID 컬럼 값을 int 자료형으로 얻어옴
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				String salLevel = rs.getString("SAL_LEVEL");
				int empSalary = rs.getInt("SALARY");
				double empBonus = rs.getDouble("BONUS"); // 실수형
				int managerId = rs.getInt("MANAGER_ID");
				Date hireDate = rs.getDate("HIRE_DATE");
				Date entDate = rs.getDate("ENT_DATE");
				char entYn = rs.getString("ENT_YN").charAt(0); 

				// 얻어온 컬럼값으로 객체 생성 후 초기화
				Employee emp = new Employee(empId, empName, empNo, email, phone,
						deptCode, jobCode,salLevel, empSalary,
						empBonus, managerId, hireDate, entDate, entYn);

				// empList에 추가
				empList.add(emp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return empList;
	}

	/** 4. 새로운 사원 정보 추가 DAO
	 * @param emp
	 * @return
	 */
	public int insertEmployee(Employee emp) {

		int result =0; // 결과 저장용 변수

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리 로드

			String type = "jdbc:oracle:thin:@"; 
			String ip = "localhost"; // 접속할 아이피
			String port = ":1521"; 
			String sid = ":xe"; // 접속할 DB 이름
			String user = "oyj"; // 사용자 계정명
			String pw = "oyj1234"; // 사용자 계정 비밀번호

			// 커넥션 생성
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			// -> 생성된 커넥션을 이용해 SQL을 수행하면 자동 커밋이 됨(기본값)
			// --> 자동 커밋 기능을 끄고 개발자가 트랜잭션을 직접 제어하는 게 좋다.

			conn.setAutoCommit(false); // 자동 커밋 기능 비활성화
			// --> 자동 커밋을 비활성화 시켜도
			//	   conn.close()가 실행되면 남은 트랜잭션이 모두 COMMIT 된다.

			// SQL 작성
			String sql = "INSERT INTO EMPLOYEE2 VALUES(?,?,?,?,?,?,?,'S5', ?, ?, 200, SYSDATE, NULL, 'N')";
			// INSERT에 컬럼명을 따로 지정하지 않았기 때문에 14개임
			// ? 기호 == 위치홀더
			// Statement: 커넥션 생성 - SQL 작성 - Statement 객체 생성 - SQL 수행 후 결과 반환

			// PreparedStatement: 커넥션 생성 - SQL 작성(? 사용) - PreparedStatement 객체 생성(SQL 적재) 
			//						- 위치홀더에 알맞는 값 대입 - SQL 수행 후 결과 반환

			// PreparedStatement 객체 생성(SQL 적재)
			pstmt = conn.prepareStatement(sql); // 적재만 함 (결과 반환 X) 반환하려면 executeQuery를 써야 하는데 이때 sql 담지 않음

			// 위치홀더에 알맞은 값 대입
			// pstmt.set[Type](위치홀더 순서, 값)

			pstmt.setInt(1, emp.getEmpId()); // 입력받은 사번을 1번 ?(위치홀더)에 세팅
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getEmpNo());
			pstmt.setString(4, emp.getEmail());
			pstmt.setString(5, emp.getPhone());
			pstmt.setString(6, emp.getDeptCode());
			pstmt.setString(7, emp.getJobCode());
			pstmt.setInt(8, emp.getSalary()); 
			pstmt.setDouble(9, emp.getBonus());

			// SQL 수행 후 결과 반환받기
			// 1) Statement - SELECT		: stmt.executeQuery(sql); --> rs
			// 실행할 때 sql 적재하고 보냈는데
			// 2) PreparedStatement - SELECT: pstmt.executeQuery(); <-- SQL 다시 담지 않음!!! -- rs
			// 이미 객체 생성할 때 적재해서 따로 보내지 않는다

			// **** DML 수행 시 executeUpdate 사용 ****
			// 3) Statement - DML			: stmt.executeUpdate(sql);
			// 4) PreparedStatement - DML	: pstmt.executeUpdate(); <-- SQL 다시 담지 않음!!!
			// 똑같이 따로 적재하지 않는다

			result = pstmt.executeUpdate(); // INSERT, UPDATE, DELETE가 성공한 행의 개수 반환
			// 조건에 맞는 행이 없으면 0 반환

			// ***** 트랜잭션 제어 *****
			if (result > 0) conn.commit();   // DML 성공 시 commit 수행
			else			conn.rollback(); // DML 실패 시 rollback 수행

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/** 5. 사번으로 사원 정보 삭제 DAO
	 * @param input
	 * @return
	 */
	public int deleteEmployee(int input) {
		
		int result = 0; // 결과 저장용 변수
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리 로드

			String type = "jdbc:oracle:thin:@"; 
			String ip = "localhost"; // 접속할 아이피
			String port = ":1521"; 
			String sid = ":xe"; // 접속할 DB 이름
			String user = "oyj"; // 사용자 계정명
			String pw = "oyj1234"; // 사용자 계정 비밀번호
			
			// 커넥션 생성
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);
			
			conn.setAutoCommit(false); // 자동 커밋 비활성화
			// 활성화상태일 경우 SQL이 수행되자마자 COMMIT 되어버림
			
			String sql = "DELETE FROM EMPLOYEE_COPY WHERE EMP_ID = ?";
			
			// PreparedStatement 생성(SQL 적재)
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더에 알맞은 값 대입
			pstmt.setInt(1, input);
			
			result = pstmt.executeUpdate();
			
			if(result > 0) conn.commit();
			else		   conn.rollback(); // 성공하면 커밋, 아니라면 롤백
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/** 6. 사번으로 사원 정보 수정 DAO (PreparedStatement)
	 * @param emp
	 * @return
	 */
	public int updateEmployee(Employee emp) {

		int result = 0; // 결과 저장용 변수

		try {

			// oracle jdbc driver 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 메모리 로드

			// 커넥션 생성
			// conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","oyj","oyj1234");
			String type = "jdbc:oracle:thin:@"; 
			String ip = "localhost"; // 접속할 아이피
			String port = ":1521"; 
			String sid = ":xe"; // 접속할 DB 이름
			String user = "oyj"; // 사용자 계정명
			String pw = "oyj1234"; // 사용자 계정 비밀번호
			conn = DriverManager.getConnection(type + ip + port + sid, user, pw);

			// 자동 커밋 비활성
			conn.setAutoCommit(false); 

			// sql 작성 (위치 홀더 포함)
			String sql ="UPDATE EMPLOYEE2 SET EMAIL = ?, PHONE = ?, SALARY =? WHERE EMP_ID =? ";

			// PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);

			// 위치 홀더에 알맞은 값 대입
			pstmt.setString(1, emp.getEmail());
			pstmt.setString(2, emp.getPhone());

			// setString()을 통해 위치홀더에 문자열 값을 대입하면
			// 문자열 양쪽에 ''(홑따옴표)가 포함된 상태로 추가!

			// ex) pstmt.setString(1, "user");
			//			--> 위치 홀더 자리 'user'
			// setInt() 는 '' 붙지 않음
			pstmt.setInt(3, emp.getSalary());
			pstmt.setInt(4, emp.getEmpId());

			// SQL 수행
			result = pstmt.executeUpdate();

			// 트랜잭션 제어
			if(result > 0) conn.commit(); // DML 성공 시 commit 수행
			else		   conn.rollback(); // DML 실패 시 rollback 수행

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/** 6. 사번으로 사원 정보 수정2 DAO (Statement)
	 * @param emp
	 * @return
	 */
	public int updateEmployee2(Employee emp) {

		int result = 0;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 커넥션 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","oyj","oyj1234");

			// 자동 커밋 비활성화
			conn.setAutoCommit(false);

			// SQL 작성(위치홀더 포함) // 문자열 데이터 양쪽에 홑따옴표 주의!!
			String sql ="UPDATE EMPLOYEE_COPY SET EMAIL = '" + emp.getEmail() + 
						"' , PHONE = '" + emp.getPhone() +
						"' , SALARY = " + emp.getSalary() + 
						" WHERE EMP_ID = " + emp.getEmpId();
			// WHERE 앞에 한 칸 띄우기

			// Statement 객체 생성
			stmt = conn.createStatement();

			// SQL 수행
			result = stmt.executeUpdate(sql);

			// 트랜잭션 제어
			if(result >0) conn.commit();
			else		  conn.rollback();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) stmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	/** 부서의 보너스를 모두 수정 DAO (Statement)
	 * @param emp
	 * @return
	 */
	public int updateBonus(Employee emp) {

		int result = 0;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 커넥션 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","oyj","oyj1234");

			// 자동 커밋 비활성화
			conn.setAutoCommit(false);

			// SQL 작성(위치홀더 포함) 
			// 문자열 데이터 양쪽에 홑따옴표 주의!!
			String sql = "UPDATE EMPLOYEE_COPY SET BONUS = " + 	emp.getBonus() + 
					" WHERE DEPT_CODE = '" + emp.getDeptCode() + "'";

			stmt = conn.createStatement();

			result = stmt.executeUpdate(sql);

			if(result > 0) conn.commit();
			else		   conn.rollback();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (stmt != null) stmt.close();
				if (conn != null) conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}


	/** 부서의 보너스를 모두 수정 DAO2 (PreparedStatement)
	 * @param emp
	 * @return
	 */
	public int updateBonus2(Employee emp) {

		int result = 0;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 커넥션 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","oyj","oyj1234");

			// 자동 커밋 비활성화
			conn.setAutoCommit(false);

			// SQL 작성(위치홀더 포함) // 문자열 데이터 양쪽에 홑따옴표 주의!!
			String sql = "UPDATE EMPLOYEE_COPY SET BONUS = ? WHERE DEPT_CODE =?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setDouble(1, emp.getBonus());
			pstmt.setString(2, emp.getDeptCode());

			result = pstmt.executeUpdate();

			if (result > 0) conn.commit();
			else			conn.rollback();


		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {

				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
