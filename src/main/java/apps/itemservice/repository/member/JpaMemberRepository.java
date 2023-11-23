package apps.itemservice.repository.member;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Team saveTeam(Team team, Member member) {
        // 연관관계 설정 테스트
        // 팀 저장
        member.setTeam(team);
        em.persist(team);
        return team;
    }

    @Override
    public Member save(Member member, Team team) {
        member.setTeam(team);  //연관관계 설정 member -> team
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long seq) {
        Member member = em.find(Member.class, seq);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        // JPQL 엔티티객체 대상 쿼리, 클래스와 필드 대상 쿼리 (DB 테이블대상 쿼리가 아님)
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        //String jpql = "select m from Member m";
        //List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        //객체조인
        String jpql = "select m from Member m join m.team t where t.name = :teamName";
        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1")
                .getResultList();

        for (Member member : resultList) {
            log.info("[객체조인] member.username = " + member.getName());
            log.info("[객체조인] teamname = " + member.getTeam().getName());
        }

        //객체그래프탐색 (팀 -> 회원)
        Team team = em.find(Team.class, "team1");
        List<Member> members = team.getMembers();

        for (Member member : members) {
            log.info("[객체그래프탐색] member.username = " + member.getName());
            log.info("[객체그래프탐색] teamname = " + team.getName());
        }

        return resultList;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getId().equals(loginId))
                .findFirst();
    }

    @Override
    public Optional<Team> findByTeam(String id) {
        Team Team = em.find(Team.class, id);
        return Optional.ofNullable(Team);
    }

    @Override
    public void remove(Long id) {
        Member member = em.find(Member.class, id);  //삭제 대상 엔티티 조회
        em.remove(member);  //엔티티삭제
    }

}
