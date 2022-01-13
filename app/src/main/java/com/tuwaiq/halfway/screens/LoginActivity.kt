package com.tuwaiq.halfway.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.utility.Chat21Manager
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_EMAIL
import com.tuwaiq.halfway.utility.Constant.SharedPref.Companion.USER_ID
import com.tuwaiq.halfway.utility.PreferencesHelper
import org.chat21.android.core.ChatManager
import org.chat21.android.core.authentication.ChatAuthentication
import org.chat21.android.core.users.models.IChatUser
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        loading?.visibility = View.GONE
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
    private  val TAG = "LoginActivity"
    //login into the firebase database for the access
    private fun loginUser() {
        val email = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
        val password = findViewById<EditText>(R.id.etRegisterPass).text.toString()
        //below is the validation of the credential enter by the uer
        if (TextUtils.isEmpty(email)) {
            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.enter_email),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else if (!Common.isValidEmail(email)) {
            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.enter_valid_email),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else if (TextUtils.isEmpty(password)) {

            MotionToast.darkColorToast(this,
                getString(R.string.warning),
                getString(R.string.enter_password),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.helvetica_regular))

        } else {//it goes here when all the require condition matches
            loading?.visibility = View.GONE



            ChatManager.startWithEmailAndPassword(this, Chat21Manager.AppId,
                email,password, object : ChatAuthentication.OnChatLoginCallback {
                    override fun onChatLoginSuccess(currentUser: IChatUser?) {
                        loading?.visibility = View.GONE


                        MotionToast.darkColorToast(this@LoginActivity,
                            getString(R.string.successful),
                            getString(R.string.login_success),
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@LoginActivity,R.font.helvetica_regular))


                        val intent = Intent(this@LoginActivity, HalfwayActivity::class.java)
                        //on below line we are opening our mainactivity.
                        PreferencesHelper(this@LoginActivity).putString(USER_ID, currentUser!!.id)
                        PreferencesHelper(this@LoginActivity).putString(USER_EMAIL, email)
                        startActivity(intent)
                        finish()
                    }

                    override fun onChatLoginError(e: Exception?) {
                        loading?.visibility = View.GONE
                        //hiding our progress bar and displaying a toast message.
                        Log.d(TAG, "loginUser: ${e?.message}")

                        MotionToast.darkColorToast(this@LoginActivity,
                            getString(R.string.warning),
                            getString(R.string.user_credentials),
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@LoginActivity,R.font.helvetica_regular))

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
                                    //
                                }
                            }
                        });

             */


        }

    }
}