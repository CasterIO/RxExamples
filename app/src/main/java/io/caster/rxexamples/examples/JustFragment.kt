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

        // With Strings
        val disposable1 =
                Observable.just("One Fish", "Two Fish", "Red Fish", "Blue Fish")
                        .subscribe({ item ->
                            // onNext
                            content.text = "${content.text}\n$item"
                            Timber.d("Item: $item, Time: ${System.currentTimeMillis()}")
                        })

        compositeDisposable.add(disposable1)

        // With integers
        val disposable2 =
                Observable.just(1, 2, 3, 4)
                        .subscribe({ item ->
                            // onNext
                            content.text = "${content.text}\n$item"
                            Timber.d("Item: $item, Time: ${System.currentTimeMillis()}")
                        })

        compositeDisposable.add(disposable2)

        // With a complex object
        val fooSquare = FavoriteShape("Foo", FunShape("Red", "Square", 4))
        val barCircle = FavoriteShape("Bar", FunShape("Orange", "Circle", 0))
        val fizRectangle = FavoriteShape("Fiz", FunShape("Purple", "Rectangle", 4))
        val binTriangle = FavoriteShape("Bin", FunShape("Blue", "Triangle", 3))

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
                            // onComplete
                            Timber.i("onComplete")
                        })

        compositeDisposable.add(disposable3)

    }

    override fun onPause() {
        super.onPause()
        // Clean up our disposables
        compositeDisposable.clear()
    }
}

data class FavoriteShape(val favoriteName: String, val funShape: FunShape)

class FunShape(val color: String, val shape: String, val sides: Int) {
    override fun toString(): String {
        if (shape.equals("Circle")) {
            throw NotImplementedError("Circles are special, they don't have 'sides'.")
        } else {
            return "color: $color, shape: $shape, sides: $sides"
        }
    }
}

