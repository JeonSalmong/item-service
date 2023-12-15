package apps.itemservice.web.domain.item;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.web.domain.entity.item.Item;
import apps.itemservice.web.dto.ItemSaveForm;
import apps.itemservice.web.dto.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@RequiredArgsConstructor
public class ItemControllerProxy implements ItemController {
    private final ItemController target;
    private final LogTrace logTrace;
    @Override
    public String items(Model model) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.items()");
            //target 호출
            String result = target.items(model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String item(long itemId, Model model) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.item()");
            //target 호출
            String result = target.item(itemId, model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String addForm(Model model) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.addForm()");
            //target 호출
            String result = target.addForm(model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String addItemV1(String itemName, int price, Integer quantity, Model model) {
        return null;
    }

    @Override
    public String addItemV2(Item item, Model model) {
        return null;
    }

    @Override
    public String addItemV3(Item item) {
        return null;
    }

    @Override
    public String addItemV4(Item item) {
        return null;
    }

    @Override
    public String addItemV5(Item item) {
        return null;
    }

    @Override
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.addItemV6()");
            //target 호출
            String result = target.addItemV6(item, redirectAttributes);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String addItemValidV1(Item item, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    @Override
    public String addItemValidV2(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    @Override
    public String addItemValidV2_2(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    @Override
    public String addItemValidV3(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    @Override
    public String addItemValidV4(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        return null;
    }

    @Override
    public String addItemValidV5(ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.addItemValidV5()");
            //target 호출
            String result = target.addItemValidV5(form, bindingResult, redirectAttributes, model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String editForm(Long itemId, Model model) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.editForm()");
            //target 호출
            String result = target.editForm(itemId, model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String edit(Long itemId, Item item) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.edit()");
            //target 호출
            String result = target.edit(itemId, item);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String editV1(Long itemId, Item item, BindingResult bindingResult) {
        return null;
    }

    @Override
    public String editV2(Long itemId, ItemUpdateForm form, BindingResult bindingResult) throws IOException {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.editV2()");
            //target 호출
            String result = target.editV2(itemId, form, bindingResult);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public Resource downloadImage(String filename) throws MalformedURLException {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.downloadImage()");
            //target 호출
            Resource result = target.downloadImage(filename);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Resource> downloadAttach(Long itemId) throws MalformedURLException {
        TraceStatus status = null;
        try {
            status = logTrace.begin("ItemController.downloadAttach()");
            //target 호출
            ResponseEntity<Resource> result = target.downloadAttach(itemId);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
