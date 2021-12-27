package com.tuwaiq.halfway

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tuwaiq.halfway.signup.RegistrationFragment

private const val TAG = "LoginFragment"
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
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
                            Toast.makeText(context, "Logged in", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, HalfwayActivity::class.java)
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
}