package com.juangdiaz.apptest.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juangdiaz.apptest.R;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class PlaceDetailsActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback {

    private static final String LOG_TAG = "DeepLinkActivity";
    public final static String PARAM_PLACE = "PARAM_PLACE";

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;


    private TextView mNameTextView;
    private TextView mAddressTextView;
    private TextView mPhoneTextView;
    private TextView mWebTextView;
    private TextView mAttTextView;



    private LatLng latLng;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(PlaceDetailsActivity.this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .enableAutoManage(this, R.string.google_geo_api_key, this)
                .addConnectionCallbacks(this)
                .build();


        Bundle bundle = getIntent().getExtras();
        String placeid = bundle.getString(PARAM_PLACE);


        mNameTextView = (TextView) findViewById(R.id.name);
        mAddressTextView = (TextView) findViewById(R.id.address);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mWebTextView = (TextView) findViewById(R.id.web);
        mAttTextView = (TextView) findViewById(R.id.att);

        //Fetch data from Places API
        PendingResult<PlaceBuffer> placeResult = com.google.android.gms.location.places.Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeid);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        Log.i(LOG_TAG, "Fetching details for ID: " + placeid);

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


            mPhoneTextView.setText(Html.fromHtml(place.getPhoneNumber() + ""));
            mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
            placeName =  place.getName().toString();
            latLng = place.getLatLng();

            //Release buffer
            places.release();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(PlaceDetailsActivity.this);

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(latLng).title(placeName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

}
