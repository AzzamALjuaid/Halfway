package com.tuwaiq.halfway.Geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tuwaiq.halfway.Geofence.GeofenceTransitionsJobIntentService

class GeofenceBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
  }
}