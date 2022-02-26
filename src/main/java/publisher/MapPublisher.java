package publisher;

import subscriber.DelegateSubscriber;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.function.Function;

public class MapPublisher<T, R> implements Publisher<R> {
    private final Publisher<T> pub;
    private Function<T, R> function;

    public MapPublisher(Publisher<T> pub) {
        this.pub = pub;
    }

    public MapPublisher<T, R> doOnNext(Function<T, R> function) {
        this.function = function;

        return this;
    }

    @Override
    public void subscribe(Subscriber<? super R> sub) {
        pub.subscribe(new DelegateSubscriber<T, R>(sub) {
            @Override
            public void onNext(T item) {
                sub.onNext(function.apply(item));
            }
        });
    }
}
