package io.caster.rxexamples.examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.UUID;

import io.caster.rxexamples.R;
import io.caster.rxexamples.models.Customer;
import io.caster.rxexamples.models.CustomerOrderInfo;
import io.caster.rxexamples.models.Order;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func2;

public class ZipActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);


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
