package com.tuwaiq.halfway.chatsdk.model

import android.media.DrmInitData
import android.media.Image
import android.os.Parcelable
import android.provider.ContactsContract
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatUser(
    val firstName:String,
    val username:String,
    val email:String,
):Parcelable
