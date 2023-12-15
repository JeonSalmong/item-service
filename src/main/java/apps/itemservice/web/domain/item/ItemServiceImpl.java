package apps.itemservice.web.domain.item;

import apps.itemservice.web.domain.entity.item.Item;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
//@Service
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public void update(Long itemId, Item updateParam) {
        itemRepository.update(itemId, updateParam);
    }

    @Override
    public int getItemStock(Long id) {
        return itemRepository.findById(id).getQuantity();
    }
}
