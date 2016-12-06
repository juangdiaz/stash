package com.juangdiaz.apptest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@RealmClass
public class Places implements RealmModel, Serializable{

    @SerializedName("placeid")
    private String placeid;

    @SerializedName("datesent")
    private Date dateSent;

    @SerializedName("name")
    private String Name;

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getFormattedDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(dateSent);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
