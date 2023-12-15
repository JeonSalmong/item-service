package apps.itemservice.web.domain.entity.member;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker")
    private Member member;
}
