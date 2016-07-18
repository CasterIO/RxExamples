package io.caster.rxexamples.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.caster.rxexamples.R;
import io.caster.rxexamples.models.Stock;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class ReplaySubjectFragment extends Fragment {

    private static final String GOOG = "GOOG";

    private CompositeSubscription compositeSubscription;
    private Unbinder unbinder;

    public static ReplaySubjectFragment newInstance() {
        return new ReplaySubjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_replay_subject, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    private void unsubscribe() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.replay_subject_default)
    public void onReplaySubjectDefault() {
        unsubscribe();
        createSubscription();

        ReplaySubject<Stock> stockReplaySubject = ReplaySubject.create();

        stockReplaySubject.onNext(new Stock(GOOG, 715.09));
        stockReplaySubject.onNext(new Stock(GOOG, 716.00));
        stockReplaySubject.onNext(new Stock(GOOG, 714.00));

        Subscription defaultSub = stockReplaySubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(defaultSub); // All three values will be delivered.

        stockReplaySubject.onNext(new Stock(GOOG, 720));
        stockReplaySubject.onCompleted(); // Terminate the stream with a completed event.

        // Subscribe again, this time the subscriber will get all events and the terminal event
        // right away. All items are "replayed" even though the stream has completed.
        Subscription tardySubscription = stockReplaySubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

    }

    @OnClick(R.id.replay_subject_sized)
    public void onReplaySubjectSizedClick() {
        unsubscribe();
        createSubscription();

        // A replay subject that will only replay the last two items.
        ReplaySubject<Stock> stockReplaySubject = ReplaySubject.createWithSize(2);

        stockReplaySubject.onNext(new Stock(GOOG, 715.09));
        stockReplaySubject.onNext(new Stock(GOOG, 716.00));
        stockReplaySubject.onNext(new Stock(GOOG, 714.00));

        // Only the last two items will be replayed to this subscriber (716 and 714)
        Subscription defaultSub = stockReplaySubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(defaultSub); // All three values will be delivered.

        // This will also be emitted to the defaultSub above.
        stockReplaySubject.onNext(new Stock(GOOG, 720));
        stockReplaySubject.onCompleted(); // Terminate the stream with a completed event.

        // Subscribe again, this time the subscriber will get the last two events and the terminal
        // event right away. The last two items are "replayed" even though the stream has completed.
        Subscription tardySubscription = stockReplaySubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

    }


    @OnClick(R.id.replay_subject_timed)
    public void onReplaySubjectTimed() {

        try {

            unsubscribe();
            createSubscription();

            // A replay subject that will only replay the last two items.
            ReplaySubject<Stock> stockReplaySubject = ReplaySubject.createWithTime(250, TimeUnit.MILLISECONDS, Schedulers.immediate());

            stockReplaySubject.onNext(new Stock(GOOG, 715.09));
            Thread.sleep(100);
            stockReplaySubject.onNext(new Stock(GOOG, 716.00));
            Thread.sleep(100);
            stockReplaySubject.onNext(new Stock(GOOG, 714.00));
            Thread.sleep(100);

            // Only the last two items will be replayed to this subscriber (716 and 714)
            // because the first one has already expired.
            Subscription defaultSub = stockReplaySubject.subscribe(getDefaultSubscriber());
            compositeSubscription.add(defaultSub); // All three values will be delivered.

            // This will also be emitted to the defaultSub above.
            stockReplaySubject.onNext(new Stock(GOOG, 720));
            Thread.sleep(100);
            stockReplaySubject.onCompleted(); // Terminate the stream with a completed event.

            // Lets sleep for another 100 millis to simulate some time passing.
            Thread.sleep(100);

            // Subscribe again with a new subscriber. This time the only item that is valid is
            // the last item: '720' as all the others have expired.
            Subscription tardySubscription = stockReplaySubject.subscribe(getTardySubscriber());
            compositeSubscription.add(tardySubscription);


        } catch (InterruptedException ex) {
            Timber.e(ex, ex.getMessage());
        }
    }

    private void createSubscription() {
        compositeSubscription = new CompositeSubscription();
    }

    //
    // Subscribers
    //

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
}
