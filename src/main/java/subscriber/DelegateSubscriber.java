package subscriber;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class DelegateSubscriber implements Subscriber<Integer> {
    private final Subscriber<? super Integer> sub;

    public DelegateSubscriber(Subscriber<? super Integer> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Subscription sub) {
        this.sub.onSubscribe(sub);
    }

    @Override
    public void onNext(Integer item) {
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
