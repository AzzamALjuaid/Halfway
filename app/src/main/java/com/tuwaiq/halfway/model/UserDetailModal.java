package com.tuwaiq.halfway.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetailModal implements Parcelable {
    //creating variables for our different fields.
    private String name;
    private String gender;
    private String age;
    private String latitude;
    private String longitude;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //creating an empty constructor.
    public UserDetailModal() {

    }

    protected UserDetailModal(Parcel in) {
        name = in.readString();
        gender = in.readString();
        age = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        email = in.readString();
    }

    public static final Creator<UserDetailModal> CREATOR = new Creator<UserDetailModal>() {
        @Override
        public UserDetailModal createFromParcel(Parcel in) {
            return new UserDetailModal(in);
        }

        @Override
        public UserDetailModal[] newArray(int size) {
            return new UserDetailModal[size];
        }
    };

    public UserDetailModal(String name, String gender, String age, String latitude, String longitude, String email) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeString(age);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(email);
    }

    @Override
    public String toString() {
        return "UserDetailModal{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
