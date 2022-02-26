package publisher;

import subscriber.DelegateSubscriber;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

public class SumPublisher implements Publisher<Integer> {
    private final Publisher<Integer> pub;

    public SumPublisher(Publisher<Integer> pub) {
        this.pub = pub;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> sub) {
        pub.subscribe(new DelegateSubscriber<Integer, Integer>(sub) {
            private int sum = 0;

            @Override
            public void onNext(Integer item) {
                sum += item;
            }

            @Override
            public void onComplete() {
                sub.onNext(sum);
                sub.onComplete();
            }
        });
    }
}
