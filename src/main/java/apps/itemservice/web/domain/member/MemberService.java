package apps.itemservice.web.domain.member;

import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.member.Team;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

//@Service
@Transactional  //스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작하고, 메서드가 정상 종료되면 트랜잭션을 커밋한다. 만약 런타임 예외가 발생하면 롤백한다.
public class MemberService {

    //@Autowired
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member, Team team) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member, team);
        return member.getSeq();
    }
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public Team saveTeam(Team team, Member member) {
        return memberRepository.saveTeam(team, member);
    }

    public Optional<Team> findByTeam(String id) { return memberRepository.findByTeam(id); }
}
