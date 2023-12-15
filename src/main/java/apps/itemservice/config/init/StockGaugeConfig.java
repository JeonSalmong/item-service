package apps.itemservice.config.init;

import apps.itemservice.web.domain.item.ItemService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StockGaugeConfig {

    private final ItemService itemService;
    private MeterRegistry registry;

    public StockGaugeConfig(ItemService itemService, MeterRegistry registry) {
        this.itemService = itemService;
        this.registry = registry;
    }

    @PostConstruct
    public void init() {
        Gauge.builder("my.stock", itemService, service -> {
            log.info("stock gauge call");
            return service.getItemStock(1L);
        }).register(registry);
    }
}
