package apps.itemservice.web.domain.member;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.member.Team;

import java.util.List;
import java.util.Optional;

public class MemberServiceProxy extends MemberService {
    private final MemberService target;
    private final LogTrace logTrace;
    public MemberServiceProxy(MemberService target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Long join(Member member, Team team) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberService.save()");
            //target 호출
            Long result = target.join(member, team);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Optional<Member> findOne(Long id) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberService.findOne()");
            //target 호출
            Optional<Member> result = target.findOne(id);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public List<Member> findMembers() {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberService.findMembers()");
            //target 호출
            List<Member> result = target.findMembers();
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
            status = logTrace.begin("MemberService.findByLoginId()");
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
    public Team saveTeam(Team team, Member member) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberService.saveTeam()");
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
            status = logTrace.begin("MemberService.findByTeam()");
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
