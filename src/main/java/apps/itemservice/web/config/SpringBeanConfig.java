package apps.itemservice.web.config;

import apps.itemservice.core.file.FileStore;
import apps.itemservice.core.trace.FieldLogTrace;
import apps.itemservice.core.trace.LogTrace;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;


/**
 * 컴포넌트 스캔 방식(@Service, @Repository, @Autowired 애노테이션 사용)이 아닌 메뉴얼 빈 설정
 * 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이기 때문
 */
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

    // ItemController Proxy 주입
    @Bean
    public ItemController itemController(LogTrace logTrace) {
        BasicItemControllerImpl basicItemController = new BasicItemControllerImpl(itemService(logTrace), fileStore);
        return new ItemControllerProxy(basicItemController, logTrace);
    }
    @Bean
    public ItemService itemService(LogTrace logTrace) {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(itemRepository(logTrace));
        return new ItemServiceProxy(itemServiceImpl, logTrace);
    }

    @Bean
    public ItemRepository itemRepository(LogTrace logTrace) {
        JpaItemRepository jpaItemRepository = new JpaItemRepository(em);
        return new ItemRepositoryProxy(jpaItemRepository, logTrace);
    }

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

    @Bean
    public MemberController memberController(LogTrace logTrace) {
        MemberController memberController = new MemberController(memberService(logTrace));
        return new MemberControllerProxy(memberController, logTrace);
    }
    @Bean
    public MemberService memberService(LogTrace logTrace) {
        MemberService memberService = new MemberService(memberRepository(logTrace));
        return new MemberServiceProxy(memberService, logTrace);
    }
    @Bean
    public MemberRepository memberRepository(LogTrace logTrace) {
        JpaMemberRepository jpaMemberRepository = new JpaMemberRepository(em);
        return new MemberRepositoryProxy(jpaMemberRepository, logTrace);
    }

}
