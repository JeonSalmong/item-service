package apps.itemservice.web.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean  // 비밀번호 암호화
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().
                requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher( "/login"))
                .requestMatchers(new AntPathRequestMatcher( "/members/add"))
                .requestMatchers(new AntPathRequestMatcher( "/error/**"))
                .requestMatchers(new AntPathRequestMatcher( "/error-page/**"))
                .requestMatchers(new AntPathRequestMatcher( "/api/**"))
                .requestMatchers(new AntPathRequestMatcher( "/test/**"))
                .requestMatchers(new AntPathRequestMatcher( "/*.ico"))
                .requestMatchers(new AntPathRequestMatcher( "/css/**"))
                .requestMatchers(new AntPathRequestMatcher( "/js/**"))
                .requestMatchers(new AntPathRequestMatcher( "/img/**"))
                .requestMatchers(new AntPathRequestMatcher( "/lib/**"))
                .requestMatchers(new AntPathRequestMatcher( "/memory/**"));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/order/orders");
        http
                .csrf().disable().cors().disable()
                .authorizeHttpRequests(
                        request -> request
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                .requestMatchers(new MvcRequestMatcher(introspector, "/")).permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern("/order/orders")).hasRole("ADMIN")       //권한 없는 경우 403
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")                 //로그인페이지 controller path 설정하지 않으면 내장 로그인창이 뜸
                        .loginProcessingUrl("/login-process")   // [B] submit 받을 url
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(withDefaults());

        return http.build();
    }

}
