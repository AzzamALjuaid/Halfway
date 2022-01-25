package com.tuwaiq.halfway.Geofence

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Reminder(val id: String = UUID.randomUUID().toString(),
                    var latLng: LatLng?,
                    var radius: Double?,
                    var message: String?,
                    var userName: String?,
                    var currentUserName: String?,
                    var userToken: String?,
)