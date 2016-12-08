package com.juangdiaz.apptest.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.juangdiaz.apptest.Injector;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.adapter.HistoryListAdapter;
import com.juangdiaz.apptest.model.Places;
import com.juangdiaz.apptest.model.User;
import com.juangdiaz.apptest.repository.DatabaseRealm;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class SentHistoryActivity extends AppCompatActivity {

    public final static String PARAM_USER = "PARAM_USER";

    @Inject DatabaseRealm databaseRealm;

    private RecyclerView recyclerView;
    private HistoryListAdapter historyListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ProgressBar progressBar;

    private String phoneNumber;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Get Realm Instance
        Injector.getApplicationComponent().inject(this);

        realm = databaseRealm.getRealmInstance();

        progressBar= (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        getUserHistory();
    }

    private void getUserHistory() {

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            //Get users Phone number from Realm
           User user = (User) bundle.getSerializable(PARAM_USER);
            phoneNumber = user.getPhoneNumb();
        }
        else {
            //Get users Phone number
            TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

            phoneNumber = tm.getLine1Number();
        }

        //Get user History from Realm
        final User userHistory = realm.where(User.class).equalTo("phoneNumb", phoneNumber.replace("+", "")).findFirst();

        if(userHistory != null) {
            Log.d("Realm", userHistory.getPhoneNumb() + Collections.singletonList(userHistory.getPlaces()));
            displayUsers(userHistory.getPlaces());
        } else {
            //Toasty for now
            Toast.makeText(this, "There is no user data", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void displayUsers(List<Places> userHistory) {

        historyListAdapter = new HistoryListAdapter(this, userHistory);
        recyclerView.setAdapter(historyListAdapter);
        progressBar.setVisibility(View.GONE);
    }


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
    protected void onDestroy() {

        super.onDestroy();
        //Close Realm prevent memory leak
        databaseRealm.close();
    }



}
