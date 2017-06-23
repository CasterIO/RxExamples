package io.caster.rxexamples.examples.rxjava2.operators


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

/**
 * This example is in RxJava 2
 */
class RepeatFragment : Fragment() {

    lateinit var disposable: Disposable

    lateinit var content: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_repeat, container, false)
        content = view.findViewById(R.id.repeat_content) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        disposable =
                Observable.just("one", "two", "three", "four")
                        .repeat(5) // This will force the sequence to repeat 5 times in a row.
                        .subscribe({ item ->
                            // Consumer<String>
                            // onNext
                            content.text = "${content.text}\n$item"
                            Timber.d("Value $item, Time: ${System.currentTimeMillis()}")
                        }, { ex ->
                            // Consumer<? extends Throwable>
                            // onError
                            Timber.e(ex, "Oh No!")
                        }, {
                            // Action
                            // onComplete
                            Timber.i("onComplete")
                            content.text = "${content.text}\nonComplete"
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}