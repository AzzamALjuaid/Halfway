package com.tuwaiq.halfway.screens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_ID
import com.tuwaiq.halfway.utility.PreferencesHelper

//this screen is not visible but it is used to navigate user to the correct screen
// after checking whether user if logged in or not into the application
class Splash : AppCompatActivity() {

//    private lateinit var splash: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//        splash= findViewById(R.id.splash)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()
//
//        },3000)
//



        FirebaseAuth.getInstance().currentUser.let {

            if (it !=null&& !TextUtils.isEmpty(PreferencesHelper(this).getString(USER_ID))) {//user is not logged in
                startActivity(Intent(this@Splash, HalfwayActivity::class.java))

            } else {//user is logged in

                startActivity(Intent(this@Splash, LoginActivity::class.java))

            }
            finish()
        }

    }
}