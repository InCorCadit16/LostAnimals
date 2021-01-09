package com.pbl.animals.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.services.PostService;
import com.pbl.animals.ui.activities.CreatePostActivity;
import com.pbl.animals.ui.activities.PostActivity;
import com.pbl.animals.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    private GoogleMap map;

    private PostService postService;
    private ViewGroup container;
    private FloatingActionButton actionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        postService = PostService.getPostService(getContext());

        actionButton = v.findViewById(R.id.new_post);

        actionButton.setOnClickListener((View view) -> {
            Intent i = new Intent(getContext(), CreatePostActivity.class);
            startActivity(i);
        });

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng moldovaLocation = new LatLng(47.023809, 28.832489);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moldovaLocation,12));
        map = googleMap;
        enableMyLocation();

        postService.getPosts(true, new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Marker> markers = new ArrayList<>();
                    for (Post post: response.body()) {
                        int icon_id = getIconId(post);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(icon_id);
                        markers.add(googleMap.addMarker(
                                new MarkerOptions()
                                .icon(icon)
                                .position(new LatLng(post.location.latitude, post.location.longitude))));
                    }

                    googleMap.setOnMarkerClickListener((Marker marker) -> {
                        int position = markers.indexOf(marker);
                        Intent intent = new Intent(getActivity(), PostActivity.class);
                        intent.putExtra(PostActivity.POST_ID, response.body().get(position).id);
                        startActivity(intent);
                        return true;
                    });
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            PermissionUtils.PermissionDeniedDialog
                    .newInstance(true).show(getChildFragmentManager(), "dialog");
        }
    }

    private int getIconId(Post post) {
        return post.species.name.equals("Dog") ? R.drawable.marker_dog : R.drawable.marker_cat;
    }
}
