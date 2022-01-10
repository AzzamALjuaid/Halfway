package com.tuwaiq.halfway.utility

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object Test {
    fun GetCentralGeoCoordinate(geoCoordinates: List<LatLng>): LatLng {
        var x = 0.0
        var y = 0.0
        var z = 0.0

        for (geoCoordinate in geoCoordinates) {
            val latitude = (geoCoordinate.latitude
                    * (Math.PI / 180))
            val longitude = (geoCoordinate.longitude
                    * (Math.PI / 180))
            x = x + Math.cos(latitude) * Math.cos(longitude)
            y = y + Math.cos(latitude) * Math.sin(longitude)
            z = z + Math.sin(latitude)
        }
        val total = geoCoordinates.size.toDouble()
        x = x / total
        y = y / total
        z = z / total
        val centralLongitude = Math.atan2(y, x)
        val centralSquareRoot = Math.sqrt(
            x * x + y * y
        )
        val centralLatitude = Math.atan2(z, centralSquareRoot)
        var latitude = Location.convert(centralLatitude * (180 / Math.PI),Location.FORMAT_DEGREES)
        var longitude = Location.convert(centralLongitude * (180 / Math.PI),Location.FORMAT_DEGREES)
        println("===*****====>" + latitude)
        println("===*****====>" + longitude)
        return LatLng(centralLatitude * (180 / Math.PI),centralLongitude * (180 / Math.PI))
    }
}