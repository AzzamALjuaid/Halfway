package com.tuwaiq.halfway.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tuwaiq.halfway.databinding.ActivityMapsBinding
import com.tuwaiq.halfway.model.UserDetailModal
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_GENDER
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_LATITUDE
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_LONGITUDE
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_NAME
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdateFactory
import org.chat21.android.core.messages.models.LocationMessage
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.material.snackbar.Snackbar
import com.tuwaiq.halfway.App
import com.tuwaiq.halfway.Geofence.Reminder
import com.tuwaiq.halfway.Geofence.ReminderRepository
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.*


//this screen is currently not in use
open class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private  var mMap: GoogleMap?=null
    private lateinit var binding: ActivityMapsBinding
    private var userDetailModal :UserDetailModal? = null
    private var mRepository : ReminderRepository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mRepository= (application as App).getRepository()
        getData()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    fun getData() {
        binding.idPBLoading.visibility = View.VISIBLE
        intent.getParcelableExtra<UserDetailModal>("friend")?.let {
            userDetailModal=it
        }
//        binding.tvFriendName.text = userDetailModal?.name + " Location"

        binding.btnPlaces.setOnClickListener {

            (application as App).getRepository().getLast()?.let {reminder->
                var bundle = Bundle()

                reminder.latLng?.let {
                    bundle.putDouble("latitude", it?.latitude)
                    bundle.putDouble("longitude", it?.longitude)

                }
                bundle.putString("userName", reminder?.userName)

                Common.startNewActivity(
                    this,
                    PlaceActivity::class.java,
                    bundle,
                    false
                )
            }

        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(intent.hasExtra("locationLat")&&intent.hasExtra("locationLng")){
            mMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(intent.getDoubleExtra("locationLat",0.0),
                        intent.getDoubleExtra("locationLng",0.0)))
                    .anchor(0.5f, 0.5f)
                    .title(intent.getStringExtra("locationName"))
                    .icon(vectorToBitmap(resources, R.drawable.ic_other_location))
            )
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(intent.getDoubleExtra("locationLat",0.0),
                intent.getDoubleExtra("locationLng",0.0)),17f))
            binding.apply {
                btnPlaces.isVisible=false
//                llTop.isVisible=false
                btnSetReminder.isVisible=false
                idPBLoading.isVisible=false
            }

            return
        }
 intent.getParcelableExtra<LocationMessage>("locationMessage")?.let { locationsMessage ->


     binding.idPBLoading.isVisible = false

     binding.btnSetReminder.isVisible = mRepository?.getAll()?.filter {
             locationsMessage.lat == it.latLng?.latitude && locationsMessage.lon == it.latLng?.longitude
     }.isNullOrEmpty();
     showReminders()
     binding.btnSetReminder.setOnClickListener {
         checkPermessation()
     }

     mMap?.addMarker(
         MarkerOptions()
             .position(LatLng(locationsMessage.lat,locationsMessage.lon))
             .anchor(0.5f, 0.5f)
             .title("user locations")
             .snippet(locationsMessage.senderName)
             .icon(getBitmapDescriptor(R.drawable.ic_my_location))
     )
     mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationsMessage.lat,locationsMessage.lon),17f))

