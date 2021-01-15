package com.pbl.animals.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pbl.animals.R;
import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Location;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.services.CommentService;
import com.pbl.animals.utils.ImageHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCommentActivity extends AuthenticationActivity implements Validator.ValidationListener {
    private static final int REQUEST_MAP_POINT = 2;

    private CommentService commentService;
    private AuthenticationService authService;

    Comment comment;

    protected Calendar seenTimeState;

    @NotEmpty
    @Length(min=4,max=4000)
    EditText content;

    @Length(min=11, message="It's required to indicate location")
    TextView location;
    Button locationButton;

    @Length(min=12, message="It's required to indicate seen time")
    TextView seenTime;
    Button seenTimeButton;
    CheckBox rightNowTime;

    Button createCommentButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_create);

        authService = AuthenticationService.getAuthenticationService(this);
        commentService = CommentService.getCommentService(this);
        comment = new Comment();

        comment.userId = authService.user.id;
        comment.postId = getIntent().getLongExtra(PostActivity.POST_ID, -1);

        seenTimeState = Calendar.getInstance();

        content = findViewById(R.id.content);
        location = findViewById(R.id.location);
        locationButton = findViewById(R.id.button_location);
        seenTime = findViewById(R.id.seen_time);
        seenTimeButton = findViewById(R.id.button_seen_time);
        rightNowTime = findViewById(R.id.right_now_checkbox);

        createCommentButton = findViewById(R.id.create_comment);

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        locationButton.setOnClickListener((View v) -> chooseMapPoint());

        seenTimeButton.setOnClickListener((View v) -> chooseLostTime());

        rightNowTime.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            Date date = new Date();
            if (b) {
                comment.commentTime = date;
                comment.seenTime = date;
                SimpleDateFormat format = new SimpleDateFormat("hh:mm, MMM d");
                seenTime.setText(getString(R.string.seen_time_filled, format.format(date)));
            } else {
                comment.commentTime = null;
                comment.seenTime = null;
                seenTime.setText(getString(R.string.seen_time));
            }
        });

        createCommentButton.setOnClickListener((View v) -> {
            comment.content = content.getText().toString();

            validator.validate();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MAP_POINT) {
                double longitude = data.getDoubleExtra(MapPointPickerActivity.Longitude,-1);
                double latitude = data.getDoubleExtra(MapPointPickerActivity.Latitude,-1);

                Location place = new Location();
                place.longitude = longitude;
                place.latitude = latitude;
                comment.location = place;
                location.setText(getString(R.string.location_filled, longitude, latitude));
            }
        }

    }

    private void chooseLostTime() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(CreateCommentActivity.this,
                null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dateDialog.setOnDateSetListener((DatePicker datePicker, int year, int month, int dayOfMonth) -> {
            seenTimeState.set(Calendar.YEAR, year);
            seenTimeState.set(Calendar.MONTH, month);
            seenTimeState.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Calendar c = Calendar.getInstance();
            TimePickerDialog timeDialog = new TimePickerDialog(CreateCommentActivity.this,
                    (TimePicker timePicker, int hour, int minute) -> {
                        seenTimeState.set(Calendar.HOUR, hour);
                        seenTimeState.set(Calendar.MINUTE, minute);
                        comment.seenTime = seenTimeState.getTime();

                        SimpleDateFormat format = new SimpleDateFormat("hh:mm, MMM d");
                        seenTime.setText(getString(R.string.seen_time_filled, format.format(comment.seenTime)));
                    },
                    c.get(Calendar.HOUR),
                    c.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(CreateCommentActivity.this));

            timeDialog.show();
        });

        dateDialog.show();
    }

    private void chooseMapPoint() {
        Intent intent = new Intent(this, MapPointPickerActivity.class);

        startActivityForResult(intent, REQUEST_MAP_POINT);
    }

    @Override
    public void onValidationSucceeded() {
        if (comment.commentTime == null) {
            comment.commentTime = new Date();
        }

        commentService.createComment(comment, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(CreateCommentActivity.this, PostActivity.class);
                    intent.putExtra(PostActivity.POST_ID, comment.postId);
                    CreateCommentActivity.this.finish();
                    startActivity(intent);
                    Toast.makeText(CreateCommentActivity.this, "Comment added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateCommentActivity.this, "Failed to create comment", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreateCommentActivity.this, "Failed to make request", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error: errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
