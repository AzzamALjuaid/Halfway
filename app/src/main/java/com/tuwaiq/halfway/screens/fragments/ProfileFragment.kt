package com.tuwaiq.halfway.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.databinding.FragmentProfileBinding
import com.tuwaiq.halfway.screens.LoginActivity
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "ProfileFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    val AppId="halfway-7f6ca"
    val firebaseUrl="https://halfway-7f6ca-default-rtdb.firebaseio.com"
    val storageBucket="gs://halfway-7f6ca.appspot.com"
    private lateinit var binding: FragmentProfileBinding
    val currentUser = FirebaseAuth.getInstance().currentUser!!

    val userName = currentUser.displayName.toString()
    val userEmail = currentUser.email.toString()

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater)


        return binding.root
    }

    lateinit var mView:FragmentProfileBinding;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mView = FragmentProfileBinding.bind(view);
        initView()
    }

    private fun initView() {

        binding.etName.setText(userName)
        mView.etEmail.setText(userEmail)

        mView.etEmail.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_EMAIL))
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_NAME)))
            mView.etName.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_NAME))
//        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_AGE)))
//            mView.etAge.setText(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_AGE))
//        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_GENDER))) {
//            gender = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_GENDER)
//            if ( gender.equals("male", true)) {
//                mView.rbMale.isChecked = true
//            } else {
//                mView.rbFemale.isChecked = true
//            }
//        }
        if (!TextUtils.isEmpty(PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LATITUDE))) {
            latitude = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LATITUDE)
            longitude = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_LONGITUDE)
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line creating our database reference.
        databaseReference = firebaseDatabase?.getReference("account");

//        mView.rgGender.setOnCheckedChangeListener { group, checkedId ->
//            println("========>gender")
//            gender = when (checkedId) {
//                R.id.rb_male -> "Male"
//                R.id.rb_female -> "Female"
//                else -> ""
//            }
//        }

//        mView.btnLocation.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    Constant.SharedPref.PERMISSION_REQUEST_ACCESS_FINE_LOCATION
//                )
//            } else {
//
//            }
//        }

        mView.btnSave.setOnClickListener {
//            if (validateData())
            saveData()
        }
    }

    //validation before saving the data
//    private fun validateData(): Boolean {
//
//        if (TextUtils.isEmpty(mView.etName.text.toString())) {
//            Common.showToast(
//                requireActivity(),
//                "Please enter the name"
//            )
//            return false
//        } else if (TextUtils.isEmpty(gender)) {
//            Common.showToast(
//                requireActivity(),
//                "Please select the $gender"
//            )
//            return false
//        } else if (TextUtils.isEmpty(mView.etAge.text.toString())) {
//            Common.showToast(
//                requireActivity(),
//                "Please enter the age"
//            )
//            return false
//        } else if (TextUtils.isEmpty(latitude) && TextUtils.isEmpty(longitude)) {
//            Common.showToast(
//                requireActivity(),
//                "Please select the current location"
//            )
//            return false
//        }
//        return true
//    }

    //saving the data to the firebase db
    private fun saveData() {

        val editUserName = mView.etName.text.toString()


        val userName = currentUser.displayName.toString()


//        databaseReference = FirebaseDatabase.getInstance().getReference(userName)

        databaseReference?.child(userName)?.setValue(editUserName)?.addOnSuccessListener {

            Toast.makeText(context,"success",Toast.LENGTH_LONG).show()

        }


//        val id = PreferencesHelper(requireContext()).getString(Constant.SharedPref.USER_ID)
//        val et_name = mView.etName.text.toString()
////        val et_age = mView.etAge.text.toString()
//        val et_email = mView.etEmail.text.toString()
//
//
////        val courseRVModal = UserDetailModal(
////            et_name,
////            gender,
////            et_age,
////            latitude,
////            longitude,
////            et_email
////        )
//        PreferencesHelper(requireContext()).putString(
//            Constant.SharedPref.USER_LATITUDE,
//            latitude!!
//        )
//        PreferencesHelper(requireContext()).putString(
//            Constant.SharedPref.USER_LONGITUDE,
//            longitude!!
//        )
//        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_NAME, et_name)
////        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_AGE, et_age)
//        PreferencesHelper(requireContext()).putString(Constant.SharedPref.USER_GENDER, gender!!)
//        //on below line we are calling a add value event to pass data to firebase database.
//        //on below line we are calling a add value event to pass data to firebase database.
//        databaseReference!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                //on below line we are setting data in our firebase database.
////                databaseReference!!.child(id).setValue(courseRVModal)
//                //displaying a toast message.
//                Toast.makeText(
//                    requireContext(),
//                    "Account Updated Successfully..",
//                    Toast.LENGTH_SHORT
//                ).show()
//                //starting a main activity.
//            }

//            override fun onCancelled(error: DatabaseError) {
//                //displaying a failure message on below line.
//                Toast.makeText(requireContext(), "Fail to add Account..", Toast.LENGTH_SHORT)
//                    .show()
//            }
    }

    override fun onStart() {
        super.onStart()
        binding.logout.setOnClickListener {
            context?.let { it1 -> PreferencesHelper(it1).clearPreferences() }
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java))

        }


        binding.deleteAccount.setOnClickListener {
            Firebase.auth.currentUser?.delete()?.addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(context, getString(R.string.delete_complete), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, LoginActivity::class.java))
                }else{
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()

                }
                // Log.d(TAG, "onCreateView: ${it.result}")

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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {

            }
    }

}
