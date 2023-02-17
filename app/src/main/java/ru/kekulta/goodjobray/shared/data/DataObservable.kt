package ru.kekulta.goodjobray.utils

interface DataObservable {
    fun addObserver(observer: () -> Unit)
    fun removeObserver(observer: () -> Unit)
    fun notifyObservers()
}
