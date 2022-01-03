package com.tuwaiq.halfway

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ActionMode
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
return
    }


}



//        val registerButton = findViewById<Button>(R.id.register_btn)
//
//        val loginLink = findViewById<TextView>(R.id.tvLoginLink)
//
//        registerButton.setOnClickListener {
//            registerUser()
//        }
//
//        loginLink.setOnClickListener {
//            startActivity(Intent(this@MainActivity , LoginActivity::class.java))
//        }
//    }

//    private fun registerUser() {
//        val email = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
//        val password = findViewById<EditText>(R.id.etRegisterPass).text.toString()
//
//        if (email.isNotEmpty() && password.isNotEmpty()){
//            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful){
//
//                        val firebaseUser : FirebaseUser = task.result!!.user!!
//                        Toast.makeText(this@MainActivity, "Logged in" , Toast.LENGTH_LONG).show()
//                        val intent = Intent(this@MainActivity , SecendActivity::class.java)
//                        intent.putExtra("user_id", firebaseUser.uid)
//                        intent.putExtra("email_id", email)
//                        startActivity(intent)
//                        finish()
//                    }else{
//
//                        Toast.makeText(this@MainActivity , "Error" , Toast.LENGTH_LONG).show()
//                    }
//                }
//        }
//
//    }

