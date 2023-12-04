package apps.itemservice.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteService {
    public String call() {
        log.info("ConcreteService 호출");
        return "c";
    }
}
