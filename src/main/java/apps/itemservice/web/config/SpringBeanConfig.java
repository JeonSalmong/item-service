package apps.itemservice.web.config;

import apps.itemservice.core.file.FileStore;
import apps.itemservice.core.trace.FieldLogTrace;
import apps.itemservice.core.trace.LogTrace;
import apps.itemservice.core.trace.advice.LogTraceAdvice;
import apps.itemservice.core.trace.handler.LogTraceBasicHandler;
import apps.itemservice.core.trace.template.TraceTemplate;
import apps.itemservice.repository.item.ItemRepository;
import apps.itemservice.repository.item.ItemRepositoryProxy;
import apps.itemservice.repository.item.JpaItemRepository;
import apps.itemservice.repository.member.JpaMemberRepository;
import apps.itemservice.repository.member.MemberRepository;
import apps.itemservice.repository.member.MemberRepositoryProxy;
import apps.itemservice.service.item.ItemService;
import apps.itemservice.service.item.ItemServiceImpl;
import apps.itemservice.service.item.ItemServiceProxy;
import apps.itemservice.service.member.MemberService;
import apps.itemservice.service.member.MemberServiceProxy;
import apps.itemservice.web.controller.item.BasicItemControllerImpl;
import apps.itemservice.web.controller.item.ItemController;
import apps.itemservice.web.controller.item.ItemControllerProxy;
import apps.itemservice.web.controller.member.MemberController;
import apps.itemservice.web.controller.member.MemberControllerProxy;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;


/**
 * 컴포넌트 스캔 방식(@Service, @Repository, @Autowired 애노테이션 사용)이 아닌 메뉴얼 빈 설정
 * 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이기 때문
 */
@Slf4j
@EnableJpaAuditing
@EnableScheduling
@Configuration
public class SpringBeanConfig {
    private final DataSource dataSource;
    private final EntityManager em;
    private final FileStore fileStore;

    public SpringBeanConfig(DataSource dataSource, EntityManager em, FileStore fileStore) {
        this.dataSource = dataSource;
        this.em = em;
        this.fileStore = fileStore;
    }

    /**
     * counted 메트릭 AOP 적용
     * @param registry
     * @return
     */
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }

    /**
     * timed 메트릭 AOP 적용
     * @param registry
     * @return
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }


    /**
     * Case1 : 인터페이스 없는 구체 클래스
     */

    // 1. MemberController 일반
//    @Bean
//    public MemberService memberService() {
//        return new MemberService(memberRepository());
//    }
//    @Bean
//    public MemberRepository memberRepository() {
////        return new MemoryMemberRepository();
////        return new JdbcMemberRepository(dataSource);
////        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }

    // 2. MemberController Proxy
//    @Bean
//    public MemberController memberController(LogTrace logTrace) {
//        MemberController memberController = new MemberController(memberService(logTrace));
//        return new MemberControllerProxy(memberController, logTrace);
//    }
//    @Bean
//    public MemberService memberService(LogTrace logTrace) {
//        MemberService memberService = new MemberService(memberRepository(logTrace));
//        return new MemberServiceProxy(memberService, logTrace);
//    }
//    @Bean
//    public MemberRepository memberRepository(LogTrace logTrace) {
//        JpaMemberRepository jpaMemberRepository = new JpaMemberRepository(em);
//        return new MemberRepositoryProxy(jpaMemberRepository, logTrace);
//    }

    // 3. MemberController ProxyFactory 적용
    @Bean
    public MemberController memberController(LogTrace logTrace) {
        MemberController memberController = new MemberController(memberService(logTrace));
        ProxyFactory factory = new ProxyFactory(memberController);
        factory.addAdvisor(getAdvisor(logTrace));
        MemberController proxy = (MemberController) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), memberController.getClass());
        return proxy;
    }
    @Bean
    public MemberService memberService(LogTrace logTrace) {
        MemberService memberService = new MemberService(memberRepository(logTrace));
        ProxyFactory factory = new ProxyFactory(memberService);
        factory.addAdvisor(getAdvisor(logTrace));
        MemberService proxy = (MemberService) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), memberService.getClass());
        return proxy;
    }
    @Bean
    public MemberRepository memberRepository(LogTrace logTrace) {
        MemberRepository memberRepository = new JpaMemberRepository(em);
        ProxyFactory factory = new ProxyFactory(memberRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        MemberRepository proxy = (MemberRepository) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), memberRepository.getClass());
        return proxy;
    }

    /**
     * Case2 : 인터페이스 있는 클래스
     */

    // 1. ItemController 수동 주입
