package apps.itemservice.service.order;

import apps.itemservice.domain.entity.delivery.Delivery;
import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.order.OrderItem;
import apps.itemservice.domain.entity.order.OrderSearch;
import apps.itemservice.domain.entity.order.Orders;
import apps.itemservice.domain.vo.DeliveryStatus;
import apps.itemservice.repository.member.MemberRepository;
import apps.itemservice.repository.order.OrderRepository;
import apps.itemservice.service.item.ItemService;
import apps.itemservice.service.vo.ActuatorTags;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Timed(ActuatorTags.ORDER_TIMED)
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemService itemService;
    private final MeterRegistry registry;

    String packageName = getClass().getPackage().getName();

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemService itemService, MeterRegistry registry) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemService = itemService;
        this.registry = registry;
    }

    /** 주문 */
    @Counted(ActuatorTags.ORDER_COUNTED)    //tag에 method 기준으로 분류해서 적용 됨
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Optional<Member> member = memberRepository.findById(memberId);
        Item item = itemService.findById(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery(member.get().getAddress());
        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //주문 생성
        Orders order = Orders.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

//        Counter.builder("my.order")
//                .tag("class", this.getClass().getName())
//                .tag("method", "order")
//                .description("order")
//                .register(registry).increment();
        return order.getId();
    }

    /** 주문 취소 */
    @Counted(ActuatorTags.ORDER_COUNTED)
    public void cancelOrder(Long orderId) {

        //주문 엔티티 조회
        Orders order = orderRepository.fineOne(orderId);

        //주문 취소
        order.cancel();

//        Counter.builder("my.order")
//                .tag("class", this.getClass().getName())
//                .tag("method", "cancel")
//                .description("order")
//                .register(registry).increment();
    }

    /** 주문 검색 */
    public Orders fineOne(Long orderId) {
        return orderRepository.fineOne(orderId);
    }

    /** 주문 검색 */
    public List<Orders> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

    /** 배송 상태 변경 */
    public void changeDeli(Long orderId, String status) {
        //주문 엔티티 조회
        Orders order = orderRepository.fineOne(orderId);

        order.getDelivery().setStatus(DeliveryStatus.COMP);

    }

}
