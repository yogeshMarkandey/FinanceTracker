package com.example.financetracker.presentation.application

import android.app.Application
import com.example.financetracker.presentation.common.IconHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinanceTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        IconHelper.init()
    }
}