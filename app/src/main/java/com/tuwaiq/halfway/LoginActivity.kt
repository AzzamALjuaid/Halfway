package com.tuwaiq.halfway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.logoutbtn)

//        loginButton.setOnClickListener {
//            loginUser()
//        }
    }

//    private fun loginUser()  {
//        val email = findViewById<EditText>(R.id.etEmailLogin).text.toString()
//        val password = findViewById<EditText>(R.id.etPasswordLogin).text.toString()
//
//        if (email.isNotEmpty() && password.isNotEmpty()){
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//                        val firebaseUser : FirebaseUser = task.result!!.user!!
//                        Toast.makeText(this@LoginActivity, "Logged in" , Toast.LENGTH_LONG).show()
//                        val intent = Intent(this@LoginActivity , SecendActivity::class.java)
//                        intent.putExtra("user_id", firebaseUser.uid)
//                        intent.putExtra("email_id", email)
//                        startActivity(intent)
//                        finish()
//                    }else{
//
//                        Toast.makeText(this@LoginActivity , "Error" , Toast.LENGTH_LONG).show()
//                    }
//                }
//        }

    }