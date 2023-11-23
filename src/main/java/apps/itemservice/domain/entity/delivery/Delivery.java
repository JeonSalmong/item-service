package apps.itemservice.domain.entity.delivery;

import apps.itemservice.domain.entity.order.Orders;
import apps.itemservice.domain.entity.valueType.Address;
import apps.itemservice.domain.vo.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DELIVERY")
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DELIVERY_ID")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Orders order;

//    private String city;
//    private String street;
//    private String zipcode;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]
    public Delivery() {
    }

    public Delivery(Address address) {
        this.address = address;
        this.status = DeliveryStatus.READY;
    }
}
