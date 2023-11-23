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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemService itemService;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemService = itemService;
    }

    /** 주문 */
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
        return order.getId();
    }

    /** 주문 취소 */
    public void cancelOrder(Long orderId) {

        //주문 엔티티 조회
        Orders order = orderRepository.fineOne(orderId);

        //주문 취소
        order.cancel();
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
