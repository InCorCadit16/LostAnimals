package com.pbl.animals.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.ui.activities.PostActivity;
import com.pbl.animals.utils.ImageHelper;

import java.util.List;

public class PostsListFragment extends Fragment {
    private List<Post> posts;

    public static PostsListFragment createFragment(List<Post> posts) {
        PostsListFragment fragment = new PostsListFragment();
        fragment.posts = posts;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts_list, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.posts_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new PostAdapter(posts));

        return v;
    }

    class PostAdapter extends RecyclerView.Adapter<PostHolder> {
        List<Post> posts;


        PostAdapter(List<Post> posts) {
            this.posts = posts;
        }

        @NonNull
        @Override
        public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PostHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostHolder holder, int position) {
            holder.bind(posts.get(position));
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }


    class PostHolder extends RecyclerView.ViewHolder {
        TextView authorName;
        TextView species;
        TextView color;
        TextView content;
        TextView lostTime;
        ImageView postImage;
        ImageButton mapButton;
        ImageButton commentButton;

        public PostHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.post,parent,false));

            authorName = itemView.findViewById(R.id.author_name);
            species = itemView.findViewById(R.id.post_species);
            color = itemView.findViewById(R.id.post_color);
            content = itemView.findViewById(R.id.post_content);
            lostTime = itemView.findViewById(R.id.post_lost_time);
            postImage = itemView.findViewById(R.id.post_image);
            mapButton = itemView.findViewById(R.id.map_button);
            commentButton = itemView.findViewById(R.id.comment_button);
        }

        public void bind(Post post) {
            authorName.setText(post.author.getFullName());

            species.setText(post.species.name + ", " + post.breed.name);
            color.setText(post.color.name);
            content.setText(post.content);
            lostTime.setText(post.lostTime.toString());

            if (post.imageSource != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(post.imageSource,
                        0,
                        post.imageSource.length);

                postImage.setImageBitmap(ImageHelper.getScaledBitmap(bitmap, ImageHelper.DpToPx(100, getContext())));
            } else {
                postImage.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener((View v) -> {
                Intent i = new Intent(getActivity(), PostActivity.class);
                i.putExtra(PostActivity.POST_ID, post.id);
                startActivity(i);
            });

            mapButton.setOnClickListener((View v) -> {

            });

            commentButton.setOnClickListener((View v) -> {

            });
        }
    }
}
