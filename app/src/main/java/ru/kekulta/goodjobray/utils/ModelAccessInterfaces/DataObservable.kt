package ru.kekulta.goodjobray.utils.ModelAccessInterfaces

interface DataObservable {
    fun addObserver(observer: () -> Unit)
    fun removeObserver(observer: () -> Unit)
    fun notifyObservers()
}
