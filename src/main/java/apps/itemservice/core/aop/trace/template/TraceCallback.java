package apps.itemservice.core.aop.trace.template;

public interface TraceCallback<T> {
    T call();
}
