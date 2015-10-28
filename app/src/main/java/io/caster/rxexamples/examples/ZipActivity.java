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



    }

}
