package subscriber;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class DelegateSubscriber<T, R> implements Subscriber<T> {
    @SuppressWarnings("rawtypes")
    private final Subscriber sub;

    public DelegateSubscriber(Subscriber<? super R> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription sub) {
        this.sub.onSubscribe(sub);
    }

    @Override
    public void onNext(T item) {
        //noinspection unchecked
        this.sub.onNext(item);
    }

    @Override
    public void onError(Throwable throwable) {
        this.sub.onError(throwable);
    }

    @Override
    public void onComplete() {
        this.sub.onComplete();
    }
}
