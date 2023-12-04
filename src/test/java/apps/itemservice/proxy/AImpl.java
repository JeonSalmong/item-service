package apps.itemservice.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AImpl implements AInterface {
    @Override
    public String call() {
        log.info("A 호출");
        return "a";
    }

    @Override
    public String find() {
        log.info("A 찾기");
        return "a";
    }
}
