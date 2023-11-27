package apps.itemservice.web.config;

import apps.itemservice.repository.item.ItemRepository;
import apps.itemservice.repository.item.JpaItemRepository;
import apps.itemservice.repository.member.JpaMemberRepository;
import apps.itemservice.repository.member.MemberRepository;
import apps.itemservice.service.item.ItemService;
import apps.itemservice.service.member.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.sql.DataSource;


/**
 * 컴포넌트 스캔 방식(@Service, @Repository, @Autowired 애노테이션 사용)이 아닌 메뉴얼 빈 설정
 * 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이기 때문
 */
@EnableJpaAuditing
@Configuration
public class SpringBeanConfig {
    private final DataSource dataSource;
    private final EntityManager em;

    public SpringBeanConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }

    @Bean
    public ItemService itemService() {
        return new ItemService(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepository(em);
    }

}
