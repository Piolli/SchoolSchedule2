package com.akamyshev.schoolschedule2

import android.app.Application
import io.realm.Realm
import io.realm.log.RealmLog

/**
 * Created by alexandr on 16.08.17.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}