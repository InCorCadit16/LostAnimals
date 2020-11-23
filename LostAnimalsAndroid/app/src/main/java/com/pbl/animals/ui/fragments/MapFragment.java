package com.pbl.animals.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.services.PostService;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private PostService postService;
    private ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        postService = PostService.getPostService(getContext());

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng moldovaLocation = new LatLng(47.023809, 28.832489);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moldovaLocation,12));

        postService.getPosts(true, new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Post post: response.body()) {
                        View v = markerViewFromPost(post);
                        Bitmap markerImage = viewToBitmap(v);
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(markerImage);
                        googleMap.addMarker(
                                new MarkerOptions()
                                .icon(icon)
                                .position(new LatLng(post.address.latitude, post.address.longitude)));
                    }
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


    private View markerViewFromPost(Post post) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout group = new LinearLayout(getContext());
        group.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View postView = inflater.inflate(R.layout.marker,new LinearLayout(getContext()),  false);
        TextView lostTime = postView.findViewById(R.id.post_lost_time);
        ImageView image = postView.findViewById(R.id.post_image);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm, dd MMM");
        lostTime.setText(format.format(post.lostTime));

        image.setImageBitmap(post.getImage());

        return postView;
    }

    private Bitmap viewToBitmap(View postView) {
        postView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Bitmap result = Bitmap.createBitmap(
                postView.getMeasuredWidth(),
                postView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(result);
        postView.layout(0, 0 , postView.getMeasuredWidth(), postView.getMeasuredHeight());
        postView.draw(c);
        return result;

    }
}
