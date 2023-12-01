package apps.itemservice.repository.member;

import apps.itemservice.core.trace.LogTrace;
import apps.itemservice.core.trace.TraceStatus;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberRepositoryProxy implements MemberRepository {
    private final MemberRepository target;
    private final LogTrace logTrace;
    @Override
    public Member save(Member member, Team team) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.save()");
            //target 호출
            target.save(member, team);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.findById()");
            //target 호출
            Optional<Member> result = target.findById(id);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.findByName()");
            //target 호출
            Optional<Member> result = target.findByName(name);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public List<Member> findAll() {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.findAll()");
            //target 호출
            List<Member> result = target.findAll();
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.findByLoginId()");
            //target 호출
            Optional<Member> result = target.findByLoginId(loginId);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public void remove(Long id) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.remove()");
            //target 호출
            target.remove(id);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Team saveTeam(Team team, Member member) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.saveTeam()");
            //target 호출
            Team result = target.saveTeam(team, member);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Optional<Team> findByTeam(String id) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberRepository.findByTeam()");
            //target 호출
            Optional<Team> result = target.findByTeam(id);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
