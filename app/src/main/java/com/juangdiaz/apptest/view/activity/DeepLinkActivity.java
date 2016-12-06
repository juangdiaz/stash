package com.juangdiaz.apptest.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.model.Places;
import com.juangdiaz.apptest.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class DeepLinkActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {

    private static final String LOG_TAG = "DeepLinkActivity";

    String phoneNumber;
    Places place;

    private Realm realm;

    private GoogleApiClient mGoogleApiClient;

    private TextView sentByTextView;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mIdTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        realm = Realm.getDefaultInstance();

        mGoogleApiClient = new GoogleApiClient.Builder(DeepLinkActivity.this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .enableAutoManage(this, R.string.google_geo_api_key, this)
                .addConnectionCallbacks(this)
                .build();


        sentByTextView = (TextView) findViewById(R.id.text_sent_by);
        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mIdTextView = (TextView) findViewById(R.id.place_image);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);
        Button openButton = (Button) findViewById(R.id.save_button);

        Intent intent = getIntent();
        Uri data = intent.getData();

        place = new Places();

        if (data.getQueryParameter("place") != null) {

            place.setPlaceid(data.getQueryParameter("place"));
        }
        if (data.getQueryParameter("date") != null) {

            Date date = new Date();
            place.setDateSent(date);
        }
        if (data.getQueryParameter("phone") != null) {

            phoneNumber = data.getQueryParameter("phone");
        }
        if (data.getQueryParameter("placename") != null) {

            place.setName(data.getQueryParameter("placename")) ;
        }


        Query q = Contacts.getQuery();
        q.whereContains(Contact.Field.PhoneNumber, phoneNumber);
        List<Contact> contacts = q.find();

        if(contacts.get(0) != null && contacts.size() > 0){
            sentByTextView.setText("Sent by: " + contacts.get(0).getDisplayName());
        }
        else {
            sentByTextView.setText("No user found");
            openButton.setVisibility(View.INVISIBLE);
        }

        //Fetch data from Places API
        PendingResult<PlaceBuffer> placeResult = com.google.android.gms.location.places.Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, place.getPlaceid());
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + place.getPlaceid());


        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store into Realm
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction(){

                    @Override
                    public void execute(Realm realm) {
                        final Places managedPlace = realm.copyToRealm(place); // Persist unmanaged objects
                        final User userToUpdate = realm.where(User.class).equalTo("phoneNumb", phoneNumber).findFirst();

                        if(userToUpdate == null) {
                            // if user is not in realm create
                            User realmUser = realm.createObject(User.class, phoneNumber);
                            realmUser.setPhoneNumb(phoneNumber); // not needed cuz of primary key
                            realmUser.getPlaces().add(managedPlace);
                        } else {
                            // else update that user
                            userToUpdate.getPlaces().add(managedPlace);
                        }
                    }
                });

                final RealmResults<User> users = realm.where(User.class).findAll();

                for(User user: users){

                    Log.d("Realm", user.getPhoneNumb() + Collections.singletonList(user.getPlaces()));
                }

                Toast.makeText(DeepLinkActivity.this, "Data has been saved for this user", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DeepLinkActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {

            if (!places.getStatus().isSuccess()) {

                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mNameTextView.setText(Html.fromHtml(place.getName() + ""));
            mAddressTextView.setText(Html.fromHtml(place.getAddress() + ""));
            mIdTextView.setText(Html.fromHtml(place.getId() + ""));
            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
            //Release buffer
            places.release();
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DeepLinkActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onDestroy() {

        super.onDestroy();

        realm.close();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {


        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }
}
