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
    val userId= FirebaseAuth.getInstance().currentUser?.uid



    val userName = currentUser.displayName.toString()

    val userEmail = currentUser.email.toString()

    private var loading: ProgressBar? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var gender: String? = null
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        //on below line creating our database reference.


        mView.btnSave.setOnClickListener {
            updateEmail()
            saveData()
        }
    }

    //saving the data to the firebase db
    private fun saveData() {

        val name = binding.etName.text.toString()
        updateData(name)
        

    }

    private fun updateData(name: String) {
//        databaseReference = FirebaseDatabase.getInstance().getReference("contacts")
        val user = mapOf<String,String>(

            "firstname" to name

        )
//        databaseReference!!.child(name).updateChildren(user).addOnSuccessListener {
//            binding.etName.text.clear()
//            Toast.makeText(context,"success",Toast.LENGTH_LONG).show()
//        }.addOnFailureListener {
//            Toast.makeText(context,"field",Toast.LENGTH_LONG).show()
//        }

    }

    override fun onStart() {
        readData()
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
                        getString(R.string.successful),
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

    private fun readData() {

         database = FirebaseDatabase.getInstance().getReference("contacts/$userId").child("firstname")
        Log.d(TAG, "readData: $database")

    }
    fun updateEmail(){
        FirebaseAuth.getInstance().currentUser?.updateEmail("${binding.etEmail.text}")
            ?.addOnCompleteListener {
                if (it.isSuccessful){
                    Snackbar.make(requireView(),"update Email successfully",Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(),"update Email not successfully",Snackbar.LENGTH_SHORT).show()
                }

            }
    }





}
