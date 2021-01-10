package com.pbl.animals.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.animals.R;
import com.pbl.animals.models.Shelter;
import com.pbl.animals.ui.activities.MapActivity;
import com.pbl.animals.ui.activities.MapPointPickerActivity;
import com.pbl.animals.ui.activities.PostActivity;
import com.pbl.animals.utils.ImageHelper;

import java.util.List;

public class SheltersListFragment extends Fragment {
    private List<Shelter> shelters;

    public static SheltersListFragment createFragment(List<Shelter> shelters) {
        SheltersListFragment fragment = new SheltersListFragment();
        fragment.shelters = shelters;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.posts_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShelterAdapter(shelters));

        return v;
    }

    class ShelterAdapter extends RecyclerView.Adapter<ShelterHolder> {
        List<Shelter> shelters;


        ShelterAdapter(List<Shelter> shelters) {
            this.shelters = shelters;
        }

        @NonNull
        @Override
        public ShelterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SheltersListFragment.ShelterHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SheltersListFragment.ShelterHolder holder, int position) {
            holder.bind(shelters.get(position));
        }

        @Override
        public int getItemCount() {
            return shelters.size();
        }
    }


    class ShelterHolder extends RecyclerView.ViewHolder {
        TextView shelterName;
        TextView shelterDescription;
        ImageView shelterImage;
        ImageButton mapButton;

        public ShelterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.shelter ,parent,false));

            shelterName = itemView.findViewById(R.id.shelter_name);
            shelterDescription = itemView.findViewById(R.id.shelter_description);
            shelterImage = itemView.findViewById(R.id.shelter_image);
            mapButton = itemView.findViewById(R.id.map_button);

        }

        public void bind(Shelter shelter) {
            shelterName.setText(shelter.name);
            shelterDescription.setText(shelter.description);

            if (shelter.imageSource != null) {
                shelterImage.setImageBitmap(
                        ImageHelper.getScaledBitmap(shelter.getImage(), ImageHelper.DpToPx(100, getContext())));
            } else {
                shelterImage.setVisibility(View.INVISIBLE);
            }
            shelterImage.setImageBitmap(shelter.getImage());

            mapButton.setOnClickListener((View v) -> {
                Intent i = new Intent(getActivity(), MapActivity.class);
                i.putExtra(MapPointPickerActivity.Longitude, shelter.location.longitude);
                i.putExtra(MapPointPickerActivity.Latitude, shelter.location.latitude);
                startActivity(i);
            });
        }
    }
}
