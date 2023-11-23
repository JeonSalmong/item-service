package apps.itemservice.member;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import apps.itemservice.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //스프링 컨테이너와 테스트를 함께 실행한다.
@Transactional  //테스트 시작 전에 트랜잭션을 시작하고,테스트 완료 후에 항상 롤백한
public class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");

        Member member1 = new Member();
        member1.setId("test1");
        member1.setName("hello1");
        member1.setPassword("1234");

        Member member2 = new Member();
        member2.setId("test2");
        member2.setName("hello2");
        member2.setPassword("1234");

        //When
        Long saveId = memberService.join(member1, team1);
        //Long saveId2 = memberService.join(member2, team1);

        memberService.saveTeam(team1, member1);

        //Then
        Optional<Member> findMember = memberService.findOne(saveId);
        List<Member> findAll = memberService.findMembers();
        assertEquals(member1.getName(), findMember.get().getName());
    }
    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");
        //When
        memberService.join(member1, null);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2, null));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
