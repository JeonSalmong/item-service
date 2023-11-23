package apps.itemservice.domain.entity.member;

import apps.itemservice.domain.entity.base.BaseEntity;
import apps.itemservice.domain.entity.order.Orders;
import apps.itemservice.domain.entity.valueType.Address;
import apps.itemservice.domain.vo.RoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(name = "SEQ_ID_UNIQUE", columnNames = {"MEMBER_SEQ", "MEMBER_ID"})})
//@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "데이터베이스시퀀스명", initialValue = 1, allocationSize = 1)
@ToString(exclude = "team") //SLF4J: Failed toString() invocation on an object of type 오류 해결을 위해 추가
@Data
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")   // DB sequence
    private Long seq;

    @NotEmpty
    @Column(name = "MEMBER_ID")
    private String id; //로그인 ID

//    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 10)
    private String name; //사용자 이름

    @NotEmpty
    //@Transient
    @Column(name = "PASSWORD")
    private String password;

    private int age;

    //@Transient
    @Column(name = "LOGIN_DATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE")
    private RoleType roleType;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;

    @Lob
    private String description;

    @Embedded
    private Address address;    //value type

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")       // DB의 FK에 해당 함
    private Team team;

   // 연관관계 편의 메소드
    public void setTeam(Team team) {
        //기존 팀과 관계를 제거
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);    //객체 관점의 양방향성 설정
    }

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<Orders>();

    public Member() {
    }

    public Member(String loginId, String password, String name) {
        this.id = loginId;
        this.name = name;
        this.password = password;
        this.loginDateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Member{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
