package com.example.ali.linkusapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable{
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private Uri profileUri;
    private String password;



    public User() {

    }

    protected User(Parcel in) {
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        fullName = in.readString();
        phoneNumber = in.readString();
        profileUri = in.readParcelable(Uri.class.getClassLoader());
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Uri getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(Uri profileUri) {
        this.profileUri = profileUri;
    }

    public User(String email, String firstName, String lastName,
                String fullName, String phoneNumber, String dobYear,
                String dobMonth, String dobDay, Uri profileUri, String password) {



        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.profileUri = profileUri;
        this.password=password;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(fullName);
        dest.writeString(phoneNumber);

        dest.writeParcelable(profileUri, flags);
        dest.writeString(password);
    }
}
