package apps.itemservice.web.domain.order;

import apps.itemservice.web.domain.entity.order.OrderSearch;
import apps.itemservice.web.domain.entity.order.Orders;

import java.util.List;

public interface OrderRepository {

    Orders save(Orders order);

    Orders fineOne(Long id);

    List<Orders> findAll(OrderSearch orderSearch);
}
