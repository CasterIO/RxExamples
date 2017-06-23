package io.caster.rxexamples.examples.rxjava1.operators;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.caster.rxexamples.R;
import io.caster.rxexamples.models.Customer;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class AmbFragment extends Fragment {

    private Thread firstThread;
    private Thread secondThread;

    @BindView(R.id.amb_results) protected TextView results;

    public static AmbFragment newInstance() {
        return new AmbFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Just some view
        View v = inflater.inflate(R.layout.fragment_amb, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.amb_execute)
    public void onExecuteClick(final View v) {

        killThreads();

        Observable.amb(getCustomerFromServerOne(), getCustomerFromServerTwo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        v.setEnabled(false);
                    }
                })
                .subscribe(new Subscriber<Customer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("AMB", e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Customer customer) {
                        Log.d("AMB", customer.getName());
                        results.setText(results.getText() + "\n" + customer.getName());
                        unsubscribe();
                        v.setEnabled(true);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public Observable<Customer> getCustomerFromServerOne() {
        return getCustomerOne();
    }

    public Observable<Customer> getCustomerFromServerTwo() {
        return getCustomerTwo();
    }

    @NonNull
    private Observable<Customer> getCustomerOne() {
        return Observable.defer(new Func0<Observable<Customer>>() {
            @Override
            public Observable<Customer> call() {
                return Observable.create(new Observable.OnSubscribe<Customer>() {
                    @Override
                    public void call(final Subscriber<? super Customer> subscriber) {
                        if (subscriber != null && !subscriber.isUnsubscribed()) {
                            firstThread =
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(getRandomTime());
                                                subscriber.onNext(new Customer("Customer One", UUID.randomUUID().toString()));
                                                subscriber.onCompleted();
                                            } catch (InterruptedException e) {
                                                subscriber.onError(e);
                                            }
                                        }
                                    });

                            firstThread.start();
                        }
                    }
                });
            }
        });

    }

    @NonNull
    private Observable<Customer> getCustomerTwo() {
        return Observable.defer(new Func0<Observable<Customer>>() {
            @Override
            public Observable<Customer> call() {
                return Observable.create(new Observable.OnSubscribe<Customer>() {
                    @Override
                    public void call(final Subscriber<? super Customer> subscriber) {
                        if (subscriber != null && !subscriber.isUnsubscribed()) {
                            secondThread =
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(getRandomTime());
                                                subscriber.onNext(new Customer("Customer Two", UUID.randomUUID().toString()));
                                                subscriber.onCompleted();
                                            } catch (InterruptedException e) {
                                                subscriber.onError(e);
                                            }
                                        }
                                    });

                            secondThread.start();
                        }
                    }
                });
            }
        });

    }

    public long getRandomTime() {
        Random ran = new Random();
        int x = ran.nextInt(500) + 1000;
        Log.d("AMB", "Random time: " + x);
        return x;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killThreads();
    }

    private void killThreads() {
        if(firstThread != null) {
            firstThread.interrupt();
        }

        if(secondThread != null) {
            secondThread.interrupt();
        }
    }
}
