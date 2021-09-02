package com.example.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner, Observer(observer))
}

inline fun <T> LiveData<List<T>>.filter(crossinline predicate: (T) -> Boolean): LiveData<List<T>> {

    return Transformations.map(this) { it.filter { predicate(it) } }
}