package apps.itemservice.web.domain.member;

import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.member.Team;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member, Team team);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
    Optional<Member> findByLoginId(String loginId);
    void remove(Long id);
    Team saveTeam(Team team, Member member);

    Optional<Team> findByTeam(String id);
}
