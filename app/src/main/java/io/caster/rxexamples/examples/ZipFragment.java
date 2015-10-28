package io.caster.rxexamples.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.UUID;

import io.caster.rxexamples.models.Customer;
import io.caster.rxexamples.models.CustomerOrderInfo;
import io.caster.rxexamples.models.Order;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func2;

public class ZipFragment extends Fragment {

    public static ZipFragment newInstance() {
        return new ZipFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Just some view
        return new LinearLayout(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Observable.zip(getCustomer(), getOrder(), new Func2<Customer, Order, CustomerOrderInfo>() {
            @Override
            public CustomerOrderInfo call(Customer customer, Order order) {
                return new CustomerOrderInfo(customer.getId(), order.getId());
            }
        }).subscribe(new Subscriber<CustomerOrderInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CustomerOrderInfo customerOrderInfo) {
                Log.d("RX", "Got the customer info");
            }
        });

    }



    public Observable<Customer> getCustomer() {
        return Observable.defer(new Func0<Observable<Customer>>() {
            @Override
            public Observable<Customer> call() {
                return Observable.just(new Customer("Foo Bar", UUID.randomUUID().toString()));
            }
        });
    }


    public Observable<Order> getOrder() {
        return Observable.defer(new Func0<Observable<Order>>() {
            @Override
            public Observable<Order> call() {
                return Observable.just(new Order(UUID.randomUUID().toString(), 10000));
            }
        });
    }
}
