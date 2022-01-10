package com.tuwaiq.halfway.utility

object Constant {
    private  val GEOFENCE_EXPIRATION_IN_HOURS: Long = 12

    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000
    const val GEOFENCE_RADIUS_IN_METERS = 1609f // 1 mile, 1.6 km

    interface SharedPref {
        companion object {
            const val SHARED_PREF = "SHARED_PREF_HALFWAY"
            const val USER_ID = "USER_ID"
            const val USER_EMAIL = "USER_EMAIL"
            const val USER_NAME = "USER_NAME"
            const val USER_AGE = "USER_AGE"
            const val USER_GENDER = "USER_GENDER"
            const val USER_LATITUDE = "USER_LATITUDE"
            const val USER_LONGITUDE = "USER_LONGITUDE"
            const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100


        }

    }
}