/*
     mMap.addMarker(
         MarkerOptions()
             .position(LatLng(it.lat,it.lon))
             .anchor(0.5f, 0.5f)
             .title("user locations")
             .snippet(it.senderName)
             .icon(getBitmapDescriptor(R.drawable.ic_my_location))
     )
     mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat,it.lon),17f))*/

 }?: run {


     val latlng: ArrayList<LatLng> = ArrayList()
     val name = PreferencesHelper(this).getString(USER_NAME)
     val gender = PreferencesHelper(this).getString(USER_GENDER)
     val lat = PreferencesHelper(this).getString(USER_LATITUDE)
     val lng = PreferencesHelper(this).getString(USER_LONGITUDE)
     if(!(lat.isNullOrBlank()||lng.isNullOrBlank())){
         latlng.add(LatLng(lat.toDouble(), lng.toDouble()))
     }

     userDetailModal?.let {
         latlng.add(
             LatLng(
                 it.latitude.toDouble(),
                 it.longitude.toDouble()
             )
         )
         val mid = midPoint(
             lat.toDouble(), lng.toDouble(), it.latitude.toDouble(),
             it.longitude.toDouble()
         )
         latlng.add(mid)

         createMarker(it, getBitmap(R.drawable.ic_other_location)!!)

         //        val placesSearchResults = NearbySearch().run(mid)!!.results
         for (item in NearbySearch().run(mid)!!.results)
             println("=========>" + item.formattedAddress!!)
         createMarker(
             UserDetailModal(
                 "Halfway",
                 "Halfway",
                 "",
                 mid.latitude.toString(),
                 mid.longitude.toString(),
                 ""
             ),
             getBitmap(R.drawable.ic_location_halfway)!!
         )
     }

     createMarker(
         UserDetailModal(
             name,
             gender,
             "",
             lat,
             lng,
             ""
         ),
         getBitmap(R.drawable.ic_my_location)!!
     )

     zoomInToAllMarker(latlng)

 }


    }

    private fun zoomInToAllMarker(latLngList: ArrayList<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (marker in latLngList) {
            builder.include(marker)
        }
        val bounds = builder.build()
        mMap?.setOnMapLoadedCallback {
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    150
                )
            )
            binding.idPBLoading.visibility = View.GONE
        }
    }

    private fun createMarker(userDetails: UserDetailModal, icon: Bitmap): Marker? {
        return mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(userDetails.latitude.toDouble(), userDetails.longitude.toDouble()))
                .anchor(0.5f, 0.5f)
                .title(userDetails.name)
                .snippet("Gender:" + userDetails.gender)
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
        )
    }

    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    private fun midPoint(lat1: Double, lon1: Double, lat2: Double, lon2: Double): LatLng {
        var lat1 = lat1
        var lon1 = lon1
        var lat2 = lat2
        val dLon = Math.toRadians(lon2 - lon1)

        //convert to radians
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)

        val Bx = Math.cos(lat2) * Math.cos(dLon)
        val By = Math.cos(lat2) * Math.sin(dLon)
        val lat3 = Math.atan2(
            Math.sin(lat1) + Math.sin(lat2),
            Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)
        )
        val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)

        //print out in degrees
        return LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3))
//        println(.toString() + " " + Math.toDegrees(lon3))
    }

    private fun getBitmapDescriptor(id: Int): BitmapDescriptor? {
        val vectorDrawable: Drawable = resources.getDrawable(id)
        val h = (24 * resources.displayMetrics.density).toInt();
        val w = (24 * resources.displayMetrics.density).toInt();
        vectorDrawable.setBounds(0, 0, w, h)
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }



    private fun addReminder(reminder: Reminder) {

        mRepository?.add(reminder,
            success = {
                showReminders()

                Snackbar.make(binding.root, "Reminder Added", Snackbar.LENGTH_LONG).show()
                binding.btnSetReminder.isVisible=false

                // setResult(Activity.RESULT_OK)
              //  finish()
            },
            failure = {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            })
    }
    private lateinit var locationManager: LocationManager
    private  val MY_LOCATION_REQUEST_CODE = 329

fun checkPermessation(){
    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_LOCATION_REQUEST_CODE)
    }else{
        onMapAndPermissionReady()
    }
}

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onMapAndPermissionReady()
        }
    }

    private fun onMapAndPermissionReady() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
        ) {
            mMap?.isMyLocationEnabled = true
            val bestProvider = locationManager.getBestProvider(Criteria(), false)
            val location = bestProvider?.let { locationManager.getLastKnownLocation(it) }

            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                intent.getParcelableExtra<LocationMessage>("locationMessage")?.let {

                    val mid = midPoint(
                        it.lat,it.lon, latLng.latitude,
                        latLng.longitude
                    )

                    addReminder( Reminder(latLng = mid, radius = 100.0, message = "You reach Half way to your friend ${it.senderName} ", userName = it.senderName)
                    )
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(mid, 15f))

                }


            }



        }
    }



    private fun showReminders() {
        mMap?.run {
            clear()

         /*   for (reminder in (application as App).getRepository().getAll()) {
                showReminderInMap(this@MapsActivity, this, reminder)
            }*/


            (application as App).getRepository().getLast()?.let {
             showReminderInMap(this@MapsActivity, this, it)
            }

        }
    }

}