package apps.itemservice.service.item;

import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.repository.item.ItemRepository;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
//@Service
public class ItemService {

    //private final ItemRepository itemRepository = new MemoryItemRepository();
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item save(Item item) {
        itemRepository.save(item);
        return item;
    }

    public Item findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void update(Long itemId, Item updateParam) {
        itemRepository.update(itemId, updateParam);
    }

    public int getItemStock(Long id) {
        return itemRepository.findById(id).getQuantity();
    }
}
