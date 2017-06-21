package io.caster.rxexamples.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.caster.rxexamples.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class JustFragment : Fragment() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    lateinit var content: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_just, container, false)
        content = view.findViewById(R.id.just_content) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // With strings
        val disposable1 =
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

        compositeDisposable.add(disposable1)

        // With integers
        val disposable2 =
                Observable.just(1, 2, 3, 4)
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
        compositeDisposable.add(disposable2)

        // With a complex object
        val fooSquare = JustAnotherObject("Foo", MyFavoriteObject("Red", "Square"))
        val barCircle = JustAnotherObject("Bar", MyFavoriteObject("Orange", "Circle"))
        val fizRectangle = JustAnotherObject("Fiz", MyFavoriteObject("Purple", "Rectangle"))
        val binTriangle = JustAnotherObject("Bin", MyFavoriteObject("Blue", "Triangle"))

        val disposable3 =
                Observable.just(fooSquare, barCircle, fizRectangle, binTriangle)
                        .subscribe({ item ->
                            // onNext
                            // .toString() is automatically called here, building the output
                            // you see on the screen.
                            content.text = "${content.text}\n$item"
                            Timber.d("Item: $item, Time: ${System.currentTimeMillis()}")
                        }, { ex ->
                            // onError
                            Timber.e(ex, ex.message)
                        }, {
                            // onComplete, all done!
                            Timber.i("onComplete!")
                        })

        compositeDisposable.add(disposable3)
    }

    override fun onPause() {
        super.onPause()

        compositeDisposable.clear()
    }
}



// A class we can use for an example
data class JustAnotherObject(var someProperty: String, var favoriteObject: MyFavoriteObject)

data class MyFavoriteObject(var myFavoriteColor: String, var favoriteShape: String)