package com.tuwaiq.halfway

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
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreat ok")
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        val registerButton = view.findViewById<Button>(R.id.register_btn)

        val loginLink = view.findViewById<TextView>(R.id.tvLoginLink)

        registerButton.setOnClickListener {
            LoginUser()
        }

        loginLink.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
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
                            val intent = Intent(context, SecendActivity::class.java)
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