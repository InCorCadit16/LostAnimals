package com.pbl.animals.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.pbl.animals.R;
import com.pbl.animals.ui.fragments.MapFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_app_bar_open_drawer_description,R.string.nav_app_bar_open_drawer_description);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigateToFragment(new MapFragment());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isChecked()) {
            drawerLayout.close();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.nav_map:
                navigateToFragment(new MapFragment());
                break;
            case R.id.nav_feed:
                break;
            case R.id.nav_favorite:
                break;
            case R.id.nav_shelters:
                break;
        }

        item.setChecked(true);
        drawerLayout.close();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                break;
            case R.id.nav_my_posts:
                break;
            case R.id.nav_log_out:
                break;
        }

        return true;
    }

    private void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }
}
