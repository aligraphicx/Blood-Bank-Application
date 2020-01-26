package com.example.ali.linkusapp;

import android.os.Parcel;
import android.os.Parcelable;

public class BlodDonor implements Parcelable {

    public BlodDonor() {
    }

    private String name;
    private String group;
    private String city;
    private String phone;
    private String profile;

    protected BlodDonor(Parcel in) {
        name = in.readString();
        group = in.readString();
        city = in.readString();
        phone = in.readString();
        profile = in.readString();
    }

    public static final Creator<BlodDonor> CREATOR = new Creator<BlodDonor>() {
        @Override
        public BlodDonor createFromParcel(Parcel in) {
            return new BlodDonor(in);
        }

        @Override
        public BlodDonor[] newArray(int size) {
            return new BlodDonor[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileLink() {
        return profile;
    }

    public void setProfileLink(String profileLink) {
        this.profile = profileLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(group);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeString(profile);
    }
}
