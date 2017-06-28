package io.caster.rxexamples.examples.rxjava2.operators

import io.reactivex.Observable
import timber.log.Timber


class JustFragment : android.support.v4.app.Fragment() {
    var compositeDisposable: io.reactivex.disposables.CompositeDisposable = io.reactivex.disposables.CompositeDisposable()

    lateinit var content: android.widget.TextView

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        val view = inflater.inflate(io.caster.rxexamples.R.layout.fragment_just, container, false)
        content = view.findViewById(io.caster.rxexamples.R.id.just_content) as android.widget.TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: android.os.Bundle?) {
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
        val fooSquare = io.caster.rxexamples.examples.rxjava2.operators.FavoriteShape("Foo", FunShape("Red", "Square", 4))
        val barCircle = io.caster.rxexamples.examples.rxjava2.operators.FavoriteShape("Bar", FunShape("Orange", "Circle", 0))
        val fizRectangle = io.caster.rxexamples.examples.rxjava2.operators.FavoriteShape("Fiz", FunShape("Purple", "Rectangle", 4))
        val binTriangle = io.caster.rxexamples.examples.rxjava2.operators.FavoriteShape("Bin", FunShape("Blue", "Triangle", 3))

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

        // .just with a List
        // listOf gives us an immutable List<T>
        val listOfPrimitives = listOf("Sally", "Joe", "Juma", "Ritu")
        var disposable4 = Observable.just(listOfPrimitives)
                .subscribe({ item ->
                    // onNext
                    content.text = "${content.text}\n$item"
                    Timber.d("Item: $item, Time: ${System.currentTimeMillis()}")
                })

        compositeDisposable.add(disposable4)

    }

    override fun onPause() {
        super.onPause()
        // Clean up our disposables
        compositeDisposable.clear()
    }
}

data class FavoriteShape(val favoriteName: String, val funShape: io.caster.rxexamples.examples.rxjava2.operators.FunShape)

class FunShape(val color: String, val shape: String, val sides: Int) {
    override fun toString(): String {
        if (shape.equals("Circle")) {
            throw NotImplementedError("Circles are special, they don't have 'sides'.")
        } else {
            return "color: $color, shape: $shape, sides: $sides"
        }
    }
}

