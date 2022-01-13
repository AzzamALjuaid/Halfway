package com.tuwaiq.halfway

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.screens.HalfwayActivity
import com.tuwaiq.halfway.screens.LoginActivity
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_ID
import com.tuwaiq.halfway.utility.PreferencesHelper

class SplashActivity : AppCompatActivity() {

    private lateinit var splash:LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash= findViewById(R.id.splash)

        Handler(Looper.getMainLooper()).postDelayed({

            FirebaseAuth.getInstance().currentUser.let {

                if (it !=null&& !TextUtils.isEmpty(PreferencesHelper(this).getString(USER_ID))) {//user is not logged in
                    startActivity(Intent(this,HalfwayActivity::class.java))

                } else {//user is logged in

                    startActivity(Intent(this,LoginActivity::class.java))

                }
                finish()
            }




//
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            finish()

        },3000)




    }

}