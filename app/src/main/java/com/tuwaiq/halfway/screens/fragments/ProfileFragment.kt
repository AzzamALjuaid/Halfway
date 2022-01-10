package com.tuwaiq.halfway.screens.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.databinding.FragmentProfileBinding
import com.tuwaiq.halfway.model.UserDetailModal
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ProfileFragment : Fragment() {
    private var loading: ProgressBar? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var gender: String? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    lateinit var mView: FragmentProfileBinding;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = FragmentProfileBinding.bind(view);
        iniView()
    }

    private fun iniView(){

        mView.etEmail.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_EMAIL))
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_NAME)))
            mView.etName.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_NAME))
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_AGE)))
            mView.etAge.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_AGE))
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_GENDER))) {
            gender = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_GENDER)
            if ( gender.equals("male", true)) {
                mView.rbMale.isChecked = true
            } else {
                mView.rbFemale.isChecked = true
            }
        }
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LATITUDE))) {
            latitude = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LATITUDE)
            longitude = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LONGITUDE)
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line creating our database reference.
        databaseReference = firebaseDatabase?.getReference("account");

        mView.rgGender.setOnCheckedChangeListener { group, checkedId ->
            println("========>gender")
            gender = when (checkedId) {
                R.id.rb_male -> "Male"
                R.id.rb_female -> "Female"
                else -> ""
            }
        }

        mView.btnLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constant.SharedPref.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
            } else {
                getCurrentLocation();
            }
        }

        mView.btnSave.setOnClickListener {
            if (validateData())
                saveData()
        }
    }

    //validation before saving the data
    private fun validateData(): Boolean {

        if (TextUtils.isEmpty(mView.etName.text.toString())) {
            Common.showToast(
                requireActivity(),
                "Please enter the name"
            )
            return false
        } else if (TextUtils.isEmpty(gender)) {
            Common.showToast(
                requireActivity(),
                "Please select the $gender"
            )
            return false
        } else if (TextUtils.isEmpty(mView.etAge.text.toString())) {
            Common.showToast(
                requireActivity(),
                "Please enter the age"
            )
            return false
        } else if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
            Common.showToast(
                requireActivity(),
                "Please select the current location"
            )
            return false
        }
        return true
    }

    //saving the data to the firebase db
    private fun saveData() {
        val id = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_ID)
        val et_name = mView.etName.text.toString()
        val et_age = mView.etAge.text.toString()
        val et_email = mView.etEmail.text.toString()


        val courseRVModal = UserDetailModal(
            et_name,
            gender,
            et_age,
            latitude,
            longitude,
            et_email
        )
        PreferencesHelper(requireContext()).putString(
            Constant.SharedPref.USER_LATITUDE,
            latitude!!
        )
        PreferencesHelper(requireContext()).putString(
            Constant.SharedPref.USER_LONGITUDE,
            longitude!!
        )
        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_NAME, et_name)
        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_AGE, et_age)
        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_GENDER, gender!!)
        //on below line we are calling a add value event to pass data to firebase database.
        //on below line we are calling a add value event to pass data to firebase database.
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //on below line we are setting data in our firebase database.
                databaseReference!!.child(id).setValue(courseRVModal)
                //displaying a toast message.
                Toast.makeText(
                    requireContext(),
                    "Account Updated Successfully..",
                    Toast.LENGTH_SHORT
                ).show()
                //starting a main activity.
            }

            override fun onCancelled(error: DatabaseError) {
                //displaying a failure message on below line.
                Toast.makeText(requireContext(), "Fail to add Course..", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

    //permission check or location
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.SharedPref.PERMISSION_REQUEST_ACCESS_FINE_LOCATION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Permission is denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //fetching the current location of the user
    private fun getCurrentLocation() {
        loading?.visibility = View.VISIBLE
        val locationRequest = LocationRequest()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(3000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(requireContext())
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.getLocations().size > 0) {
                        val latestlocIndex: Int = locationResult.getLocations().size - 1
                        val lati: Double =
                            locationResult.getLocations().get(latestlocIndex).getLatitude()
                        val longi: Double =
                            locationResult.getLocations().get(latestlocIndex).getLongitude()
                        latitude = "$lati"
                        longitude = "$longi"
                        val location = Location("providerNA")
                        location.setLongitude(longi)
                        location.setLatitude(lati)
                        loading?.setVisibility(View.GONE)
                    } else {
                        loading?.setVisibility(View.GONE)
                    }
                }
            }, Looper.getMainLooper())
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {

            }
    }
}