package com.tuwaiq.halfway

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.VideoView
import com.airbnb.lottie.LottieAnimationView
import java.time.Instant

class SplashActivity : AppCompatActivity() {

    private lateinit var splash:LottieAnimationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash= findViewById(R.id.splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        },3000)



//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
//
//        try {
//            val videoHolder = VideoView(this)
//            setContentView(videoHolder)
//            val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.s1)
//            videoHolder.setVideoURI(video)
//
//            videoHolder.setOnCompletionListener { jump() }
//
//            videoHolder.start()
//        } catch (ex:Exception){
//            jump()
//        }
//
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        jump()
//        return true}
//
//    private fun jump(){
//        if (isFinishing)
//            return
//
//
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

    }
}
