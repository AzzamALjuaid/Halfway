package com.tuwaiq.halfway.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
import android.os.Looper
import android.text.TextUtils

import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_EMAIL
import com.tuwaiq.halfway.utility.PreferencesHelper
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast

import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.model.UserDetailModal
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_AGE
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_GENDER
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_ID
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_LATITUDE
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_LONGITUDE
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_NAME

class AddDetailActivity : AppCompatActivity() {
    private var loading: ProgressBar? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var gender: String? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_detail)
        initView()
    }

    private fun initView() {
        loading = findViewById(R.id.idPBLoading)
//        println("========>"+PreferencesHelper(this@AddDetailActivity).getString(USER_NAME))
        var et_email = findViewById<EditText>(R.id.et_email)
        var et_name = findViewById<EditText>(R.id.et_name)
        var et_age = findViewById<EditText>(R.id.et_age)
        var rb_male = findViewById<RadioButton>(R.id.rb_male)
        var rb_female = findViewById<RadioButton>(R.id.rb_female)
        et_email.setText(PreferencesHelper(this@AddDetailActivity).getString(USER_EMAIL))
        if (!TextUtils.isEmpty(PreferencesHelper(this@AddDetailActivity).getString(USER_NAME)))
            et_name.setText(PreferencesHelper(this@AddDetailActivity).getString(USER_NAME))
        if (!TextUtils.isEmpty(PreferencesHelper(this@AddDetailActivity).getString(USER_AGE)))
            et_age.setText(PreferencesHelper(this@AddDetailActivity).getString(USER_AGE))
        if (!TextUtils.isEmpty(PreferencesHelper(this@AddDetailActivity).getString(USER_GENDER))) {
            gender = PreferencesHelper(this@AddDetailActivity).getString(USER_GENDER)
            if ( gender.equals("male", true)) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        }
        if (!TextUtils.isEmpty(PreferencesHelper(this@AddDetailActivity).getString(USER_LATITUDE))) {
            latitude = PreferencesHelper(this@AddDetailActivity).getString(USER_LATITUDE)
            longitude = PreferencesHelper(this@AddDetailActivity).getString(USER_LONGITUDE)
        }

        val btn_location = findViewById<Button>(R.id.btn_location)
        val btn_save = findViewById<Button>(R.id.btn_save)
        val rg_gender = findViewById<RadioGroup>(R.id.rg_gender)
        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line creating our database reference.
        databaseReference = firebaseDatabase?.getReference("account");

        rg_gender.setOnCheckedChangeListener { group, checkedId ->
            println("========>gender")
            gender = when (checkedId) {
                R.id.rb_male -> "Male"
                R.id.rb_female -> "Female"
                else -> ""
            }
        }

        btn_location.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION
                )
            } else {
                getCurrentLocation();
            }
        }

        btn_save.setOnClickListener {
            if (validateData())
                saveData()
        }
    }

    //validation before saving the data
    private fun validateData(): Boolean {
        val et_name = findViewById<EditText>(R.id.et_name).text.toString()
        val et_age = findViewById<EditText>(R.id.et_age).text.toString()
        if (TextUtils.isEmpty(et_name)) {
            Common.showToast(
                this,
                "Please enter the name"
            )
            return false
        } else if (TextUtils.isEmpty(gender)) {
            Common.showToast(
                this,
                "Please select the $gender"
            )
            return false
        } else if (TextUtils.isEmpty(et_age)) {
            Common.showToast(
                this,
                "Please enter the age"
            )
            return false
        } else if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
            Common.showToast(
                this,
                "Please select the current location"
            )
            return false
        }
        return true
    }

    //saving the data to the firebase db
    private fun saveData() {
        val id = PreferencesHelper(this@AddDetailActivity).getString(USER_ID)
        val et_name = findViewById<EditText>(R.id.et_name).text.toString()
        val et_age = findViewById<EditText>(R.id.et_age).text.toString()
        val et_email = findViewById<EditText>(R.id.et_email).text.toString()
        val courseRVModal = UserDetailModal(
            et_name,
            gender,
            et_age,
            latitude,
            longitude,
            et_email
        )
        PreferencesHelper(this@AddDetailActivity).putString(
            USER_LATITUDE,
            latitude!!
        )
        PreferencesHelper(this@AddDetailActivity).putString(
            USER_LONGITUDE,
            longitude!!
        )
        PreferencesHelper(this@AddDetailActivity).putString(USER_NAME, et_name)
        PreferencesHelper(this@AddDetailActivity).putString(USER_AGE, et_age)
        PreferencesHelper(this@AddDetailActivity).putString(USER_GENDER, gender!!)
        //on below line we are calling a add value event to pass data to firebase database.
        //on below line we are calling a add value event to pass data to firebase database.
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //on below line we are setting data in our firebase database.
                databaseReference!!.child(id).setValue(courseRVModal)
                //displaying a toast message.
                Toast.makeText(
                    this@AddDetailActivity,
                    "Account Updated Successfully..",
                    Toast.LENGTH_SHORT
                ).show()
                //starting a main activity.
            }

            override fun onCancelled(error: DatabaseError) {
                //displaying a failure message on below line.
                Toast.makeText(this@AddDetailActivity, "Fail to add Course..", Toast.LENGTH_SHORT)
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
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show()
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
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
        LocationServices.getFusedLocationProviderClient(this@AddDetailActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
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

}