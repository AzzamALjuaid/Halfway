package com.tuwaiq.halfway.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import android.text.TextUtils
import com.google.firebase.auth.UserProfileChangeRequest
import com.tuwaiq.halfway.utility.Chat21Manager
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper
import org.chat21.android.core.authentication.ChatAuthentication
import org.chat21.android.core.authentication.ChatAuthentication.OnUserCreatedOnContactsCallback
import org.chat21.android.core.authentication.ChatAuthentication.OnUserCreatedOnFirebaseCallback
import java.lang.Exception
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun initView() {
        val tv_login = findViewById<TextView>(R.id.tv_login)


        val btn_register = findViewById<Button>(R.id.btn_register)

        btn_register.setOnClickListener {
            registerUser()
        }

        tv_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    //register the user process is done below
    private fun registerUser() {


        val email = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
        val password = findViewById<EditText>(R.id.etRegisterPass).text.toString()
        val cnfPwd = findViewById<EditText>(R.id.etRegisterCnfPass).text.toString()
        val firstName = findViewById<EditText>(R.id.etLastName).text.toString()
        val lastName = findViewById<EditText>(R.id.etFirstName).text.toString()
        //here detail enter by the user is validated
        if (!password.equals(cnfPwd)) {
            Toast.makeText(
                this@RegisterActivity,
                "Please check both having same password..",
                Toast.LENGTH_SHORT
            ).show();
        } else if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ) {
            //checking if the text fields are empty or not.
            Toast.makeText(
                this@RegisterActivity,
                "Please enter your  name",
                Toast.LENGTH_SHORT
            ).show();
        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                cnfPwd
            )
        ) {
            //checking if the text fields are empty or not.
            Toast.makeText(
                this@RegisterActivity,
                "Please enter your credentials..",
                Toast.LENGTH_SHORT
            ).show();
        } else {//if the details are entered correctly then user is able to register into the application


                ChatAuthentication.getInstance().createUserOnFirebaseAuthentication(
                    this,
                    email,
                    password,
                    object : OnUserCreatedOnFirebaseCallback {
                        override fun onUserCreatedSuccess(userUID: String) {
                            val userMap: MutableMap<String, Any> = HashMap()
                            userMap["email"] = email
                            userMap["firstname"] = firstName
                            userMap["imageurl"] = ""
                            userMap["lastname"] = lastName
                            userMap["timestamp"] = Date().time
                            userMap["timestamp"] = Date().time
                            userMap["uid"] = userUID
                            Chat21Manager.start(this@RegisterActivity)



                            ChatAuthentication.getInstance().createUserOnContacts(
                                userUID,
                                userMap,
                                object : OnUserCreatedOnContactsCallback {
                                    override fun onUserCreatedSuccess() {
                                        val firebaseUser: FirebaseUser = FirebaseAuth.getInstance()!!.currentUser!!
                                        Toast.makeText(this@RegisterActivity, "Logged in", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                        intent.putExtra("user_id", firebaseUser.uid)
                                        intent.putExtra("email_id", email)
                                        PreferencesHelper(this@RegisterActivity).putString(Constant.SharedPref.USER_ID, firebaseUser.uid)
                                        PreferencesHelper(this@RegisterActivity).putString(Constant.SharedPref.USER_EMAIL, email)
                                        startActivity(intent)
                                        finish()
                                    }

                                    override fun onUserCreatedError(e: Exception) {
                                        // TODO: 04/01/18
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Saving user on contacts failed.$e",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }

                        override fun onUserCreatedError(e: Exception) {
                            // TODO: 04/01/18  string
                            Toast.makeText(
                                this@RegisterActivity,"Authentication failed.$e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
/*
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {

                        Toast.makeText(this@RegisterActivity, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }*/
        }

    }
}