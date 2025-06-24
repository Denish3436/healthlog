package com.denish3436.healthlog

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class HealthApplication : Application() {
    
    private lateinit var analytics: FirebaseAnalytics
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase Analytics
        analytics = FirebaseAnalytics.getInstance(this)
        
        // Enable Crashlytics collection
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        
        // Log app start event
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }
}