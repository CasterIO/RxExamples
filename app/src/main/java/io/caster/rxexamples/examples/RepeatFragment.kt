package io.caster.rxexamples.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.caster.rxexamples.R
import rx.Observable
import rx.Subscriber
import rx.Subscription
import timber.log.Timber

class RepeatFragment : Fragment() {

    lateinit var sub: Subscription

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_repeat, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sub = Observable.just("Foo", "Bar", "Fiz", "Bin")
                .repeat(5) // This will force the sequence to repeat 5 times in a row.
                .subscribe(object : Subscriber<String>() {
                    override fun onError(e: Throwable?) {
                        Timber.e(e, e?.message)
                    }

                    override fun onCompleted() {

                    }

                    override fun onNext(item: String?) {
                        Timber.d("Value $item, Time: ${System.currentTimeMillis()}")
                    }

                })

    }

    override fun onDestroy() {
        super.onDestroy()
        sub?.unsubscribe()
    }
}