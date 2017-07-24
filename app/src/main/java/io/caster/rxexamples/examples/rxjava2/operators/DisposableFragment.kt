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
import io.reactivex.observers.DisposableObserver
import timber.log.Timber

class DisposableFragment : Fragment() {

    lateinit var content: TextView
    lateinit var disposable: Disposable
    lateinit var disposableObserver: DisposableObserver<Int>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_disposable, container, false)
        content = view?.findViewById(R.id.disposable_content) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // disposable is a LambdaObserver, which implements Disposable
        disposable = Observable.just(1, 2, 3, 4)
                .subscribe({ item ->
                    // onNext
                    content.text = "${content.text}\n$item"
                    Timber.d("onNext: $item")
                })

        disposableObserver = Observable.just(5, 6, 7, 8)
                .subscribeWith(object: DisposableObserver<Int>() {
                    override fun onError(e: Throwable?) {
                        Timber.e(e, e?.message)
                    }

                    override fun onComplete() {
                        Timber.i("onComplete disposable observer")
                    }

                    override fun onNext(item: Int?) {
                        content.text = "${content.text}\n$item"
                        Timber.d("onNext: $item ")
                    }
                })
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
        disposableObserver.dispose()

        Timber.d("Disposed")
    }
}