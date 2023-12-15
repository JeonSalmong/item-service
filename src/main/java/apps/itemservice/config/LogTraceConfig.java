package apps.itemservice.config;

import apps.itemservice.config.aop.trace.FieldLogTrace;
import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.template.TraceTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {
    @Bean
    public LogTrace logtrace() {
        return new FieldLogTrace();
    }

    @Bean
    public TraceTemplate traceTemplate(LogTrace trace) {
        return new TraceTemplate(trace);
    }
}
