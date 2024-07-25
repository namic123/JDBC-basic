package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

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

    public Member findById(String memberId) throws SQLException {
        // 특정 회원 ID로 회원 정보를 조회하는 SQL 쿼리
        String sql = "select * from member where member_id = ?";

        Connection con = null; // 데이터베이스 연결을 위한 Connection 객체
        PreparedStatement psmt = null; // SQL 문을 실행하기 위한 PreparedStatement 객체
        ResultSet rs = null; // SQL 쿼리의 결과를 저장하기 위한 ResultSet 객체

        try {
            con = getConnection(); // 데이터베이스 연결 획득
            psmt = con.prepareStatement(sql); // SQL 쿼리를 미리 컴파일하여 PreparedStatement 객체 생성
            psmt.setString(1, memberId); // 첫 번째 매개변수에 memberId 값 설정

            rs = psmt.executeQuery(); // SQL 쿼리 실행 및 결과 집합(ResultSet) 반환

            if (rs.next()) { // 결과 집합의 다음 행이 존재하는지 확인
                Member member = new Member(); // 새로운 Member 객체 생성
                member.setMemberId(rs.getString("member_id")); // 결과 집합에서 member_id 값을 가져와 설정
                member.setMoney(rs.getInt("money")); // 결과 집합에서 money 값을 가져와 설정
                return member; // 조회된 회원 객체 반환
            } else {
                // 결과 집합에 행이 없으면 예외 발생
                throw new NoSuchElementException("member not found memberId= " + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e); // 데이터베이스 오류 발생 시 로그 출력
            throw e; // 예외 다시 던짐
        } finally {
            close(con, psmt, rs); // 리소스 해제
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
