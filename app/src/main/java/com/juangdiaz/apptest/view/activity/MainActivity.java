package com.juangdiaz.apptest.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.greysonparrelli.permiso.Permiso;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.adapter.UserListAdapter;
import com.juangdiaz.apptest.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private List<User> userList = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting Activity for Permissions
        Permiso.getInstance().setActivity(this);

        progressBar= (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        //Request Permissions
        askPermissions();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Display Places
                Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadUserDetails() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute(){

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                //Load from contacts
                Query q = Contacts.getQuery();
                q.hasPhoneNumber();
                q.include(Contact.Field.DisplayName, Contact.Field.Email, Contact.Field.PhoneNormalizedNumber);
                List<Contact> contacts = q.find();
                User user;


                for(Contact contact : contacts ){
                    if(contact.getPhoneNumbers().size() > 0) {
                        user = new User();
                        user.setFirstName(contact.getDisplayName());
                        user.setPhoneNumb(contact.getPhoneNumbers().get(0).getNormalizedNumber());
                        if(contact.getEmails().size() > 0){
                            user.setEmail(contact.getEmails().get(0).getAddress());
                        } else {
                            user.setEmail("");
                        }

                        if(user.getPhoneNumb() != null) {
                            userList.add(user);
                        }
                    }

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                displayUsers(userList);

                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute((Void[]) null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, SentHistoryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void askPermissions(){

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {

            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.isPermissionGranted(Manifest.permission.READ_CONTACTS)
                        && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
                        && resultSet.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && resultSet.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
                    // permission granted!
                    loadUserDetails();
                } else {
                    // permissions denied, exit app
                    Toast.makeText(getApplicationContext(), "All permissions are Required if you want to use this app!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permissions", "To be able to use this app, Please enable all permissions Required", null, callback);
            }
        }, Manifest.permission.READ_CONTACTS
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    public void displayUsers(List<User> userList) {
        userListAdapter = new UserListAdapter(this, userList);
        recyclerView.setAdapter(userListAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
