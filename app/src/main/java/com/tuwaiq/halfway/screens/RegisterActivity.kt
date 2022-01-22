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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.tuwaiq.halfway.utility.Chat21Manager
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper
import org.chat21.android.core.authentication.ChatAuthentication
import org.chat21.android.core.authentication.ChatAuthentication.OnUserCreatedOnContactsCallback
import org.chat21.android.core.authentication.ChatAuthentication.OnUserCreatedOnFirebaseCallback
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun registerUser() {


        val email = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
        val password = findViewById<EditText>(R.id.etRegisterPass).text.toString()
        val cnfPwd = findViewById<EditText>(R.id.etRegisterCnfPass).text.toString()
        val firstName = findViewById<EditText>(R.id.etLastName).text.toString()
        val lastName = findViewById<EditText>(R.id.etFirstName).text.toString()

        if (!password.equals(cnfPwd)) {
            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.password_same),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ) {
            //checking if the text fields are empty or not.
            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.enter_name),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                cnfPwd
            )
        ) {

            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.user_credentials),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else {


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

                                        MotionToast.darkColorToast(this@RegisterActivity,
                                            getString(R.string.successful),
                                            getString(R.string.reg_success),
                                            MotionToastStyle.SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(this@RegisterActivity,R.font.helvetica_regular))

                                        val intent = Intent(this@RegisterActivity, HalfwayActivity::class.java)
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