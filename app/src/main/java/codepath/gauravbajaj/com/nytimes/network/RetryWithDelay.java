package codepath.gauravbajaj.com.nytimes.network;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by gauravb on 3/19/17.
 */

public class RetryWithDelay implements
        Func1<Observable<? extends Throwable>, Observable<?>> {
    String TAG = RetryWithDelay.class.getSimpleName();
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (++retryCount < maxRetries) {

                            Log.d(TAG, "call: retry");
                            // When this Observable calls onNext, the original
                            // Observable will be retried (i.e. re-subscribed).
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }

                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }

}

