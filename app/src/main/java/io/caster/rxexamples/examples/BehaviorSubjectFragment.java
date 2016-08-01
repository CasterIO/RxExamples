package io.caster.rxexamples.examples;

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
import rx.internal.operators.OnSubscribeRedo;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class BehaviorSubjectFragment extends Fragment {

    private static final String GOOG = "GOOG";

    private Unbinder unbinder;
    private CompositeSubscription compositeSubscription;

    public static BehaviorSubjectFragment newInstance() {
        return new BehaviorSubjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_behavior_subject, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.behavior_subject_test)
    public void onBehaviorSubjectClick() {
        unsubscribe();
        createSubscription();

        BehaviorSubject<Stock> behaviorSubject = BehaviorSubject.create();

        Subscription subscription = behaviorSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        behaviorSubject.onNext(new Stock(GOOG, 347));

        // Register a new subscriber who shows up late (tardy)
        Subscription tardySubscription = behaviorSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

        // Terminate the subject with an onCompleted event
        behaviorSubject.onCompleted();

        // Subscribe much later to see what happens.
        Subscription extraTardySubscription = behaviorSubject.subscribe(getExtraTardySubscriber());
        compositeSubscription.add(extraTardySubscription);
    }

    @OnClick(R.id.behavior_subject_existing_item)
    public void onExistingValueClick() {
        unsubscribe();
        createSubscription();

        BehaviorSubject<Stock> behaviorSubject = BehaviorSubject.create(new Stock(GOOG, 199));

        Subscription subscription = behaviorSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        behaviorSubject.onNext(new Stock(GOOG, 347));

        // Demonstrate how you can also get the existing values
        final Stock currentStock = behaviorSubject.getValue();
        Timber.d("Current stock: %s", currentStock.toString());


        // Register a new subscriber who shows up late (tardy)
        Subscription tardySubscription = behaviorSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

        Timber.d("Items: %s", behaviorSubject.getValues());

        // Terminate the subject with an onCompleted event
        behaviorSubject.onCompleted();

        // Subscribe much later to see what happens.
        Subscription extraTardySubscription = behaviorSubject.subscribe(getExtraTardySubscriber());
        compositeSubscription.add(extraTardySubscription);
    }

    @OnClick(R.id.behavior_subject_test_error)
    public void onTestErrorCondition() {

        unsubscribe();
        createSubscription();

        BehaviorSubject<Stock> behaviorSubject = BehaviorSubject.create();

        Subscription subscription = behaviorSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        behaviorSubject.onNext(new Stock(GOOG, 347));

        // Register a new subscriber who shows up late (tardy)
        Subscription tardySubscription = behaviorSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);

        // Terminate the subject with an onCompleted event
        behaviorSubject.onError(new Exception("Boom!"));

        // Subscribe much later to see what happens.
        Subscription extraTardySubscription = behaviorSubject.subscribe(getExtraTardySubscriber());
        compositeSubscription.add(extraTardySubscription);

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
                Timber.d("onNext on default subscriber: %s", stock.toString());
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
                Timber.d("onNext on tardy subscriber: %s", stock.toString());
            }
        };
    }

    /**
     * Simple subscriber that is used to subscribe to a replay subject after it has completed.
     */
    private Subscriber<Stock> getExtraTardySubscriber() {
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
                Timber.d("onNext on tardy subscriber: %s", stock.toString());
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
