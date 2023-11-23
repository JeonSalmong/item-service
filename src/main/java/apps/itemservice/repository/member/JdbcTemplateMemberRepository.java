package apps.itemservice.repository.member;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);;
    }

    @Override
    public Member save(Member member, Team team) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("seq");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setSeq(key.longValue());
        return member;

    }

    @Override
    public Optional<Member> findById(Long seq) {
        List<Member> result = jdbcTemplate.query("select * from member where seq = ?", memberRowMapper(), seq);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getId().equals(loginId))
                .findFirst();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setSeq(rs.getLong("seq"));
            member.setName(rs.getString("name"));
            return member;
        };
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Team saveTeam(Team team, Member member) {
        return null;
    }

    @Override
    public Optional<Team> findByTeam(String id) {
        return Optional.empty();
    }
}
