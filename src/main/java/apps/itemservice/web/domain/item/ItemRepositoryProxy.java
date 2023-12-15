package apps.itemservice.web.domain.item;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.web.domain.entity.item.Item;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemRepositoryProxy implements ItemRepository {
    private final ItemRepository target;
    private final LogTrace logTrace;
    @Override
    public Item save(Item item) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemRepository.save()");
            //target 호출
            target.save(item);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return item;
    }

    @Override
    public Item findById(Long id) {
        TraceStatus status = null;
        Item item = null;
        try {
            status = logTrace.begin("ItemRepository.findById()");
            //target 호출
            item = target.findById(id);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return item;
    }

    @Override
    public List<Item> findAll() {
        TraceStatus status = null;
        List<Item> items = new ArrayList<>();
        try {
            status = logTrace.begin("ItemRepository.findAll()");
            //target 호출
            items = target.findAll();
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return items;
    }

    @Override
    public void update(Long itemId, Item updateParam) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemRepository.update()");
            //target 호출
            target.update(itemId, updateParam);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
