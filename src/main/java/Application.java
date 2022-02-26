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

        IterablePublisher iterPub = IterablePublisher.of(iter);

        MapPublisher.of(iterPub)
                .doOnNext(i -> i * 100)
                .doOnNext(i -> -i)
                .subscribe(LogSubscriber.just());

        SumPublisher.of(iterPub)
                .subscribe(LogSubscriber.just());

        ReducePublisher.of(iterPub)
                .reduce(0, Integer::sum)
                .subscribe(LogSubscriber.just());
    }
}
