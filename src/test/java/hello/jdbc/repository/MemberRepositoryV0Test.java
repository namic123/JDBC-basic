package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
       Member member = new Member("memberV0", 10000);
        memberRepository.save(member);
    }

    @Test
    void findByMemberId() throws SQLException {
        Member member = new Member("memberV0", 10000);
        Member findMember = memberRepository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    void updateByMemberId() throws SQLException {
        Member member = new Member("memberV0", 20000);
        memberRepository.update(member.getMemberId(), member.getMoney());
        Assertions.assertThat(memberRepository.findById(member.getMemberId())).isEqualTo(member);
    }

    @Test
    void deleteByMemberId() throws SQLException {
        Member member = new Member("memberV0", 20000);
        memberRepository.deleteById(member.getMemberId());
        Assertions.assertThatThrownBy(()-> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}