package apps.itemservice.repository.member;

import apps.itemservice.domain.entity.member.Team;
import lombok.extern.slf4j.Slf4j;
import apps.itemservice.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용
    @Override
    public Member save(Member member, Team team) {
        member.setSeq(++sequence);
        log.info("save: member={}", member);
        store.put(member.getSeq(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long seq) {
        return Optional.ofNullable(store.get(seq));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return findAll().stream()
                .filter(m -> m.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getId().equals(loginId))
                .findFirst();
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Team saveTeam(Team team, Member member) {
        return null;
    }

    public void clearStore() {
        store.clear();
    }

    @Override
    public Optional<Team> findByTeam(String id) {
        return Optional.empty();
    }
}
