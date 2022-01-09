package com.tuwaiq.halfway

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.tuwaiq.halfway.Geofence.ReminderRepository

class App : Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        repository = ReminderRepository(this)
    }

    fun getRepository() = repository



}