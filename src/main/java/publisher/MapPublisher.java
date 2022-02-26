package publisher;

import subscriber.DelegateSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.function.Function;
import java.util.stream.Stream;

public class MapPublisher implements Publisher<Integer> {
    private final Publisher<Integer> pub;
    private Function<Integer, Integer> function;

    private MapPublisher(Publisher<Integer> pub) {
        this.pub = pub;
    }

    public static MapPublisher of(Publisher<Integer> pub) {
        return new MapPublisher(pub);
    }

    public MapPublisher doOnNext(Function<Integer, Integer> function) {
        if (this.function == null) {
            this.function = function;
        }

        this.function = this.function.compose(function);

        return this;
    }

    @Override
    public void subscribe(Subscriber<? super Integer> sub) {
        pub.subscribe(new DelegateSubscriber(sub) {
            @Override
            public void onNext(Integer item) {
                sub.onNext(function.apply(item));
            }
        });
    }
}
