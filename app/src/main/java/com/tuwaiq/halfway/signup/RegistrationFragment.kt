package com.tuwaiq.halfway.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.LoginFragment
import com.tuwaiq.halfway.R

private const val TAG = "RegistrationFragment"
class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var regBTN:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        regBTN=view.findViewById(R.id.login_btn)
        regBTN.setOnClickListener {
            registerUser()
        }
        return view
    }

    private fun registerUser() {
        val email = view?.findViewById<EditText>(R.id.etRegisterEmail)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etRegisterPass)?.text.toString()

        Log.d(TAG,"reg fun")
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG,"if frag")
                        Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                        val fragment=LoginFragment()
                        activity?.supportFragmentManager
                            ?.beginTransaction()?.replace(R.id.fragmentContainerView,fragment)
                            ?.addToBackStack(null)?.commit()
                    } else {

                        Log.d(TAG,"else frag")
                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    println(it.message)
                }
        }
    }
}