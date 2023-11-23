package apps.itemservice.domain.entity.order;

import apps.itemservice.domain.vo.OrderStatus;
import lombok.Data;

@Data
public class OrderSearch {
    private String memberName;      //회원 이름
    private OrderStatus orderStatus;//주문 상태
}
