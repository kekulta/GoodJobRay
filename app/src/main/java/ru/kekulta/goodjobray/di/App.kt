package ru.kekulta.goodjobray.di

import android.app.Application


internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.initDi(this)
    }
}


