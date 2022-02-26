package publisher;

import subscriber.DelegateSubscriber;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.function.BiFunction;

public class ReducePublisher<T, R> implements Publisher<R> {
    private final Publisher<T> pub;

    private R init;
    private BiFunction<R, T, R> biFunction;

    public ReducePublisher(Publisher<T> pub) {
        this.pub = pub;
    }

    public ReducePublisher<T, R> reduce(R init, BiFunction<R, T, R> biFunction) {
        this.init = init;
        this.biFunction = biFunction;

        return this;
    }

    @Override
    public void subscribe(Subscriber<? super R> sub) {
        pub.subscribe(new DelegateSubscriber<T, R>(sub) {
            private R result = init;

            @Override
            public void onNext(T item) {
                result = biFunction.apply(result, item);
            }

            @Override
            public void onComplete() {
                sub.onNext(result);
                sub.onComplete();
            }
        });
    }
}
