package com.tuwaiq.halfway.screens

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }


    private fun initView() {
        mAuth = FirebaseAuth.getInstance()
        loading = findViewById(R.id.idPBLoading)
        val tv_register = findViewById<TextView>(R.id.tv_register)

        val btn_login = findViewById<Button>(R.id.btn_login)

        btn_login.setOnClickListener {
            loginUser()
        }

        tv_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    //login into the firebase database for the access
    private fun loginUser() {
        val email = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
        val password = findViewById<EditText>(R.id.etRegisterPass).text.toString()
        //below is the validation of the credential enter by the uer
        if (TextUtils.isEmpty(email)) {
            Common.showToast(
                this@LoginActivity,
                "Please enter the email"
            )
        } else if (!Common.isValidEmail(email)) {
            Common.showToast(
                this@LoginActivity,
                "Please enter a valid email"
            )
        } else if (TextUtils.isEmpty(password)) {
            Common.showToast(
                this@LoginActivity,
                "Please enter the password"
            )
        } else {//it goes here when all the require condition matches
            loading?.visibility = View.VISIBLE



            ChatManager.startWithEmailAndPassword(this, Chat21Manager.AppId,
                email,password, object : ChatAuthentication.OnChatLoginCallback {
                    override fun onChatLoginSuccess(currentUser: IChatUser?) {
                        loading?.visibility = View.GONE
                        //on below line we are hiding our progress bar.
                        Common.showToast(
                            this@LoginActivity,
                            "Login Successful.."
                        )



                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        //on below line we are opening our mainactivity.
                        PreferencesHelper(this@LoginActivity).putString(Constant.SharedPref.USER_ID, currentUser!!.id)
                        PreferencesHelper(this@LoginActivity).putString(Constant.SharedPref.USER_EMAIL, email)
                        startActivity(intent)
                        finish()
                    }

                    override fun onChatLoginError(e: Exception?) {
                        loading?.visibility = View.GONE
                        //hiding our progress bar and displaying a toast message.
                        Log.d(TAG, "loginUser: ${e?.message}")
                        Common.showToast(
                            this@LoginActivity,
                            "Please enter valid user credentials.."
                        )

                    }
                }
            );

            /*
               ChatManager.getInstance().createContactFor(currentUser.getId(), currentUser.getEmail(),
                        [YOUR_FIRST_NAME], [YOUR_LAST_NAME], new OnContactCreatedCallback() {
                            @Override
                            public void onContactCreatedSuccess(ChatRuntimeException exception) {
                                if (exception == null) {
                                    ChatUI.getInstance().openConversationsListActivity();
                                } else {
                                    // TODO: handle the exception
                                }
                            }
                        });

             */


        }

    }
}