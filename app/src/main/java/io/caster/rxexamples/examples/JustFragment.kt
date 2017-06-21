package io.caster.rxexamples.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.caster.rxexamples.R
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber


class JustFragment: Fragment() {
    lateinit var disposable: Disposable

    lateinit var content: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_just, container, false)
        content = view.findViewById(R.id.just_content) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Observable.just("One Fish", "Two Fish", "Red Fish", "Blue Fish")
                .subscribe({ item ->
                    // onNext
                    content.text = "${content.text}\n$item"
                    Timber.d("Item: $item, Time: ${System.currentTimeMillis()}")
                }, { ex ->
                    // onError
                    Timber.e(ex, ex.message)
                }, {
                    // onComplete, all done!
                    Timber.i("onComplete!")
                })
    }
}
