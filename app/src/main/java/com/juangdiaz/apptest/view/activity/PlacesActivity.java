package com.juangdiaz.apptest.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.model.Places;
import com.juangdiaz.apptest.model.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class PlacesActivity extends AppCompatActivity {

    /**
     * Request code for the autocomplete activity. This will be used to identify results from the
     * autocomplete activity in onActivityResult.
     */
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int  SUCCESSFUL_SHARE_REQUEST = 2;
    private static final int REQUEST_PLACE_PICKER = 3;
    private static final String TAG = "PlacesActivity";

    private TextView mPlaceDetailsText;
    private TextView mPlaceAttribution;
    private TextView textViewSelectedLocation;

    private FloatingActionButton fab;
    private Place place;
    private String myNumber;
    private Realm realm;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        realm = Realm.getDefaultInstance();

        // Open the autocomplete activity when the button is clicked.
        Button openButton = (Button) findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(PlacesActivity.this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), PlacesActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(PlacesActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Retrieve the TextViews that will display details about the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        mPlaceAttribution = (TextView) findViewById(R.id.place_attribution);
        textViewSelectedLocation = (TextView) findViewById(R.id.selected_place);

        textViewSelectedLocation.setVisibility(View.INVISIBLE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

                myNumber = tm.getLine1Number();

                date = new Date();

                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate= dateFormat.format(date);

                if(myNumber != null){
                    String placeName = "";

                    try {
                        placeName = URLEncoder.encode(place.getName().toString(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Check this place out: -> http://www.shareplaces.app/?place=" + place.getId()
                                    + "&placename=" + placeName
                                    + "&phone="+ myNumber
                                    + "&date="+ formattedDate);
                    sendIntent.setType("text/plain");
                    startActivityForResult(sendIntent, SUCCESSFUL_SHARE_REQUEST);

                } else {
                    Snackbar.make(view, "You need to use a phone with service to use this feature", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void saveHistory() {

        realm.executeTransaction(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {
                Places myPlace = new Places();
                myPlace.setPlaceid(place.getId());
                myPlace.setDateSent(date);
                myPlace.setName(place.getName().toString());

                final Places managedPlace = realm.copyToRealm(myPlace); // Persist unmanaged objects
                final User userToUpdate = realm.where(User.class).equalTo("phoneNumb", myNumber).findFirst();

                if(userToUpdate == null) {
                    // if user is not in realm create
                    User realmUser = realm.createObject(User.class, myNumber);
                    realmUser.setPhoneNumb(myNumber);
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

    }

    private void openAutocompleteActivity() {

        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete, Intent activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the user successfully Shared, store in the Realm
        if(requestCode == SUCCESSFUL_SHARE_REQUEST ){
            //TODO: big Issue here not all apps will return a result code on Action_Sent
            //As stated in http://stackoverflow.com/questions/22355899/facebook-intent-action-send-returns-always-0-as-resultcode
            // Leaving the code as is for now
            saveHistory();
        }


        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE || requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                place = PlacePicker.getPlace(this, data);

                textViewSelectedLocation.setVisibility(View.VISIBLE);
                Log.i(TAG, "Place Selected: " + place.getName());

                // Format the place's details and display them in the TextView.
                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()));

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }

                fab.setVisibility(View.VISIBLE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, CharSequence address,
                                              CharSequence phoneNumber, Uri websiteUri) {

        Log.e(TAG, res.getString(R.string.place_details, name, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, address, phoneNumber,
                websiteUri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_search:
                openAutocompleteActivity();
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
}
