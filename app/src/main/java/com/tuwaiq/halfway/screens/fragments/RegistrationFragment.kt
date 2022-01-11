package com.tuwaiq.halfway.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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


    private lateinit var regBTN: Button
    private lateinit var tv_login: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        tv_login = view.findViewById(R.id.tv_login)
        tv_login.setOnClickListener {
            val fragment=LoginFragment()
            activity?.supportFragmentManager
                ?.beginTransaction()?.replace(R.id.fragmentContainerView,fragment)
                ?.addToBackStack(null)?.commit()
        }

        regBTN = view.findViewById(R.id.btn_register)
        regBTN.setOnClickListener {
            registerUser()
        }
        return view
    }

    private fun registerUser() {
        val email = view?.findViewById<EditText>(R.id.etRegisterEmail)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etRegisterPass)?.text.toString()
        val cnfPwd = view?.findViewById<EditText>(R.id.etRegisterCnfPass)?.text.toString()
        val firstName = view?.findViewById<EditText>(R.id.etLastName)?.text.toString()
        val lastName = view?.findViewById<EditText>(R.id.etFirstName)?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            if (password != cnfPwd) {
                MotionToast.darkColorToast(
                    requireActivity(),
                    getString(R.string.error),
                    getString(R.string.Password_not_match),
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
                                getString(R.string.success),
                                getString(R.string.reg_success),
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
                                getString(R.string.error),
                                getString(R.string.faild_reg),
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