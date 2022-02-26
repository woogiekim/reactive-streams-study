package subscriber;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

@Slf4j
public class LogSubscriber implements Subscriber<Integer> {
    private LogSubscriber() {
        log.isDebugEnabled(MarkerFactory.getMarker("DEBUG"));
    }

    public static LogSubscriber just() {
        return new LogSubscriber();
    }

    @Override
    public void onSubscribe(Subscription sub) {
        log.info("onSubscribe");
        sub.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Integer item) {
        log.info("onNext: {}", item);
    }

    @Override
    public void onError(Throwable cause) {
        log.info("onError: {}", cause.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }
}
