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
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class PublishSubjectFragment extends Fragment {

    private static final String GOOG = "GOOG";

    private Unbinder unbinder;
    private CompositeSubscription compositeSubscription;

    public static PublishSubjectFragment newInstance() {
        return new PublishSubjectFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publish_subject, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.publish_subject_test)
    public void onPublishSubjectDefaultClick() {
        unsubscribe();
        createSubscription();

        PublishSubject<Stock> publishSubject = PublishSubject.create();

        publishSubject.onNext(new Stock(GOOG, 722));

        Subscription subscription = publishSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        publishSubject.onNext(new Stock(GOOG, 723));
        publishSubject.onNext(new Stock(GOOG, 100));
        publishSubject.onNext(new Stock(GOOG, 699));
        publishSubject.onCompleted();

        Subscription tardySubscription = publishSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);
    }

    @OnClick(R.id.publish_subject_test_error)
    public void onPublishSubjectTestError() {
        unsubscribe();
        createSubscription();

        PublishSubject<Stock> publishSubject = PublishSubject.create();

        publishSubject.onNext(new Stock(GOOG, 722));

        Subscription subscription = publishSubject.subscribe(getDefaultSubscriber());
        compositeSubscription.add(subscription);

        publishSubject.onNext(new Stock(GOOG, 723));
        publishSubject.onNext(new Stock(GOOG, 100));
        publishSubject.onNext(new Stock(GOOG, 699));
        publishSubject.onError(new Exception("BOOM!"));

        Subscription tardySubscription = publishSubject.subscribe(getTardySubscriber());
        compositeSubscription.add(tardySubscription);
    }

    /**
     * Simple subscriber for the default subject.
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
     * Simple subscriber that is used to subscribe to a subject after it has completed.
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
