package com.example.melichallenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MeliChallengeApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}