package publisher;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class IterablePublisher<E> implements Publisher<E> {
    private final Iterable<E> iter;

    public IterablePublisher(Iterable<E> iter) {
        this.iter = iter;
    }

    @Override
    public void subscribe(Subscriber<? super E> sub) {
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
