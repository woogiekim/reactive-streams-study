package publisher;

import subscriber.DelegateSubscriber;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

public class SumPublisher implements Publisher<Integer> {
    private final Publisher<Integer> pub;

    private SumPublisher(Publisher<Integer> pub) {
        this.pub = pub;
    }

    public static SumPublisher of(Publisher<Integer> pub) {
        return new SumPublisher(pub);
    }


    @Override
    public void subscribe(Subscriber<? super Integer> sub) {
        pub.subscribe(new DelegateSubscriber(sub) {
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
