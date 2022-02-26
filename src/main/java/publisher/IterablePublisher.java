package publisher;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class IterablePublisher implements Publisher<Integer> {
    private final Iterable<Integer> iter;

    private IterablePublisher(Iterable<Integer> iter) {
        this.iter = iter;
    }

    public static IterablePublisher of(Iterable<Integer> iter) {
        return new IterablePublisher(iter);
    }

    @Override
    public void subscribe(Subscriber<? super Integer> sub) {
        sub.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    iter.forEach(sub::onNext);
                    sub.onComplete();
                } catch (Throwable cause) {
                    sub.onError(cause);
                }
            }

            @Override
            public void cancel() {
            }
        });
    }
}
