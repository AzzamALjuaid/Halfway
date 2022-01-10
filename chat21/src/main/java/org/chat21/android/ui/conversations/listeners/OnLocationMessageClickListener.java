package org.chat21.android.ui.conversations.listeners;

import android.location.Location;

import com.google.type.LatLng;

import org.chat21.android.core.conversations.models.Conversation;
import org.chat21.android.core.messages.models.LocationMessage;
import org.chat21.android.core.messages.models.Message;

/**
 * Created by stefanodp91 on 07/12/17.
 */

public interface OnLocationMessageClickListener {
    void onMessageClicked(LocationMessage message);
}
