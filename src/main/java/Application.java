import lombok.extern.slf4j.Slf4j;
import publisher.IterablePublisher;
import publisher.MapPublisher;
import publisher.ReducePublisher;
import publisher.SumPublisher;
import subscriber.LogSubscriber;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Application {
    public static void main(String[] args) {
        List<Integer> iter = Stream.iterate(1, i -> i + 1).limit(10).collect(Collectors.toUnmodifiableList());

        IterablePublisher<Integer> iterPub = new IterablePublisher<>(iter);

        new MapPublisher<Integer, Integer>(iterPub)
                .doOnNext(i -> i * 100)
                .subscribe(LogSubscriber.just());

        new SumPublisher(iterPub)
                .subscribe(LogSubscriber.just());

        new ReducePublisher<Integer, Integer>(iterPub)
                .reduce(0, Integer::sum)
                .subscribe(LogSubscriber.just());
    }
}