//    @Bean
//    public ItemController itemController() {
//        return new BasicItemControllerImpl(itemService(), fileStore);
//    }
//    @Bean
//    public ItemService itemService() {
//        return new ItemServiceImpl(itemRepository());
//    }
//
//    @Bean
//    public ItemRepository itemRepository() {
//        return new JpaItemRepository(em);
//    }

    // 2. ItemController Proxy 수동 주입
//    @Bean
//    public ItemController itemController(LogTrace logTrace) {
//        BasicItemControllerImpl basicItemController = new BasicItemControllerImpl(itemService(logTrace), fileStore);
//        return new ItemControllerProxy(basicItemController, logTrace);
//    }
//    @Bean
//    public ItemService itemService(LogTrace logTrace) {
//        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(itemRepository(logTrace));
//        return new ItemServiceProxy(itemServiceImpl, logTrace);
//    }
//
//    @Bean
//    public ItemRepository itemRepository(LogTrace logTrace) {
//        JpaItemRepository jpaItemRepository = new JpaItemRepository(em);
//        return new ItemRepositoryProxy(jpaItemRepository, logTrace);
//    }

    // 3. ItemController 동적 Proxy 수동 주입
//    private static final String[] PATTERNS = {"item*", "add*"};
//    @Bean
//    public ItemController itemController(LogTrace logTrace) {
//        ItemController itemController = new BasicItemControllerImpl(itemService(logTrace), fileStore);
//        ItemController proxy = (ItemController) Proxy.newProxyInstance(
//                ItemController.class.getClassLoader()
//                , new Class[]{ItemController.class}
//                , new LogTraceBasicHandler(itemController, logTrace, PATTERNS)
//        );
//        return proxy;
//    }
//    @Bean
//    public ItemService itemService(LogTrace logTrace) {
//        ItemService itemService = new ItemServiceImpl(itemRepository(logTrace));
//        ItemService proxy = (ItemService) Proxy.newProxyInstance(
//                ItemService.class.getClassLoader()
//                , new Class[]{ItemService.class}
//                , new LogTraceBasicHandler(itemService, logTrace, PATTERNS)
//        );
//        return proxy;
//    }
//
//    @Bean
//    public ItemRepository itemRepository(LogTrace logTrace) {
//        ItemRepository itemRepository = new JpaItemRepository(em);
//        ItemRepository proxy = (ItemRepository) Proxy.newProxyInstance(
//                ItemRepository.class.getClassLoader(),
//                new Class[]{ItemRepository.class},
//                new LogTraceBasicHandler(itemRepository, logTrace, PATTERNS)
//        );
//        return proxy;
//    }

    // 4. ItemController ProxyFactory 적용
    private static final String[] PATTERNS = {"item*", "add*", "find*", "save*", "list*"};
    @Bean
    public ItemController itemController(LogTrace logTrace) {
        ItemController itemController = new BasicItemControllerImpl(itemService(logTrace), fileStore);
        ProxyFactory factory = new ProxyFactory(itemController);
        factory.addAdvisor(getAdvisor(logTrace));
        ItemController proxy = (ItemController) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), itemController.getClass());
        return proxy;
    }
    @Bean
    public ItemService itemService(LogTrace logTrace) {
        ItemService itemService = new ItemServiceImpl(itemRepository(logTrace));
        ProxyFactory factory = new ProxyFactory(itemService);
        factory.addAdvisor(getAdvisor(logTrace));
        ItemService proxy = (ItemService) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), itemService.getClass());
        return proxy;
    }

    @Bean
    public ItemRepository itemRepository(LogTrace logTrace) {
        ItemRepository itemRepository = new JpaItemRepository(em);
        ProxyFactory factory = new ProxyFactory(itemRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        ItemRepository proxy = (ItemRepository) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), itemRepository.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames(PATTERNS);
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        //advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
