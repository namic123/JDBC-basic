package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    // 회원 정보를 DB에 저장하는 메서드
    public Member save(Member member) throws SQLException {
        // SQL 쿼리: member 테이블에 member_id와 money 값을 삽입
        String sql = "insert into member (member_id, money) values (?, ?)";

        Connection con = null; // DB 연결을 위한 Connection 객체
        PreparedStatement psmt = null;  // SQL 문을 실행하기 위한 PreparedStatement 객체

        try {
            con = getConnection();  // DB 연결 획득
            psmt = con.prepareStatement(sql);   // SQL 쿼리를 미리 컴파일하여 PreparedStatement 객체 생성
            psmt.setString(1, member.getMemberId());    // 1번째 매개변수에 member_id 값 설정
            psmt.setInt(2, member.getMoney());  // 2번째 매개변수에 money 값 설정
            psmt.executeUpdate();   // SQL 쿼리 실행 (데이터베이스에 변경 사항 적용)
            return member;  // 저장된 회원 정보 반환
        } catch (SQLException e) {
            log.error("db error", e);       // 데이터베이스 오류 발생 시 오류 출력
            throw e;    // 예외 던짐
        } finally {
            close(con, psmt, null);     // 리소스 해제
        }

    }

    // Connection, Statement, ResultSet 객체를 닫는 메서드
    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) { // ResultSet 객체가 null이 아닌 경우
            try {
                rs.close(); // ResultSet 객체 닫기
            } catch (SQLException e) {
                log.error("db error", e); // 닫는 도중 오류 발생 시 로그 출력
            }
        }

        if (stmt != null) { // Statement 객체가 null이 아닌 경우
            try {
                stmt.close(); // Statement 객체 닫기
            } catch (SQLException e) {
                log.error("db error", e); // 닫는 도중 오류 발생 시 로그 출력
            }
        }
        if (con != null) { // Connection 객체가 null이 아닌 경우
            try {
                con.close(); // Connection 객체 닫기
            } catch (SQLException e) {
                log.error("db error", e); // 닫는 도중 오류 발생 시 로그 출력
            }
        }
    }

    // DB 연결을 반환하는 메서드
    private Connection getConnection() {
        // DBConnectionUtil을 사용하여 Connection 객체 반환
        return DBConnectionUtil.getConnection();
    }

}
