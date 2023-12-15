package apps.itemservice.platform.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayClient {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public void pay(int money) {
        if ("local".equals(activeProfile)) {
            log.info("Local 결제 money={}", money);
        } else if ("local".equals(activeProfile)) {
            log.info("Dev 결제 money={}", money);
        } else if ("prod".equals(activeProfile)) {
            log.info("Prod 결제 money={}", money);
        } else {
            log.info("Local 결제 money={}", money);
        }
    }
}
