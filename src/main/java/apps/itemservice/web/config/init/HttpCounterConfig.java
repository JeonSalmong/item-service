package apps.itemservice.web.config.init;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * prometheus application ip, port를 알수 없기 때문에 추가
 * pushgateway의 http_requests_total만 알수 있기 때문에
 * application ip, port 정보를 별도 tag로 구성해서 보내줘야 함
 */
@Slf4j
@Configuration
public class HttpCounterConfig {

    private MeterRegistry registry;
    private ApplicationContext applicationContext;

    public HttpCounterConfig(MeterRegistry registry, ApplicationContext applicationContext) {
        this.registry = registry;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = applicationContext.getBean(Environment.class).getProperty("server.port", Integer.class, 8080);
        log.info("http.requests push to gateway {}, {}", ip, port);
        Counter.builder("http.requests")
                .tag("ip", ip)
                .tag("port", String.valueOf(port))
                .description("ip_port")
                .register(registry).increment();
    }
}
