package com.juangdiaz.apptest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@RealmClass
public class User implements Serializable, RealmModel {

    @PrimaryKey
    @SerializedName("firstName")
    private String firstName;

    @SerializedName("phoneNumb")
    private String phoneNumb;

    @SerializedName("email")
    private String email;

    @SerializedName("places")
    private RealmList<Places> places;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumb() {
        return phoneNumb;
    }

    public void setPhoneNumb(String phoneNumb) {
        this.phoneNumb = phoneNumb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<Places> getPlaces() {
        return places;
    }

    public void setPlaces(RealmList<Places> places) {
        this.places = places;
    }

}
