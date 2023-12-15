package apps.itemservice.web.domain.order;

import apps.itemservice.platform.exception.NotEnoughStockException;
import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.config.aop.trace.template.AbstractTemplate;
import apps.itemservice.config.aop.trace.template.TraceCallback;
import apps.itemservice.config.aop.trace.template.TraceTemplate;
import apps.itemservice.web.domain.entity.item.Item;
import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.order.OrderSearch;
import apps.itemservice.web.domain.entity.order.Orders;
import apps.itemservice.web.domain.item.ItemService;
import apps.itemservice.web.domain.member.MemberService;
import apps.itemservice.web.domain.pay.PayService;
import apps.itemservice.web.dto.MessageDto;
import apps.itemservice.web.dto.OrderForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final PayService payService;
    private final LogTrace trace;
    private final TraceTemplate traceTemplate;

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

        //익명내부클래스
        AbstractTemplate<String> template = new AbstractTemplate<String>(trace) {
            @Override
            protected String call() {
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
        };
        return template.execute("OrderController.addOrder()");
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.orderList()");
            List<Orders> orders = orderService.findOrders(orderSearch);
            model.addAttribute("orders", orders);
            trace.end(status);
            return "order/orderList";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; //예외를 꼭 다시 던져주어야 한다.
        }
    }

    @RequestMapping(value = "/orders/{orderId}/cancel")
    public String processCancelBuy(@PathVariable("orderId") Long orderId, Model model) {

        // 템플릿 콜백 패턴
        return traceTemplate.execute("OrderController.processCancelBuy()", new TraceCallback<String>() {
            @Override
            public String call() {
                try {
                    orderService.cancelOrder(orderId);
                } catch (RuntimeException e) {
                    MessageDto message = new MessageDto(e.getMessage(), "/order/orders", RequestMethod.GET, null);
                    return showMessageAndRedirect(message, model);
                }

                return "redirect:/order/orders";
            }
        });
    }

    @RequestMapping(value = "/orders/{orderId}/pay")
    public String processPayBuy(@PathVariable("orderId") Long orderId, Model model) {

        try {
            Orders order = orderService.fineOne(orderId);
            payService.pay(order.getTotalPrice());
            log.info("Pay 임시로직 실행");
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
