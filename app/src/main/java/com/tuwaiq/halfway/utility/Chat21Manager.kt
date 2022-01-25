package com.tuwaiq.halfway.utility

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.tuwaiq.halfway.Geofence.Reminder
import com.tuwaiq.halfway.screens.MapsActivity
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.chat21.android.core.ChatManager
import org.chat21.android.core.users.models.ChatUser
import org.chat21.android.core.users.models.IChatUser
import org.chat21.android.ui.ChatUI
import org.chat21.android.ui.contacts.activites.ContactListActivity
import org.chat21.android.ui.conversations.listeners.OnNewConversationClickListener
import org.json.JSONObject

object Chat21Manager {

    val AppId="halfway-7f6ca"
    val firebaseUrl="https://halfway-7f6ca-default-rtdb.firebaseio.com"
    val storageBucket="gs://halfway-7f6ca.appspot.com"


    fun  start(context: Context){

        val currentUser = FirebaseAuth.getInstance().currentUser!!


        val user =  ChatUser(
            currentUser.uid,
            currentUser.displayName
        )
        user.email = user.email
        // mandatory
        // it creates the chat configurations
        val mChatConfiguration = ChatManager.Configuration.Builder(AppId)
            .firebaseUrl(firebaseUrl)
            .storageBucket(storageBucket)
            .build();

        ChatManager.start(context, mChatConfiguration, user);




        //  RefreshFirebaseInstanceIdTask().execute()

        ChatUI.getInstance().context = context

        ChatUI.getInstance().enableGroups(false)
        val support: IChatUser? = null
        ChatUI.getInstance().onNewConversationClickListener = OnNewConversationClickListener {
            if (support != null) {
                ChatUI.getInstance().openConversationMessagesActivity(support)
            } else {
                val intent = Intent(context,
                    ContactListActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // start activity from context
                context.startActivity(intent)
            }
        }
        ChatUI.getInstance().setOnLocationMessageClickListener { locationMessage ->

            context.startActivity(Intent(context, MapsActivity::class.java).apply {
                putExtra("locationMessage",locationMessage)

            })


        }
    }

    private  val TAG = "Chat21Manager"
    fun sendNotification(reminder: Reminder?) {


        val firebaseUsersPath = FirebaseDatabase.getInstance().reference
            .child(
                "apps/" + ChatManager.Configuration.appId +
                        "/users/" + reminder?.userToken + "/instances"
            )

        firebaseUsersPath.get().addOnCompleteListener { task: Task<DataSnapshot> ->
            if (task.isSuccessful) {

                var usertoken="";
                task.result.children.forEach {
                    usertoken =  it.key!!
                    Log.d(TAG, "sendNotification:key ${it.key}  value ${it.value} ")
                }


                object : AsyncTask<Void?, Void?, Void?>() {
                    override fun doInBackground(vararg params: Void?): Void? {
                        try {
                            val client = OkHttpClient()
                            val json = JSONObject()
                            val dataJson = JSONObject()
                            dataJson.put("body", "You reach Half way to your friend ${reminder?.currentUserName}")
                            dataJson.put("title","Half Way")
                            dataJson.put("channel_type","near")
                            json.put("notification", dataJson)
                            json.put("data", dataJson)
                            json.put("to", usertoken)
                            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
                            val request = Request.Builder()
                                .header("Authorization", "key=AAAAAwmemOE:APA91bHxrKBEftCQIBwckrKj8sPDmcaS_5Y8Sc0WHOlRGH8dc5mYn8K1vS7IeS12gdg0KCd_5q5yhX4Yj9anzvRKdzeGeyS6SSGufzmY3yR1X6C40DFtLzURBStjCIRLROO0rNh8jzAz")
                                .url("https://fcm.googleapis.com/fcm/send")
                                .post(body)
                                .build()
                            val response = client.newCall(request).execute()
                            if (response.isSuccessful) {

                            }
                            val finalResponse = response.body()!!.string()
                            Log.d(TAG, "doInBackground: ${finalResponse}")
                        } catch (e: Exception) { //Log.d(TAG,e+"");
                        }
                        return null
                    }

                }.execute()

            } else {
                Log.d(
                    TAG,
                    "onComplete:user _token " + task.exception!!.message
                )
            }
        }






    }


}