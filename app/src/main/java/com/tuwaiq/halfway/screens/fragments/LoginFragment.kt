package com.tuwaiq.halfway

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.webkit.WebView
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.halfway.screens.HalfwayActivity
import com.tuwaiq.halfway.signup.RegistrationFragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.security.acl.LastOwnerException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.PriorityBlockingQueue

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var signBTN:Button
    private lateinit var progressBar:ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        progressBar=view.findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        signBTN=view.findViewById(R.id.login_btn)


        val sighUpLink = view.findViewById<TextView>(R.id.tvsighUpLink)


        signBTN.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            LoginUser()
        }
        sighUpLink.setOnClickListener {
            val fragment=RegistrationFragment()
            activity?.supportFragmentManager
                ?.beginTransaction()?.replace(R.id.fragmentContainerView,fragment)
                ?.addToBackStack(null)?.commit()
        }
        return view }

    override fun onStop() {
        super.onStop()
        progressBar.visibility = View.INVISIBLE
    }


    fun LoginUser() {
        val email = view?.findViewById<EditText>(R.id.etLoginEmail)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etLoginPass)?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        MotionToast.darkColorToast(requireActivity(),
                            "Successful",
                            "Login successfully!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))

                        val intent = Intent(context, HalfwayActivity::class.java)
                        intent.putExtra("user_id", firebaseUser.uid)
                        intent.putExtra("email_id", email)
                        startActivity(intent)
                    } else {
                        MotionToast.darkColorToast(requireActivity(),
                            "Error",
                            "Failed to Login",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
                        progressBar.visibility = View.INVISIBLE
                    }
                }
        }

    }
}