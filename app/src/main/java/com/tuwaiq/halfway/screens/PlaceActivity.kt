package com.tuwaiq.halfway.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import com.tuwaiq.halfway.service.GoogleMapAPI
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.tuwaiq.halfway.Geofence.Reminder
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.adapter.PlaceAdapter
import com.tuwaiq.halfway.adapter.TabsAdapter
import com.tuwaiq.halfway.databinding.ActivityPlaceBinding

import com.tuwaiq.halfway.service.APIClient.client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tuwaiq.halfway.model.Result
import com.tuwaiq.halfway.model.UserDetailModal
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper
import com.tuwaiq.halfway.model.NearByLocation
import org.chat21.android.core.messages.models.LocationMessage


class PlaceActivity : AppCompatActivity() {
    private  var mid: LatLng?=null
    private lateinit var binding: ActivityPlaceBinding
    private var userDetailModal = UserDetailModal()
    private var adapter: PlaceAdapter? = null
    private var locationList = ArrayList<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()



    }

    //setting up the data to show details of the nearby places
    private fun getData() {
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val userName = intent.getStringExtra("userName")?:"My"

        if (intent.hasExtra("latitude")&&intent.hasExtra("longitude")){
            myLocation= LatLng(latitude,longitude)

            binding.tvFriendName.text = "$userName"+ getString(R.string.location)

        }else{
            binding.tvFriendName.text = getString(R.string.my_location)


            checkPermessation();
        }

        binding.idPBLoading.visibility = View.VISIBLE

        adapter = PlaceAdapter(this, locationList, onItemClicked = {

            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=${myLocation?.latitude},${myLocation?.longitude}&daddr=${it.geometry.location.lat},${it.geometry.location.lng}")
                )
                startActivity(intent)
            }catch (ex:Exception){

            }

            startActivity(Intent(this, MapsActivity::class.java).apply {
                putExtra("locationLat",it.geometry.location.lat)
                putExtra("locationLng",it.geometry.location.lng)
                putExtra("locationName",it.name)

            })
        })

        var linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvPlace.layoutManager = linearLayoutManager
        binding!!.rvPlace.adapter = adapter

        binding.tabs.apply {
            layoutManager=LinearLayoutManager(this@PlaceActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter=TabsAdapter(this@PlaceActivity, tabs = listOf(
                PlaceType.RESTAURANT.toString(),
                PlaceType.CAFE.toString(),
                PlaceType.MOVIE_THEATER.toString(),
                PlaceType.BANK.toString(),
                PlaceType.STORE.toString(),
            ), onItemClicked = {
                getLocationData("${myLocation?.latitude},${myLocation?.longitude}", it)
            }
            )
        }

    }

    //fetching the data from the server with the help of the api call of nearBySearch of google
    private fun getLocationData(location: String, type: String) {
        binding.idPBLoading.visibility = View.VISIBLE
        locationList.clear()
        val key = getText(R.string.google_maps_key).toString()
        val radius = 2000
        val googleMapAPI = client!!.create(GoogleMapAPI::class.java)
        googleMapAPI.getNearBy(location, radius, type, key)!!
            .enqueue(object : Callback<NearByLocation?> {
                override fun onResponse(
                    call: Call<NearByLocation?>?,
                    response: Response<NearByLocation?>
                ) {
                    if (response.isSuccessful) {
                        println("===========>" + response.body()?.results.toString())
                        locationList.addAll(response.body()?.results as ArrayList<Result>)
                        adapter?.notifyDataSetChanged()
                        binding.idPBLoading.visibility = View.GONE
                        binding.tvEmpty.visibility = View.GONE
                        println("===========>" + locationList.size)
                        response.body()?.results.let {
                            if (it.isNullOrEmpty()){
                                binding.tvEmpty.visibility = View.VISIBLE
                                binding.tvEmpty.text=getString(R.string.no_Items)
                            }else{
                                binding.tvEmpty.visibility = View.GONE
                            }

                        }

                    } else {
                        Toast.makeText(applicationContext, getString(R.string.failed), Toast.LENGTH_LONG).show()
                        binding.idPBLoading.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.tvEmpty.text=getString(R.string.no_Items)

                    }
                }

                override fun onFailure(call: Call<NearByLocation?>?, t: Throwable) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text=getString(R.string.no_Items)

                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    binding.idPBLoading.visibility = View.GONE
                }
            })
    }


    private lateinit var locationManager: LocationManager
    private  val MY_LOCATION_REQUEST_CODE = 329
    fun checkPermessation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
    var  myLocation: LatLng?=null

    private fun onMapAndPermissionReady() {


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val bestProvider = locationManager.getBestProvider(Criteria(), false)
            val location= bestProvider?.let { locationManager.getLastKnownLocation(it) }
            if(location!=null){
                myLocation=LatLng(location.latitude,location.longitude)
            }


            }



        }



}