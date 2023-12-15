package apps.itemservice.config;

import apps.itemservice.platform.converter.typeconverter.IpPortToStringConverter;
import apps.itemservice.platform.converter.typeconverter.StringToIpPortConverter;
import apps.itemservice.platform.filter.LogFilter;
import apps.itemservice.platform.filter.LoginCheckFilter;
import apps.itemservice.platform.formatter.MyNumberFormatter;
import apps.itemservice.platform.interceptor.LogInterceptor;
import apps.itemservice.web.common.resolver.argument.LoginMemberArgumentResolver;
import apps.itemservice.web.common.resolver.exception.MyHandlerExceptionResolver;
import apps.itemservice.web.common.resolver.exception.UserHandlerExceptionResolver;
import jakarta.servlet.Filter;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Filter
    // HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
    // HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러
    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter2() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    // Interceptor
    // HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
    // HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러
    // PathPattern을 매우 정교하게 설정할 수 있음
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error/**", "/error-page/**");

        //Spring Security 적용으로 주석처리
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/", "/members/add", "/login", "/logout", "/login-process", "/login-form", "/login-accept",
//                        "/css/**", "/*.ico", "/error/**", "/error-page/**", "/api/**", "/api2/**",
//                        "/test/**"
//                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //컨버전 서비스는 @RequestParam , @ModelAttribute , @PathVariable , 뷰 템플릿 등에서 사용할 수있다.

        // 우선순위 컨버터가 포맷터보다 우선이므로 주석처리
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        // 포맷터 추가
        registry.addFormatter(new MyNumberFormatter());
    }

    /**
     * HTTP 요청 응답 기록
     * @return
     */
    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
