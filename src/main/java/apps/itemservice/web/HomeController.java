package apps.itemservice.web;

import apps.itemservice.platform.session.SessionConst;
import apps.itemservice.platform.session.SessionManager;
import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.member.MemberService;
import apps.itemservice.web.common.resolver.argument.Login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 쿠키적용
     * @param memberId
     * @param model
     * @return
     */
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        log.info("@CookieValue memberId? {}", memberId);
        if (memberId == null) {
            return "home";
        }
        //로그인
        Optional<Member> loginMember = memberService.findOne(memberId);
        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    private final SessionManager sessionManager;

    /**
     * 커스텀 세선적용
     * @param request
     * @param model
     * @return
     */
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
    //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }
        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     * httpsession 세선적용
     * @param request
     * @param model
     * @return
     */
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원 데이터가 없으면 home
        if (member == null) {
            return "home";
        }
        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     * httpsession 세선적용 - SessionAttribute
     * @param member
     * @param model
     * @return
     */
    //@GetMapping("/")
    public String homeLoginV3_1(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member, Model model) {
        //세션이 없으면 home
        if (member == null) {
            return "home";
        }

        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    /**
     * httpsession 세선적용 - ArgumentResolver
     * @param member
     * @param model
     * @return
     */
    //@GetMapping("/")
    public String homeLoginV3_2(@Login Member member, Model model) {
        //세션이 없으면 home
        if (member == null) {
            return "home";
        }

        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV4(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "home";
        }
//        model.addAttribute("loginId", user.getUsername());
//        model.addAttribute("loginRoles", user.getAuthorities());

        Member member = new Member();
        member.setId(user.getUsername());
        member.setName(user.getUsername());
        member.setLoginDateTime(LocalDateTime.now());
        model.addAttribute("member", member);
        return "loginHome";
    }

}
