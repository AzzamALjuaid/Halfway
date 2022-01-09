package com.tuwaiq.halfway.Geofence

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsJobIntentService : JobIntentService() {

    companion object {

        private const val LOG_TAG = "GeoTrIntentService"

        private const val JOB_ID = 555

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent)

        }
    }

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()){
            val errorMessage = GeofenceErrorMessages.getErrorString(
                this,
                geofencingEvent.errorCode
            )
            Log.e(LOG_TAG, errorMessage)
            return
        }
        handleEvent(geofencingEvent)
    }

    private fun handleEvent(event: GeofencingEvent) {
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val reminders = getFirstReminder(event.triggeringGeofences)
            val message = reminders?.message
            val latLng = reminders.LatLng
            if (message != null && latLng != null) {
                sendNotifivation(this, message , latLng)
            }
        }
    }

    private fun getFirstReminder(triggeringGeofence: List<Geofence>) : Reminder? {
        val firstGeofence = triggeringGeofence[0]
        return (application as App).getRepository().get(firstGeofence.requestId)
    }

}