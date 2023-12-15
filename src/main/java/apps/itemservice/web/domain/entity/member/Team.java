package apps.itemservice.web.domain.entity.member;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString(exclude = "members")
@Data
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;

    private String name;

    @OneToMany(mappedBy = "team")   //연관관계(FK)의 주인이 아님을 선언, DB의 FK 주인은 FK 컬럼을 가지고 있는 테이블이 관리하므로 해당 테이블이 주인임.
                                    //즉 멤버는 팀이 변경될 수 있지만, 팀은 해당 팀의 멤버만 조회(읽기)할 수 있다.
                                    //일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하자
    private List<Member> members = new ArrayList<Member>();

    // 연관관계 편의 메소드
    public void addMember(Member member) {
        this.members.add(member);
        if (member.getTeam() != this) { //무한루프에 빠지지 않도록 체크
            member.setTeam(this);
        }
    }
}
