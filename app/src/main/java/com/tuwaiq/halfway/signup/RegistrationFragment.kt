package com.tuwaiq.halfway.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
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
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.SecendActivity

private const val TAG = "RegistrationFragment"
class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var regBTN:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registration_fragment, container, false)

        regBTN=view.findViewById(R.id.register_btn)
        regBTN.setOnClickListener {
            registerUser()
        }
        return view
    }

    private fun registerUser() {
        val email = view?.findViewById<EditText>(R.id.etRegisterEmail)?.text.toString()
        val password = view?.findViewById<EditText>(R.id.etRegisterPass)?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG,"if frag")
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, SecendActivity::class.java)
                        intent.putExtra("user_id", firebaseUser.uid)
                        intent.putExtra("email_id", email)
                        startActivity(intent)
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