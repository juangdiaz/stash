package com.juangdiaz.apptest.view.activity;

import android.Manifest;
import android.content.Intent;
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

import com.greysonparrelli.permiso.Permiso;
import com.juangdiaz.apptest.BaseApplication;
import com.juangdiaz.apptest.R;
import com.juangdiaz.apptest.adapter.UserListAdapter;
import com.juangdiaz.apptest.model.User;
import com.juangdiaz.apptest.presenter.UserPresenter;
import com.juangdiaz.apptest.view.UserView;

import java.util.List;

import javax.inject.Inject;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class MainActivity extends AppCompatActivity  implements UserView {

    @Inject
    UserPresenter userPresenter;


    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((BaseApplication) this.getApplication()).getComponent().inject(this);

        Permiso.getInstance().setActivity(this);

        progressBar= (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        //Request Permissions,
        askPermissions();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                startActivity(intent);
            }
        });
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
                    userPresenter.setView(MainActivity.this, MainActivity.this);
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

    @Override
    public void displayUsers(List<User> userList) {
        userListAdapter = new UserListAdapter(this, userList);
        recyclerView.setAdapter(userListAdapter);
    }

    @Override
    public void showLoadingLayout() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingLayout() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
