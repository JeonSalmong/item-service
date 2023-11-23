package apps.itemservice.web.controller.order;

import apps.itemservice.core.exception.NotEnoughStockException;
import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.order.OrderSearch;
import apps.itemservice.domain.entity.order.Orders;
import apps.itemservice.service.item.ItemService;
import apps.itemservice.service.member.MemberService;
import apps.itemservice.service.order.OrderService;
import apps.itemservice.web.controller.dto.ItemSaveForm;
import apps.itemservice.web.controller.dto.MessageDto;
import apps.itemservice.web.controller.dto.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/add")
    public String order(@ModelAttribute("order") OrderForm order, Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping("/add")
    public String addOrder(@Validated @ModelAttribute("order") OrderForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws IOException {
        Item item = itemService.findById(form.getItemId());
        try {
            orderService.order(form.getMemberId(), form.getItemId(), form.getCount());
        } catch (NotEnoughStockException e) {
            //bindingResult.addError(new ObjectError("order", "재고량 초과 주문 할 수 없습니다. 현재고 = " + item.getQuantity()));
            bindingResult.addError(new FieldError("order", "count", item.getQuantity(), false, null, null, "재고량 초과 주문 할 수 없습니다. 현재고 = " + item.getQuantity()));
            return "order/orderForm";
        }


        return "redirect:/order/orders";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        List<Orders> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @RequestMapping(value = "/orders/{orderId}/cancel")
    public String processCancelBuy(@PathVariable("orderId") Long orderId, Model model) {

        try {
            orderService.cancelOrder(orderId);
        } catch (RuntimeException e) {
            MessageDto message = new MessageDto(e.getMessage(), "/order/orders", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        return "redirect:/order/orders";
    }

    @RequestMapping(value = "/orders/{orderId}/changeDeli")
    public String processChangeDeli(@PathVariable("orderId") Long orderId, String status) {

        orderService.changeDeli(orderId, status);

        return "redirect:/order/orders";
    }

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }
}
