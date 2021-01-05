package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.pbl.animals.R;

public class MapPointPickerActivity extends AuthenticationActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    public static final String Longitude = "Lng";
    public static final String Latitude = "Lat";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng moldovaLocation = new LatLng(47.023809, 28.832489);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moldovaLocation,12));

        googleMap.setOnMapLongClickListener(this);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Tap and hold your finger on a map point to select it")
                .setNeutralButton("OK", null)
                .create();

        dialog.show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent();
        intent.putExtra(Longitude, latLng.longitude);
        intent.putExtra(Latitude, latLng.latitude);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
