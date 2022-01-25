package org.chat21.android.core.messages.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationMessage implements Parcelable {

    double lat;
     double  lon;
     String senderName;
     String senderFullName;

    public LocationMessage(double lat, double lon, String senderName, String senderFullName) {
        this.lat = lat;
        this.lon = lon;
        this.senderName = senderName;
        this.senderFullName = senderFullName;
    }

    protected LocationMessage(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
        senderName = in.readString();
        senderFullName = in.readString();
    }

    public static final Creator<LocationMessage> CREATOR = new Creator<LocationMessage>() {
        @Override
        public LocationMessage createFromParcel(Parcel in) {
            return new LocationMessage(in);
        }

        @Override
        public LocationMessage[] newArray(int size) {
            return new LocationMessage[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(senderName);
        dest.writeString(senderFullName);
    }
}
