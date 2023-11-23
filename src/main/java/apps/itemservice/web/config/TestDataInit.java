package apps.itemservice.web.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.repository.item.MemoryItemRepository;
import apps.itemservice.repository.member.MemoryMemberRepository;
import org.springframework.stereotype.Component;

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
        memoryItemRepository.save(new Item("itemA", 10000, 10));
        memoryItemRepository.save(new Item("itemB", 20000, 20));

//        memoryMemberRepository.save(new Member("1", "1", "테스터1"));
//        memoryMemberRepository.save(new Member("2", "2", "테스터2"));
    }

}