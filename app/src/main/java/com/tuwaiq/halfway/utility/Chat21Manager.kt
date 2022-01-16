package com.tuwaiq.halfway.utility

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.tuwaiq.halfway.screens.MapsActivity
import org.chat21.android.core.ChatManager
import org.chat21.android.core.users.models.ChatUser
import org.chat21.android.core.users.models.IChatUser
import org.chat21.android.ui.ChatUI
import org.chat21.android.ui.contacts.activites.ContactListActivity
import org.chat21.android.ui.conversations.listeners.OnNewConversationClickListener

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



}