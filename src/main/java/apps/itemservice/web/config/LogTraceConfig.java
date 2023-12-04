package apps.itemservice.web.config;

import apps.itemservice.core.aop.trace.FieldLogTrace;
import apps.itemservice.core.aop.trace.LogTrace;
import apps.itemservice.core.aop.trace.template.TraceTemplate;
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
