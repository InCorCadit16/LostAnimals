package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbl.animals.R;
import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Post;
import com.pbl.animals.services.CommentService;
import com.pbl.animals.services.PostService;
import com.pbl.animals.ui.fragments.PostsListFragment;
import com.pbl.animals.utils.ImageHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    public static final String POST_ID = "postId";

    private PostService postService;
    private CommentService commentService;

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

    private RecyclerView commentsRecycler;


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
        commentsRecycler = findViewById(R.id.comment_recycler);

        postService = PostService.getPostService(this);
        commentService = CommentService.getCommentService(this);
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
                    commentsRecycler.setAdapter(new CommentAdapter(List.of(comments)));
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
        }
    }
}
