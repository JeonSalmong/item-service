package apps.itemservice.web.controller.member;

import apps.itemservice.core.trace.LogTrace;
import apps.itemservice.core.trace.TraceStatus;
import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.service.member.MemberService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public class MemberControllerProxy extends MemberController {
    private final MemberController target;
    private final LogTrace logTrace;
    public MemberControllerProxy(MemberController target, LogTrace logTrace) {
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String addForm(Member member) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberController.addForm()");
            //target 호출
            String result = target.addForm(member);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String save(Member member, String city, String street, String zipcode, BindingResult bindingResult) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberController.save()");
            //target 호출
            String result = target.save(member, city, street, zipcode, bindingResult);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String list(Model model) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("MemberController.list()");
            //target 호출
            String result = target.list(model);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
