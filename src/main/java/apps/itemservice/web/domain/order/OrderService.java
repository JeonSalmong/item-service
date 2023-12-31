package apps.itemservice.web.domain.order;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.config.aop.trace.template.AbstractTemplate;
import apps.itemservice.config.aop.trace.template.TraceTemplate;
import apps.itemservice.web.domain.entity.delivery.Delivery;
import apps.itemservice.web.domain.entity.item.Item;
import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.order.OrderItem;
import apps.itemservice.web.domain.entity.order.OrderSearch;
import apps.itemservice.web.domain.entity.order.Orders;
import apps.itemservice.web.vo.DeliveryStatus;
import apps.itemservice.web.domain.member.MemberRepository;
import apps.itemservice.web.domain.item.ItemService;
import apps.itemservice.web.vo.ActuatorTags;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Timed(ActuatorTags.ORDER_TIMED)
@Service    // 자동스캔 annotation, 선언하지 않으면 config에 Bean 등록 해서  줘야 함
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemService itemService;
    private final MeterRegistry registry;
    private final LogTrace trace;
    private final TraceTemplate template;


    public OrderService(OrderRepository orderRepository
            , MemberRepository memberRepository
            , ItemService itemService
            , MeterRegistry registry
            , LogTrace trace
            , TraceTemplate template) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemService = itemService;
        this.registry = registry;
        this.trace = trace;
        this.template = template;
    }

    /** 주문 */
    @Counted(ActuatorTags.ORDER_COUNTED)    //tag에 method 기준으로 분류해서 적용 됨
    public Long order(Long memberId, Long itemId, int count) {

        AbstractTemplate<Long> template = new AbstractTemplate<Long>(trace) {
            @Override
            protected Long call() {
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
        };
        return template.execute("OrderService.order()");
    }

    /** 주문 취소 */
    @Counted(ActuatorTags.ORDER_COUNTED)
    public void cancelOrder(Long orderId) {

        template.execute("OrderService.cancelOrder()", () -> {
            //주문 엔티티 조회
            Orders order = orderRepository.fineOne(orderId);

            //주문 취소
            order.cancel();

//        Counter.builder("my.order")
//                .tag("class", this.getClass().getName())
//                .tag("method", "cancel")
//                .description("order")
//                .register(registry).increment();
            return null;
        });
    }

    /** 주문 검색 */
    public Orders fineOne(Long orderId) {
        return orderRepository.fineOne(orderId);
    }

    /** 주문 검색 */
    public List<Orders> findOrders(OrderSearch orderSearch) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.findOrders()");
            List<Orders> orders = new ArrayList<>();
            orders = orderRepository.findAll(orderSearch);
            trace.end(status);
            return orders;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    /** 배송 상태 변경 */
    public void changeDeli(Long orderId, String status) {
        //주문 엔티티 조회
        Orders order = orderRepository.fineOne(orderId);

        order.getDelivery().setStatus(DeliveryStatus.COMP);

    }

}
