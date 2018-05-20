package com.nextgen.carrental.app.android;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nextgen.carrental.app.R;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager.checkLogin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        showUserDetailsOnDrawer(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentHome())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_configure) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FragmentHome())
                    .addToBackStack(null)
                    .commit();

//            final String token = Utils.getPreferenceValue(getApplicationContext(), "dialog_flow_client_token");
//            final boolean enableZipCodeMocking = Utils.isPreferenceSwitchedOn(getApplicationContext(), "enable_zip_code_mocking");
//            final String mockZipCode = Utils.getPreferenceValue(getApplicationContext(), "mock_zip_code");
//            final String app_environment = Utils.getPreferenceValue(getApplicationContext(), "app_environment");
//            final String appServiceUrl = Utils.getPreferenceValue(getApplicationContext(), "app_service_url");


        } else if (id == R.id.nav_show_reservation) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FragmentShowReservation())
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_settings) {
            //Toast.makeText(this, "Nothing assigned", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

        } else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new FragmentProfileView())
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_logout) {
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
