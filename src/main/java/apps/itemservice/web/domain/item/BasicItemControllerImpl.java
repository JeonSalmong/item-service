package apps.itemservice.web.domain.item;

import apps.itemservice.web.domain.entity.item.ItemFile;
import lombok.extern.slf4j.Slf4j;
import apps.itemservice.web.domain.entity.item.Item;
import apps.itemservice.platform.file.FileStore;
import apps.itemservice.web.dto.ItemSaveForm;
import apps.itemservice.web.dto.ItemUpdateForm;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@RequiredArgsConstructor    //final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
public class BasicItemControllerImpl implements ItemController {

//    private final MemoryItemRepository memoryItemRepository;    //final 키워드를 빼면 안된다!, 그러면 ItemRepository 의존관계 주입이 안된다.
    private final ItemService itemService;
    private final FileStore fileStore;

    public BasicItemControllerImpl(ItemService itemService, FileStore fileStore) {
        this.itemService = itemService;
        this.fileStore = fileStore;
    }

    @Override
    public String items(Model model) {
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item/items";
    }

    @Override
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        return "item/item";
    }

    /**
     * 상품등록 폼
     * @return
     */
    @Override
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "item/addForm";
    }

    /**
     * 상품등록 처리
     * @param itemName
     * @param price
     * @param quantity
     * @param model
     * @return
     */
    @Override
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemService.save(item);
        model.addAttribute("item", item);
        return "item/item";
    }

    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
    @Override
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemService.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "item/item";
    }

    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
    @Override
    public String addItemV3(@ModelAttribute Item item) {
        itemService.save(item);
        return "item/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
    @Override
    public String addItemV4(Item item) {
        itemService.save(item);
        return "item/item";
    }

    /**
     * 새로고침시 등록 방지
     * PRG - Post/Redirect/Get
     */
    @Override
    public String addItemV5(Item item) {
        itemService.save(item);
        return "redirect:/item/items/" + item.getId();
    }

    /**
     * RedirectAttributes
     */
    @Override
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("etc", "한글");
        return "redirect:/item/items/{itemId}";
    }

    /**
     * 검증 직접 처리 V1
     * @param item
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV1(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
        //검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
        }
        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "/item/addForm";
        }
        //성공 로직
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * BindingResult V2-1
     * @param item
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // 사용자 입력값 유지하지 못 함
            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            // 사용자 입력값 유지
            //bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
            // 오류 메시지 적용
            //bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
            // 단순화
            bindingResult.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            //bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
            //bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new String[]{"1,000", "1,000,000"}, null));
            bindingResult.rejectValue("price", "range", new String[]{"1,000", "1,000,000"}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            //bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
            //bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
            //bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new String[]{"9,999"}, null));
            bindingResult.rejectValue("quantity", "max", new String[]{"9,999"}, null);
        }
        //특정 필드가 아닌 복합 룰 (전체 예외) 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                //bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
                //bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
                //bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/addForm";
        }
        //성공 로직
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }

    //private final ItemValidator itemValidator;
    /**
     * ItemValidator class 적용 V2-2
     * @param item
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV2_2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //itemValidator.validate(item, bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/addForm";
        }
        //성공 로직
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * @Validated 적용 V3
     * @param item
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV3(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/addForm";
        }
        //성공 로직
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * Bean Validator 적용 V4
     * @param item
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV4(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/addForm";
        }
        //성공 로직
        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * Bean Validator 적용 V5 - Save, Update 시 분기 처리
     * HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository
     * @param form
     * @param redirectAttributes
     * @param model
     * @return
     */
    @Override
    public String addItemValidV5(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {

        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/addForm";
        }
        //성공 로직
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        //파일 저장 부분 추가
        ItemFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<ItemFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);

        Item savedItem = itemService.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/item/items/{itemId}";
    }


    /**
     * 상품 수정 폼
     * @param itemId
     * @param model
     * @return
     */
    @Override
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        return "item/editForm";
    }

    /**
     * 상품 수정 처리
     * @param itemId
     * @param item
     * @return
     */
    @Override
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemService.update(itemId, item);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * 상품 수정 처리 - Bean Validation 적용
     * @param itemId
     * @param item
     * @return
     */
    @Override
    public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/editForm";
        }

        itemService.update(itemId, item);
        return "redirect:/item/items/{itemId}";
    }

    /**
     * 상품 수정 처리 - Bean Validation 적용 (Save, Update 시 분기 처리)
     * HTML Form -> ItemUpdateForm -> Controller -> Item 생성 -> Repository
     * @param itemId
     * @param form
     * @return
     */
    @Override
    public String editV2(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) throws IOException {

        //특정 필드가 아닌 복합 룰 검증
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "/item/editForm";
        }

        //성공 로직
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        //파일 저장 부분 추가
        ItemFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<ItemFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);

        itemService.update(itemId, item);
        return "redirect:/item/items/{itemId}";
    }

    @Override
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @Override
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemService.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
