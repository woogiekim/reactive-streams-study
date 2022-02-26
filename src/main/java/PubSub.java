import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Flow.Subscriber;
import static java.util.concurrent.Flow.Subscription;

public class PubSub {
    public static void main(String[] args) throws InterruptedException {
        Iterable<Integer> itr = List.of(1, 2, 3, 4, 5);
        ExecutorService es = Executors.newSingleThreadExecutor();

        Publisher p = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                Iterator<Integer> it = itr.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        es.execute(() -> {
                            int i = 0;

                            while (i++ < n) {
                                if (it.hasNext()) {
                                    subscriber.onNext(it.next());
                                } else {
                                    subscriber.onComplete();
                                    break;
                                }
                            }
                        });

                    }

                    @Override
                    public void cancel() {
                    }
                });
            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;

                System.out.println("onSubscribe");
                subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                this.subscription.request(1);

                System.out.println("onNext " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        p.subscribe(s);

        es.awaitTermination(300, TimeUnit.SECONDS);
        es.shutdown();
    }
}
