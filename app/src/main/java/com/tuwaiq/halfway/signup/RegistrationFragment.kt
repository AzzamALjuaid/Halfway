package com.tuwaiq.halfway.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.LoginFragment
import com.tuwaiq.halfway.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.BufferedReader

private const val TAG = "RegistrationFragment"
class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var regBTN: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        regBTN = view.findViewById(R.id.login_btn)
        regBTN.setOnClickListener {
            registerUser()
        }
        return view
    }

    private fun registerUser() {
        val email = view?.findViewById<EditText>(R.id.etRegEmail)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etRegPass)?.text.toString()
        val rePasword = view?.findViewById<EditText>(R.id.etRegRePass)?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            if (password != rePasword) {
                MotionToast.darkColorToast(
                    requireActivity(),
                    "Error",
                    "Password and confirm password does not match",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                )
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            MotionToast.darkColorToast(
                                requireActivity(),
                                "Success",
                                "Register successfully",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                            )

                            val fragment = LoginFragment()
                            activity?.supportFragmentManager
                                ?.beginTransaction()?.replace(R.id.fragmentContainerView, fragment)
                                ?.addToBackStack(null)?.commit()
                        } else {
                            MotionToast.darkColorToast(
                                requireActivity(),
                                "Error",
                                "Failed to Register",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
                            )
                        }
                    }
                    .addOnFailureListener {
                        println(it.message)
                    }
            }
        }
    }
}