package apps.itemservice.service.item;

import apps.itemservice.core.trace.LogTrace;
import apps.itemservice.core.trace.TraceStatus;
import apps.itemservice.domain.entity.item.Item;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemServiceProxy implements ItemService {
    private final ItemService target;
    private final LogTrace logTrace;

    @Override
    public Item save(Item item) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemService.save()");
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
            status = logTrace.begin("ItemService.findById()");
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
            status = logTrace.begin("ItemService.findAll()");
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
            status = logTrace.begin("ItemService.update()");
            //target 호출
            target.update(itemId, updateParam);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public int getItemStock(Long id) {
        TraceStatus status = null;
        int cnt = 0;
        try {
            status = logTrace.begin("ItemService.getItemStock()");
            //target 호출
            cnt = target.getItemStock(id);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return cnt;
    }
}
