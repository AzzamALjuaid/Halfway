package com.tuwaiq.halfway.utility

import com.google.android.gms.common.api.ApiException
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.RankBy
import com.google.android.gms.maps.model.LatLng as LatLng1
import java.io.IOException

class NearbySearch {
    fun run(centerPoint: LatLng1): PlacesSearchResponse? {
        var request = PlacesSearchResponse()
        val context: GeoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyDDDjqOOpgZ--gcRwPRW0NIbpK_ulX2EkM")
            .build()

        try {
            var mid = LatLng(centerPoint.latitude, centerPoint.longitude)
            request = PlacesApi.nearbySearchQuery(context, mid)
                .radius(1000)
                .rankby(RankBy.PROMINENCE)
                .keyword("cruise")
                .language("en")
                .type(PlaceType.AMUSEMENT_PARK)
                .await()
        } catch (e:ApiException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            return request
        }

    }

}