package com.tuwaiq.halfway

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SecendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secend)

        val userId = intent.getStringExtra("user_id")
        val emilId = intent.getStringExtra("email_id")
        val logoutButton = findViewById<Button>(R.id.logoutbtn)

        findViewById<TextView>(R.id.tvuserid).text = userId
        findViewById<TextView>(R.id.tvemailid).text = emilId

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@SecendActivity , MainActivity::class.java))
        }

    }
}