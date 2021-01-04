package com.pbl.animals.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.pbl.animals.R;
import com.pbl.animals.models.Post;
import com.pbl.animals.models.contracts.requests.UpdatePostRequest;
import com.pbl.animals.models.inner.PostType;
import com.pbl.animals.models.inner.Size;
import com.pbl.animals.models.inner.SpeciesLookup;
import com.pbl.animals.utils.ImageHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends CreatePostActivity {
    private Post post;
    private UpdatePostRequest updateRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createPostButton.setText("Update post");
        updateRequest = new UpdatePostRequest();

        long id = getIntent().getLongExtra(PostActivity.POST_ID, -1);
        loadPost(id);
    }

    private void loadPost(long id) {
        postService.getPostById(id, new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    post = response.body();

                    setSpinnerItem(typeSpinner, Arrays.stream(PostType.values()).map(PostType::name).toArray(String[]::new), post.postType.name());
                    setSpinnerItem(sizeSpinner, Arrays.stream(Size.values()).map(Size::name).toArray(String[]::new), post.size.name());
                    setSpinnerItem(speciesSpinner, Arrays.stream(lookups.species).map(s -> s.name).toArray(String[]::new), post.species.name);
                    setSpinnerItem(breedSpinner, post.species.breeds.stream().map(b -> b.name).toArray(String[]::new), post.breed.name);
                    setSpinnerItem(colorSpinner, Arrays.stream(lookups.colors).map(c -> c.name).toArray(String[]::new), post.color.name);
                    description.setText(post.content);
                    location.setText(getString(R.string.location_filled, post.location.longitude, post.location.latitude));

                    SimpleDateFormat format = new SimpleDateFormat("hh:mm, MMM d");
                    lostTime.setText(getString(R.string.loose_time_filled, format.format(post.lostTime)));

                    if (post.lostTime.getTime() == post.postTime.getTime()) {
                        rightNowTime.setChecked(true);
                    }

                    if (post.imageSource != null) {
                        postImage.setImageBitmap(ImageHelper.getScaledBitmap(post.getImage(), ImageHelper.DpToPx(150, EditPostActivity.this)));
                    }
                } else {
                    Toast.makeText(EditPostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        updateRequest.postId = post.id;
        updateRequest.postType = Optional.ofNullable(createPost.postType).orElse(post.postType);
        updateRequest.size = Optional.ofNullable(createPost.size).orElse(post.size);
        updateRequest.species = Optional.ofNullable(createPost.species).orElse(post.species);
        updateRequest.breed = Optional.ofNullable(createPost.breed).orElse(post.breed);
        updateRequest.color = Optional.ofNullable(createPost.color).orElse(post.color);
        updateRequest.content = Optional.ofNullable(createPost.content).orElse(post.content);
        updateRequest.location = Optional.ofNullable(createPost.location).orElse(post.location);
        updateRequest.location.id = post.location.id;
        updateRequest.lostTime = Optional.ofNullable(createPost.lostTime).orElse(post.lostTime);
        updateRequest.imageSource = Optional.ofNullable(createPost.imageSource).orElse(post.imageSource);

        postService.updatePost(updateRequest, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Post updated", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    Toast.makeText(EditPostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSpinnerItem(Spinner spinner, String[] items, String value) {
        int index = IntStream.range(0, items.length)
                .filter(i -> items[i].equals(value))
                .findFirst()
                .getAsInt();

        spinner.setSelection(index);
    }
}
