package apps.itemservice.service.login;

import apps.itemservice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import apps.itemservice.domain.entity.member.Member;
import org.springframework.stereotype.Service;

/**
 * Session Manager 사용시 사용하는 서비스 (Spring Security 적용으로 사용하지 않음)
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtTokenProvider;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

//    @Transactional
//    public String login(LoginForm loginForm) {
//        Member member = memberRepository.findByLoginId(loginForm.getLoginId())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다."));
//        if (!passwordEncoder.matches(loginForm.getPassword(), passwordEncoder.encode(member.getPassword()))) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
//        return jwtTokenProvider.createToken(member.getName(), member.getRoleType());
//    }
}
