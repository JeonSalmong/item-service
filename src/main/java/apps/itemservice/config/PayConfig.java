package apps.itemservice.config;

import apps.itemservice.platform.pay.PayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PayConfig {

    @Bean
    public PayClient PayClient() {
        return new PayClient();
    }
}
