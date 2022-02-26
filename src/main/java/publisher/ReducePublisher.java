package publisher;

import subscriber.DelegateSubscriber;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.function.BiFunction;

public class ReducePublisher implements Publisher<Integer> {
    private final Publisher<Integer> pub;

    private int init;
    private BiFunction<Integer, Integer, Integer> biFunction;

    private ReducePublisher(Publisher<Integer> pub) {
        this.pub = pub;
    }

    public static ReducePublisher of(Publisher<Integer> pub) {
        return new ReducePublisher(pub);
    }

    public ReducePublisher reduce(int init, BiFunction<Integer, Integer, Integer> biFunction) {
        this.init = init;
        this.biFunction = biFunction;

        return this;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> sub) {
        pub.subscribe(new DelegateSubscriber(sub) {
            private int result = init;

            @Override
            public void onNext(Integer item) {
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
