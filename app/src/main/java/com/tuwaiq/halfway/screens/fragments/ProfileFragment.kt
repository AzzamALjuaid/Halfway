package com.tuwaiq.halfway.screens.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.databinding.FragmentProfileBinding
import com.tuwaiq.halfway.screens.LoginActivity
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import kotlin.math.log

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    val AppId="halfway-7f6ca"
    val firebaseUrl="https://halfway-7f6ca-default-rtdb.firebaseio.com"
    val storageBucket="gs://halfway-7f6ca.appspot.com"
    private lateinit var binding: FragmentProfileBinding
    val currentUser = FirebaseAuth.getInstance().currentUser!!
    val userId= FirebaseAuth.getInstance().currentUser?.uid




    val userName = currentUser.displayName.toString()

    val userEmail = currentUser.email.toString()

    var firebaseDatabase: FirebaseDatabase? = null
    //    var databaseReference: DatabaseReference? = null
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentProfileBinding.inflate(layoutInflater)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding = FragmentProfileBinding.bind(view);
        initView()
        readData()
    }

    private fun initView() {

        binding.emailEdit.setOnClickListener {
            updateEmail()
        }

        binding.userEdit.setOnClickListener {

            val fullName = binding.etUsername.text.toString()
            updateFullname(fullName)

        }


        binding.etEmail.setText(userEmail)


        firebaseDatabase = FirebaseDatabase.getInstance();




    }

    //saving the data to the firebase db
    private fun saveData() {

        val name = binding.etUsername.text.toString()



    }


    override fun onStart() {
//        readData()
        super.onStart()
        binding.logout.setOnClickListener {
            context?.let { it1 -> PreferencesHelper(it1).clearPreferences() }
            FirebaseAuth.getInstance().signOut()
            MotionToast.darkColorToast(requireActivity(),
                getString(R.string.successful),
                getString(R.string.logout_success),
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
            startActivity(Intent(context, LoginActivity::class.java))

        }


        binding.deleteAccount.setOnClickListener {
            Firebase.auth.currentUser?.delete()?.addOnCompleteListener {
                if(it.isSuccessful){

                    MotionToast.darkColorToast(requireActivity(),
                        getString(R.string.successful),
                        getString(R.string.delete_complete),
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))

                    startActivity(Intent(context, LoginActivity::class.java))
                }else{

                    MotionToast.darkColorToast(requireActivity(),
                        getString(R.string.failed),
                        getString(R.string.error),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))

                }

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {

            }
    }

    fun readData() {



        database = FirebaseDatabase.getInstance().getReference("apps")
        database.child("halfway-7f6ca").child("contacts").child(userId.toString()).get().addOnSuccessListener {


            val fName = it.child("firstname").value
            val lName = it.child("lastname").value
            val fullName = "$fName $lName"

            if (it.exists()){

                binding.etUsername.setText(fullName)


            }else{
                Snackbar.make(requireView(),getString(R.string.user_not_exist),Snackbar.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Snackbar.make(requireView(),(R.string.failed),Snackbar.LENGTH_SHORT).show()

        }


    }
    fun updateEmail(){
        FirebaseAuth.getInstance().currentUser?.updateEmail("${binding.etEmail.text}")
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    Snackbar.make(requireView(),getString(R.string.email_update),Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(),getString(R.string.email_not_update),Snackbar.LENGTH_SHORT).show()
                }

            }
    }

    fun updateFullname(fullName:String){

        val (firstName , lastname) = fullName.split(" ")
        database = FirebaseDatabase.getInstance().getReference("apps")
        val user = mapOf<String,String>(
            "firstname" to firstName,
            "lastname" to lastname

        )

        database.child("halfway-7f6ca").child("contacts").child(userId.toString()).updateChildren(user).addOnSuccessListener {
            Snackbar.make(requireView(),getString(R.string.username_update),Snackbar.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Snackbar.make(requireView(),getString(R.string.username_not_update),Snackbar.LENGTH_SHORT).show()
        }



    }


}
