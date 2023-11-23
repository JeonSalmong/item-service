package apps.itemservice.order;

import apps.itemservice.core.exception.NotEnoughStockException;
import apps.itemservice.domain.entity.item.Book;
import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import apps.itemservice.domain.entity.order.Orders;
import apps.itemservice.domain.entity.valueType.Address;
import apps.itemservice.domain.vo.OrderStatus;
import apps.itemservice.repository.order.OrderRepository;
import apps.itemservice.service.item.ItemService;
import apps.itemservice.service.member.MemberService;
import apps.itemservice.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest //스프링 컨테이너와 테스트를 함께 실행한다.
@Transactional  //테스트 시작 전에 트랜잭션을 시작하고,테스트 완료 후에 항상 롤백한
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;

    @Test
    public void 상품주문() throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;

        //When
        Long orderId = orderService.order(member.getSeq(), item.getId(), orderCount);

        //Then
        Orders getOrder = orderRepository.fineOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 주문(ORDER)이다.");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, item.getQuantity(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {

        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고

        int orderCount = 11; //재고 보다 많은 수량

        //When


        //Then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getSeq(), item.getId(), orderCount);
        });
    }

    @Test
    public void 주문취소() {

        //Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;

        Long orderId = orderService.order(member.getSeq(), item.getId(), orderCount);

        //When
        orderService.cancelOrder(orderId);

        //Then
        Orders getOrder = orderRepository.fineOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
        assertEquals(10, item.getQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    private Member createMember() {
        //Given
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");

        Member member1 = new Member();
        member1.setId("test1");
        member1.setName("hello1");
        member1.setPassword("1234");
        member1.setAddress(new Address("서울", "강가", "123-123"));

        Long saveId = memberService.join(member1, team1);
        memberService.saveTeam(team1, member1);

        return member1;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setItemName(name);
        book.setQuantity(stockQuantity);
        book.setPrice(price);
        itemService.save(book);
        return book;
    }
}
