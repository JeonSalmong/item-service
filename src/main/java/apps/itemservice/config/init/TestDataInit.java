package apps.itemservice.config.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import apps.itemservice.web.domain.entity.item.Item;
import apps.itemservice.web.domain.item.MemoryItemRepository;
import apps.itemservice.web.domain.member.MemoryMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemoryItemRepository memoryItemRepository;
    private final MemoryMemberRepository memoryMemberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        log.info("spring boot init() 실행");
        memoryItemRepository.save(new Item("itemA", 10000, 10));
        memoryItemRepository.save(new Item("itemB", 20000, 20));

//        memoryMemberRepository.save(new Member("1", "1", "테스터1"));
//        memoryMemberRepository.save(new Member("2", "2", "테스터2"));
    }

}