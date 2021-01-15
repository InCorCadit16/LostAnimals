package com.pbl.animals.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.animals.R;
import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Post;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.services.CommentService;
import com.pbl.animals.services.PostService;
import com.pbl.animals.utils.ImageHelper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AuthenticationActivity {
    public static final String POST_ID = "postId";

    private PostService postService;
    private CommentService commentService;
    private AuthenticationService authService;

    private Post post;
    private Comment[] comments;

    private EditText authorName;
    private EditText authorEmail;
    private EditText authorPhone;

    private TextView postType;
    private EditText postSpecies;
    private EditText postColor;
    private EditText postSize;
    private TextView postContent;
    private ImageView postImage;
    private Button commentButton;

    private RecyclerView commentsRecycler;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        authorName = findViewById(R.id.author_name);
        authorEmail = findViewById(R.id.author_email);
        authorPhone = findViewById(R.id.author_phone);
        postType = findViewById(R.id.post_type);
        postSpecies = findViewById(R.id.post_species);
        postColor = findViewById(R.id.post_color);
        postSize = findViewById(R.id.post_size);
        postContent = findViewById(R.id.post_content);
        postImage = findViewById(R.id.post_image);
        commentButton = findViewById(R.id.comment_button);
        commentsRecycler = findViewById(R.id.comment_recycler);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postService = PostService.getPostService(this);
        commentService = CommentService.getCommentService(this);
        authService = AuthenticationService.getAuthenticationService(this);
        long postId = getIntent().getLongExtra(POST_ID, -1);
        loadPost(postId);
        loadComments(postId);
    }


    private void loadPost(long id) {
        postService.getPostById(id, new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    post = response.body();
                    authorName.setText(post.author.getFullName());
                    authorEmail.setText(post.author.email);
                    if (post.author.phoneNumber != null)
                        authorPhone.setText(post.author.phoneNumber);
                    else
                        authorPhone.setVisibility(View.INVISIBLE);

                    postType.setText(post.postType.toString());
                    postSpecies.setText(post.species.name);
                    postColor.setText(post.color.name);
                    postSize.setText(post.size.toString());
                    postContent.setText(post.content);

                    if (post.imageSource != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(post.imageSource, 0, post.imageSource.length);
                        postImage.setImageBitmap(ImageHelper.getScaledBitmap(bitmap, ImageHelper.DpToPx(150, PostActivity.this)));
                    }

                    if (post.author.id == authService.user.id) {
                        commentButton.setVisibility(View.GONE);
                    }

                    commentButton.setOnClickListener((View v) -> {
                        Intent i = new Intent(PostActivity.this, CreateCommentActivity.class);
                        i.putExtra(POST_ID, post.id);
                        startActivity(i);
                    });

                    onCreateOptionsMenu(toolbar.getMenu());
                } else {
                    Toast.makeText(PostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComments(long id) {
        commentService.getCommentsByPostId(id, new Callback<Comment[]>() {
            @Override
            public void onResponse(Call<Comment[]> call, Response<Comment[]> response) {
                if (response.isSuccessful()) {
                    comments = response.body();
                    commentsRecycler.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                    commentsRecycler.setAdapter(new CommentAdapter(Stream.of(comments).collect(Collectors.toList())));
                } else {
                    Toast.makeText(PostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comment[]> call, Throwable t) {
                Toast.makeText(PostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (post != null) {
            MenuInflater inflater = getMenuInflater();
            if (post.author.id == authService.user.id) {
                inflater.inflate(R.menu.user_post_menu, menu);
            } else {
                inflater.inflate(R.menu.post_menu, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.map_view:
                i = new Intent(PostActivity.this, MapActivity.class);
                i.putExtra(PostActivity.POST_ID, post.id);
                i.putExtra(MapPointPickerActivity.Longitude, post.location.longitude);
                i.putExtra(MapPointPickerActivity.Latitude, post.location.latitude);
                startActivity(i);
                break;
            case R.id.delete_post:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Are you sure that you want to delete this post? This action is irreversible.")
                        .setPositiveButton("OK", (DialogInterface dialogInterface, int var) -> deletePost())
                        .setNegativeButton("CANCEL", null)
                        .create();

                dialog.show();
                break;
            case R.id.edit_post:
                i = new Intent(PostActivity.this, EditPostActivity.class);
                i.putExtra(PostActivity.POST_ID, post.id);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    private void deletePost() {
        postService.deletePost(post.id, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Post successfully deleted", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(PostActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PostActivity.this.finish();
                    startActivity(i);
                } else {
                    Toast.makeText(PostActivity.this, "Failed to delete post", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
        List<Comment> comments;


        CommentAdapter(List<Comment> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PostActivity.this);
            return new CommentHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            holder.bind(comments.get(position));
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }


    class CommentHolder extends RecyclerView.ViewHolder {
        ImageView authorImage;
        TextView content;

        public CommentHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.comment,parent,false));

            authorImage = itemView.findViewById(R.id.author_icon);
            content = itemView.findViewById(R.id.comment_content);
        }

        public void bind(Comment comment) {
            content.setText(comment.content);

            if (comment.author.imageSource != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(
                        comment.author.imageSource,
                        0,
                        comment.author.imageSource.length);

                authorImage.setImageBitmap(
                        ImageHelper.getScaledBitmap(bitmap,
                                ImageHelper.DpToPx(50, PostActivity.this)));
            }

            itemView.setOnClickListener((View v) -> {
                Intent i = new Intent(PostActivity.this, MapActivity.class);
                i.putExtra(PostActivity.POST_ID, post.id);
                i.putExtra(MapPointPickerActivity.Longitude, post.location.longitude);
                i.putExtra(MapPointPickerActivity.Latitude, post.location.latitude);

                i.putExtra(MapActivity.FOCUS_LONGITUDE, comment.location.longitude);
                i.putExtra(MapActivity.FOCUS_LATITUDE, comment.location.latitude);
                startActivity(i);
            });
        }
    }
}
