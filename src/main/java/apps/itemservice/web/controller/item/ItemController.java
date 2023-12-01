package apps.itemservice.web.controller.item;

import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.web.controller.dto.ItemSaveForm;
import apps.itemservice.web.controller.dto.ItemUpdateForm;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@RequestMapping("/item/items")
public interface ItemController {
    @GetMapping
    String items(Model model);

    @GetMapping("/{itemId}")
    String item(@PathVariable long itemId, Model model);

    @GetMapping("/add")
    String addForm(Model model);

    //    @PostMapping("/add")
     String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model);
    //@PostMapping("/add")
    String addItemV2(@ModelAttribute("item") Item item, Model model);

    //@PostMapping("/add")
    String addItemV3(@ModelAttribute Item item);

    //@PostMapping("/add")
    String addItemV4(Item item);

    //@PostMapping("/add")
    String addItemV5(Item item);

    //@PostMapping("/add")
    String addItemV6(Item item, RedirectAttributes redirectAttributes);

    //@PostMapping("/add")
    String addItemValidV1(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model);
    //    @PostMapping("/add")
    String addItemValidV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model);
    //    @PostMapping("/add")
    String addItemValidV2_2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model);
    //@PostMapping("/add")
    String addItemValidV3(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model);

    //    @PostMapping("/add")
    String addItemValidV4(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model);
    @PostMapping("/add")
    String addItemValidV5(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException;

    @GetMapping("/{itemId}/edit")
    String editForm(@PathVariable Long itemId, Model model);
    //    @PostMapping("/{itemId}/edit")
    String edit(@PathVariable Long itemId, @ModelAttribute Item item);
    //    @PostMapping("/{itemId}/edit")
    String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult);
    @PostMapping("/{itemId}/edit")
    String editV2(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) throws IOException;

    @ResponseBody
    @GetMapping("/images/{filename}")
    Resource downloadImage(@PathVariable String filename) throws MalformedURLException;

    @GetMapping("/attach/{itemId}")
    ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException;
}
