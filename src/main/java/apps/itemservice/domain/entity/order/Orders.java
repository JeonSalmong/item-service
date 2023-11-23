package apps.itemservice.domain.entity.order;

import apps.itemservice.domain.entity.base.BaseEntity;
import apps.itemservice.domain.entity.delivery.Delivery;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.vo.DeliveryStatus;
import apps.itemservice.domain.vo.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// 복합키의 복잡도를 해결하기 위한 대리키 사용 중간 연결 객체
@Entity
@Table(name = "ORDERS")
@Data
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //지연로딩
    @JoinColumn(name = "MEMBER_SEQ")
    private Member member;      //Member FK

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //**
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;  //배송정보
    public void setDelivery(Delivery delivery) {    //==연관관계 메서드==//
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();
    public void addOrderItem(OrderItem orderItem) { //==연관관계 메서드==//
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    private Date orderDate;     //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문상태

    //==생성 메서드==//
    public static Orders createOrder(Optional<Member> member, Delivery delivery, OrderItem... orderItems) {

        Orders order = new Orders();
        order.setMember(member.get());
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(new Date());
        return order;
    }

    //==비즈니스 로직==//
    /** 주문 취소 */
    public void cancel() {

        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new RuntimeException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /** 전체 주문 가격 조회 */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
