package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.services.PostService;
import com.pbl.animals.ui.fragments.MapFragment;
import com.pbl.animals.ui.fragments.PostsListFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AuthenticationService authService;
    private PostService postService;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private TextView userName;
    private ImageView userIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authService = AuthenticationService.getAuthenticationService(this);
        postService = PostService.getPostService(this);

        if (authService.user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.finish();
            startActivity(intent);

            return;
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        userIcon = navigationView.getHeaderView(0).findViewById(R.id.user_icon);
        userName.setText(authService.user.getFullName());


        Bitmap bitmap = BitmapFactory.decodeByteArray(authService.user.imageSource,
                                                0,
                                                       authService.user.imageSource.length);
        userIcon.setImageBitmap(bitmap);

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

                break;
            case R.id.nav_favorite:
                navigateToFragment(new PostsListFragment());
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
