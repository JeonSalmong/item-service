package apps.itemservice.config.aop.trace.template;

public interface TraceCallback<T> {
    T call();
}
