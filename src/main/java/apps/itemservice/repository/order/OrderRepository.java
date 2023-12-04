package apps.itemservice.repository.order;

import apps.itemservice.domain.entity.order.OrderSearch;
import apps.itemservice.domain.entity.order.Orders;

import java.util.List;

public interface OrderRepository {

    Orders save(Orders order);

    Orders fineOne(Long id);

    List<Orders> findAll(OrderSearch orderSearch);
}
