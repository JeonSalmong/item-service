package apps.itemservice.service.security;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.vo.RoleType;
import apps.itemservice.service.member.MemberService;
import apps.itemservice.service.vo.ActuatorTags;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Timed(ActuatorTags.LOGIN_TIMED)
@Slf4j
@Component
public class MyUserDetailService implements UserDetailsService {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailService(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @Counted(ActuatorTags.LOGIN_COUNTED)
    @Override
    public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
        Optional<Member> findOne = memberService.findByLoginId(insertedUserId);
        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다 ㅠ"));

        //패스워드 저장시 암호화 되면 필요 없음
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        log.info("암호화된 Password : " + passwordEncoder.encode(member.getPassword()));

        if (member.getRoleType() == null) {
            member.setRoleType(RoleType.USER);
        }

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles(member.getRoleType().getAuthority())
                .build();
    }
}
