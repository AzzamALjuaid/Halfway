package com.tuwaiq.halfway

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.halfway.signup.RegistrationFragment

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var signBTN:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreat ok")
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        signBTN=view.findViewById(R.id.login_btn)

        val sighUpLink = view.findViewById<TextView>(R.id.tvsighUpLink)

        signBTN.setOnClickListener {
            LoginUser()
        }

        sighUpLink.setOnClickListener {
            val fragment=RegistrationFragment()
            activity?.supportFragmentManager
                ?.beginTransaction()?.replace(R.id.fragmentContainerView,fragment)
                ?.addToBackStack(null)?.commit()
        }
        return view }


    fun LoginUser() {
            val email = view?.findViewById<EditText>(R.id.etRegisterEmail)?.text.toString()
            val password = view?.findViewById<EditText>(R.id.etRegisterPass)?.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Log.d(TAG,"fragment ok")
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, HalfwayActivity::class.java)
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                        } else {
                            Log.d(TAG,"fragment not ok")
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }

}