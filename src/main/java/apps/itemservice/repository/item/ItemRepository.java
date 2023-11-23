package apps.itemservice.repository.item;

import apps.itemservice.domain.entity.item.Item;

import java.util.List;

public interface ItemRepository {

    Item save(Item item);
    Item findById(Long id);
    List<Item> findAll();
    void update(Long itemId, Item updateParam);

}
