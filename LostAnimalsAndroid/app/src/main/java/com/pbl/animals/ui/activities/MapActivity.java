package com.pbl.animals.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pbl.animals.R;
import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Location;
import com.pbl.animals.services.CommentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AuthenticationActivity implements OnMapReadyCallback {
    public static final String FOCUS_LONGITUDE = "Focus Lng";
    public static final String FOCUS_LATITUDE = "Focus Lat";

    private CommentService commentService;

    private long postId;
    private Location location;
    private Location focusLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        commentService = CommentService.getCommentService(this);

        location = new Location();
        location.latitude = getIntent().getDoubleExtra(MapPointPickerActivity.Latitude,-1);
        location.longitude = getIntent().getDoubleExtra(MapPointPickerActivity.Longitude,-1);

        focusLocation = new Location();
        focusLocation.latitude = getIntent().getDoubleExtra(FOCUS_LATITUDE, -1);
        focusLocation.longitude = getIntent().getDoubleExtra(FOCUS_LONGITUDE, -1);

        postId = getIntent().getLongExtra(PostActivity.POST_ID, -1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng looseLocation = focusLocation.latitude == -1 && focusLocation.longitude == -1 ?
                new LatLng(location.latitude, location.longitude) :
                new LatLng(focusLocation.latitude, focusLocation.longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(looseLocation,18));

        if (postId != -1) {
            displayPost(googleMap);
        } else {
            displayShelter(googleMap);
        }

    }

    private void displayPost(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Initial point")
                .position(new LatLng(location.latitude, location.longitude)));

        commentService.getCommentsByPostId(postId, new Callback<Comment[]>() {
            @Override
            public void onResponse(Call<Comment[]> call, Response<Comment[]> response) {
                if (response.isSuccessful()) {
                    Comment[] comments = response.body();
                    List<LatLng> points = new ArrayList<>();
                    points.add(new LatLng(location.latitude, location.longitude));
                    Arrays.stream(comments).forEach(c -> {
                        points.add(new LatLng(c.location.latitude, c.location.longitude));
                        googleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .position(new LatLng(c.location.latitude, c.location.longitude)));
                    });

                    googleMap.addPolyline(new PolylineOptions().addAll(points));
                } else {
                    Toast.makeText(MapActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comment[]> call, Throwable t) {
                Toast.makeText(MapActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayShelter(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_shelter))
                .position(new LatLng(location.latitude, location.longitude)));
    }
}
