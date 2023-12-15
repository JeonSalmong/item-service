package apps.itemservice.web.domain.item;

import apps.itemservice.web.domain.entity.item.Item;

import java.util.List;

public interface ItemService {
    Item save(Item item);
    Item findById(Long id);
    List<Item> findAll();
    void update(Long itemId, Item updateParam);
    int getItemStock(Long id);
}
