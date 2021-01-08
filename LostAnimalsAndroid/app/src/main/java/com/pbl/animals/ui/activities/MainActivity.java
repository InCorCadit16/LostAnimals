package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.models.User;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.services.PostService;
import com.pbl.animals.ui.fragments.MapFragment;
import com.pbl.animals.ui.fragments.PostsListFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AuthenticationActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PostService postService;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView userName;
    private ImageView userIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postService = PostService.getPostService(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userIcon = navigationView.getHeaderView(0).findViewById(R.id.user_icon);

        if (authService.user == null) {
            tryLoadUser();
        } else {
            userName.setText(authService.user.getFullName());
            userIcon.setImageBitmap(authService.user.getImage());
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_app_bar_open_drawer_description,R.string.nav_app_bar_open_drawer_description);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigateToFragment(new MapFragment());
    }

    @Override
    protected void tryLoadUser() {
        String token = authService.retrieveToken(this);
        if (token != null) {
            authService.getUser(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        authService.user = response.body();
                        userName.setText(authService.user.getFullName());
                        userIcon.setImageBitmap(authService.user.getImage());
                    } else {
                        goToLogin();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("LOGIN", t.getMessage());
                }
            });
        }
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
                navigateToPosts();
                break;
            case R.id.nav_favorite:
                navigateToFragment(new PostsListFragment());
                break;
            case R.id.nav_shelters:
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_my_posts:
                break;
            case R.id.nav_log_out:
                logout();
                break;
        }

        item.setChecked(true);
        drawerLayout.close();
        return true;
    }

    private void navigateToPosts() {
        postService.getPosts(false, new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToFragment(PostsListFragment.createFragment(response.body()));
                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logout() {
        authService.logout(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    authService.user = null;
                    authService.token = null;
                    authService.saveToken(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to log out", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment activeFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, activeFragment.getClass().newInstance())
                    .commit();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
