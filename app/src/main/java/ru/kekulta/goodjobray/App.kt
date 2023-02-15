package ru.kekulta.goodjobray

import android.app.Application
import ru.kekulta.goodjobray.di.DI


// TODO (17) Application -> странно что в пакете .di
internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.initDi(this)
    }
}


