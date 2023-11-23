package apps.itemservice.web.controller.member;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.member.Team;
import apps.itemservice.domain.entity.valueType.Address;
import apps.itemservice.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Member member, String city, String street, String zipcode, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        // 테스트를 위해 팀 강제 할당
        Team team1 = new Team();
        team1.setId("team1");
        team1.setName("팀1");

        // 팀 정보 우선 저장
        Optional<Team> findTeam = memberService.findByTeam(team1.getId());
        if (findTeam.isEmpty()) {
            memberService.saveTeam(team1, member);
        }

        Address address = new Address(city, street, zipcode);
        member.setAddress(address);

        memberService.join(member, team1);

        return "redirect:/";
    }

    @GetMapping(value = "/list")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
