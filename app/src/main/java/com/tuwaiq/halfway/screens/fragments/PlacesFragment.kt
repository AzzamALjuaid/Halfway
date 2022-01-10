package com.tuwaiq.halfway.screens.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.adapter.PlaceAdapter
import com.tuwaiq.halfway.adapter.TabsAdapter
import com.tuwaiq.halfway.model.NearByLocation
import com.tuwaiq.halfway.service.APIClient
import com.tuwaiq.halfway.service.GoogleMapAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [PlacesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlacesFragment : Fragment() {

    private lateinit var binding: FragmentPlacesBinding
    private var adapter: PlaceAdapter? = null
    private var locationList = ArrayList<Result>()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentPlacesBinding.bind(view)
        getData
    }

    //setting up the data to show details of the nearby places
    private fun getData() {

        arguments?.let {

            val latitude = it.getDouble("latitude", 0.0)
            val longitude = it.getDouble("longitude", 0.0)
            val userName = it.getString("userName")

            if (it.containsKey("latitude")&&it.containsKey("longitude")){
                myLocation= LatLng(latitude,longitude)
                binding.tvFriendName.text = "$userName location"

            }else{
                binding.tvFriendName.text = "My Location"

                checkPermessation();
            }
        }?: run  {
            binding.tvFriendName.text = "My Location"
            checkPermessation();
        }


        binding.idPBLoading.visibility = View.VISIBLE

        adapter = PlaceAdapter(requireContext(), locationList, onItemClicked = {


            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=${myLocation?.latitude},${myLocation?.longitude}&daddr=${it.geometry.location.lat},${it.geometry.location.lng}")
                )
                startActivity(intent)
            }catch (ex:Exception){

            }
            /* startActivity(Intent(context, MapsActivity::class.java).apply {
                 putExtra("locationLat",it.geometry.location.lat)
                 putExtra("locationLng",it.geometry.location.lng)
                 putExtra("locationName",it.name)

             })*/
        })

        var linearLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.rvPlace.layoutManager = linearLayoutManager
        binding!!.rvPlace.adapter = adapter

        binding.tabs.apply {
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter= TabsAdapter(requireContext(), tabs = listOf(
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
        val googleMapAPI = APIClient.client!!.create(GoogleMapAPI::class.java)
        googleMapAPI.getNearBy(location, radius, type, key)!!
            .enqueue(object : Callback<NearByLocation?> {
                override fun onResponse(
                    call: Call<NearByLocation?>?,
                    response: Response<NearByLocation?>
                ) {
                    if (response.isSuccessful) {
                        println("===========>" + response.body()?.results.toString())
                        locationList.addAll(response.body()?.results as ArrayList<com.tuwaiq.halfway.model.Result>)
                        adapter?.notifyDataSetChanged()
                        binding.idPBLoading.visibility = View.GONE
                        binding.tvEmpty.visibility = View.GONE
                        println("===========>" + locationList.size)
                        response.body()?.results.let {
                            if (it.isNullOrEmpty()){
                                binding.tvEmpty.visibility = View.VISIBLE
                                binding.tvEmpty.text="No Items"
                            }else{
                                binding.tvEmpty.visibility = View.GONE
                            }

                        }

                    } else {
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                        binding.idPBLoading.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.tvEmpty.text="No Items"

                    }
                }

                override fun onFailure(call: Call<NearByLocation?>?, t: Throwable) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text="No Items"

                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    binding.idPBLoading.visibility = View.GONE
                }
            })
    }

    private lateinit var locationManager: LocationManager
    private  val MY_LOCATION_REQUEST_CODE = 329
    fun checkPermessation(){
        locationManager =requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val bestProvider = locationManager.getBestProvider(Criteria(), false)
            val location= bestProvider?.let { locationManager.getLastKnownLocation(it) }
            if(location!=null){
                myLocation= LatLng(location.latitude,location.longitude)
            }


        }



    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlacesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlacesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        @JvmStatic
        fun newInstance() =
            PlacesFragment().apply {
            }
    }



}