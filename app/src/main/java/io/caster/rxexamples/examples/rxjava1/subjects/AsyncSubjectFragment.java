package io.caster.rxexamples.examples.rxjava1.subjects;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.caster.rxexamples.R;
import io.caster.rxexamples.models.Stock;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.AsyncSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class AsyncSubjectFragment extends Fragment {

    private static final String GOOG = "GOOG";

    private CompositeSubscription compositeSubscription;
    private Unbinder unbinder;

    public static Fragment newInstance() {
        return new AsyncSubjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_async_subject, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.async_subject_test)
    public void onAsyncSubjectTestClick() {

        unsubscribe();
        createSubscription();

        AsyncSubject<Stock> stockAsyncSubject = AsyncSubject.create();

        stockAsyncSubject.onNext(new Stock(GOOG, 722));

        // Will get the last value (GOOG, 723) and all future items and terminal events
        Subscription subscription = stockAsyncSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        stockAsyncSubject.onNext(new Stock(GOOG, 723));
        stockAsyncSubject.onNext(new Stock(GOOG, 100));
        stockAsyncSubject.onNext(new Stock(GOOG, 699));
        stockAsyncSubject.onCompleted();

        Subscription tardySubscription = stockAsyncSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

    }

    @OnClick(R.id.async_subject_test_error)
    public void onAsyncSubjectTestWithErrorClick() {
        unsubscribe();
        createSubscription();

        AsyncSubject<Stock> stockAsyncSubject = AsyncSubject.create();

        stockAsyncSubject.onNext(new Stock(GOOG, 722));

        // Will get the last value (GOOG, 723) and all future items and terminal events
        Subscription subscription = stockAsyncSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        stockAsyncSubject.onNext(new Stock(GOOG, 723));
        stockAsyncSubject.onNext(new Stock(GOOG, 100));
        stockAsyncSubject.onNext(new Stock(GOOG, 699));
        stockAsyncSubject.onError(new Exception("Boom!")); // current and future subscribers will only receive this, with NO items emitted.

        Subscription tardySubscription = stockAsyncSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

    }

    /**
     * Simple subscriber for the default replay subject.
     */
    private Subscriber<Stock> getDefaultSubscriber() {
        return new Subscriber<Stock>() {
            @Override
            public void onCompleted() {
                Timber.d("Default subscriber completed called.");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error called on default subscriber.");
            }

            @Override
            public void onNext(Stock stock) {
                Timber.d("onNext on default subscriber: " + stock.toString());
            }
        };
    }

    /**
     * Simple subscriber that is used to subscribe to a replay subject after it has completed.
     */
    private Subscriber<Stock> getTardySubscriber() {
        return new Subscriber<Stock>() {
            @Override
            public void onCompleted() {
                Timber.d("Tardy subscriber completed called.");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error called on Tardy subscriber.");
            }

            @Override
            public void onNext(Stock stock) {
                Timber.d("onNext on tardy subscriber: " + stock.toString());
            }
        };
    }

    private void createSubscription() {
        compositeSubscription = new CompositeSubscription();
    }

    private void unsubscribe() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
