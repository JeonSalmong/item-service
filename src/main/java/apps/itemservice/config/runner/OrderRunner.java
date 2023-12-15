package apps.itemservice.config.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRunner implements ApplicationRunner {

//    private final PayService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ApplicationRunner 실행");
    }
}
