package com.pbl.animals.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pbl.animals.R;
import com.pbl.animals.models.Location;
import com.pbl.animals.models.Post;
import com.pbl.animals.models.contracts.requests.CreatePostRequest;
import com.pbl.animals.models.contracts.responses.LookupsResponse;
import com.pbl.animals.models.inner.BreedLookup;
import com.pbl.animals.models.inner.ColorLookup;
import com.pbl.animals.models.inner.PostType;
import com.pbl.animals.models.inner.Size;
import com.pbl.animals.models.inner.SpeciesLookup;
import com.pbl.animals.services.LookupService;
import com.pbl.animals.services.PostService;
import com.pbl.animals.utils.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_MAP_POINT = 2;

    private LookupService lookupService;
    private PostService postService;
    private CreatePostRequest createPost;

    private Calendar lostTimeState;

    Spinner speciesSpinner;

    Spinner breedSpinner;

    Spinner colorSpinner;

    Spinner sizeSpinner;

    Spinner typeSpinner;

    RelativeLayout imageButton;
    ImageView postImage;

    @NotEmpty
    @Length(min=4,max=4000)
    EditText description;

    @Length(min=11, message="It's required to indicate location")
    TextView location;
    Button locationButton;

    @Length(min=12, message="It's required to indicate loose time")
    TextView lostTime;
    Button lostTimeButton;
    CheckBox rightNowTime;

    Button createPostButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        lookupService = LookupService.getLookupService(this);
        postService = PostService.getPostService(this);
        lostTimeState = Calendar.getInstance();
        createPost = new CreatePostRequest();

        speciesSpinner = findViewById(R.id.species_spinner);
        breedSpinner = findViewById(R.id.breed_spinner);
        colorSpinner = findViewById(R.id.color_spinner);
        sizeSpinner = findViewById(R.id.size_spinner);
        typeSpinner = findViewById(R.id.type_spinner);

        imageButton = findViewById(R.id.image_button);
        postImage = findViewById(R.id.image);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        locationButton = findViewById(R.id.button_location);
        lostTime = findViewById(R.id.lost_time);
        lostTimeButton = findViewById(R.id.button_lost_time);
        rightNowTime = findViewById(R.id.right_now_checkbox);

        createPostButton = findViewById(R.id.create_post);

        getLookups();

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        imageButton.setOnClickListener((View v) -> setupCameraIntent());

        locationButton.setOnClickListener((View v) -> chooseMapPoint());

        lostTimeButton.setOnClickListener((View v) -> chooseLostTime());

        rightNowTime.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            Date date = new Date();
            if (b) {
                createPost.lostTime = date;
                SimpleDateFormat format = new SimpleDateFormat("hh:mm, MMM d");
                lostTime.setText(getString(R.string.loose_time_filled, format.format(date)));
            } else {
                createPost.lostTime = null;
                lostTime.setText(getString(R.string.loose_time));
            }
        });

        createPostButton.setOnClickListener((View v) -> {
            createPost.content = description.getText().toString();

            validator.validate();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                postImage.setBackgroundColor(Color.BLACK);
                postImage.setImageBitmap(ImageHelper.getScaledBitmap(ImageHelper.DpToPx(200, this)));
                createPost.imageSource = ImageHelper.imageToByteArray();
            } else if (requestCode == REQUEST_MAP_POINT) {
                double longitude = data.getDoubleExtra(MapPointPickerActivity.Longitude,-1);
                double latitude = data.getDoubleExtra(MapPointPickerActivity.Latitude,-1);

                Location place = new Location();
                place.longitude = longitude;
                place.latitude = latitude;
                createPost.location = place;
                location.setText(getString(R.string.location_filled, longitude, latitude));
            }
        }

    }

    private void getLookups() {
        lookupService.getLookups(new Callback<LookupsResponse>() {

            @Override
            public void onResponse(Call<LookupsResponse> call, Response<LookupsResponse> response) {
                if (response.isSuccessful()) {
                    createSpinners(response.body());
                } else {
                    Toast.makeText(CreatePostActivity.this, "Failed to load lookups", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LookupsResponse> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createSpinners(LookupsResponse response) {
        String[] names = Stream.of(response.species).map(s -> s.name).toArray(String[]::new);
        createPost.species = response.species[0];
        setupSpinner(names, speciesSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createPost.species = Stream.of(response.species)
                        .filter(s -> s.name.equals(adapterView.getSelectedItem()))
                        .findFirst()
                        .get();

                String[] names = Stream.of(createPost.species.breeds.toArray(new BreedLookup[]{})).map(s -> s.name).toArray(String[]::new);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreatePostActivity.this, R.layout.spinner_item, names);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                breedSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        names = Stream.of(response.species[0].breeds.toArray(new BreedLookup[]{})).map(b -> b.name).toArray(String[]::new);
        createPost.breed = response.species[0].breeds.get(0);
        setupSpinner(names, breedSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createPost.breed = Stream.of(createPost.species.breeds.toArray(new BreedLookup[]{}))
                        .filter(b -> b.name.equals(adapterView.getSelectedItem()))
                        .findFirst()
                        .get();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        names = Stream.of(response.colors).map(c -> c.name).toArray(String[]::new);
        createPost.color = response.colors[0];
        setupSpinner(names, colorSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createPost.color = Stream.of(response.colors)
                        .filter(c -> c.name.equals(adapterView.getSelectedItem()))
                        .findFirst()
                        .get();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        names = Stream.of(Size.values()).map(s -> s.name()).toArray(String[]::new);
        createPost.size = Size.Small;
        setupSpinner(names, sizeSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createPost.size = Stream.of(Size.values())
                        .filter(c -> c.name().equals((adapterView.getSelectedItem())))
                        .findFirst()
                        .get();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        names = Stream.of(PostType.values()).map(s -> {
            StringBuilder builder = new StringBuilder(s.name());
            for (int i = 1; i < builder.length(); i++) {
                if (builder.charAt(i) >= 'A' && builder.charAt(i) <= 'Z') {
                    builder.insert(i, ' ');
                    i++;
                }
            }
            return builder.toString();
        }).toArray(String[]::new);
        createPost.postType = PostType.Lost;
        setupSpinner(names, typeSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                createPost.postType = Stream.of(PostType.values())
                        .filter(t -> t.name().equals((String) (adapterView.getSelectedItem())))
                        .findFirst()
                        .get();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setupSpinner(String[] items, Spinner spinner, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                CreatePostActivity.this,
                R.layout.spinner_item,
                items);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    private void setupCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = ImageHelper.createImageFile(CreatePostActivity.this);
            } catch (IOException ioe) {
                Toast.makeText(CreatePostActivity.this, "Error while taking image", Toast.LENGTH_LONG).show();
                return;
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.pbl.animals.fileprovider",
                        photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void chooseLostTime() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(CreatePostActivity.this,
                null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dateDialog.setOnDateSetListener((DatePicker datePicker, int year, int month, int dayOfMonth) -> {
            lostTimeState.set(Calendar.YEAR, year);
            lostTimeState.set(Calendar.MONTH, month);
            lostTimeState.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Calendar c = Calendar.getInstance();
            TimePickerDialog timeDialog = new TimePickerDialog(CreatePostActivity.this,
                    (TimePicker timePicker, int hour, int minute) -> {
                            lostTimeState.set(Calendar.HOUR, hour);
                            lostTimeState.set(Calendar.MINUTE, minute);
                            createPost.lostTime = lostTimeState.getTime();

                            SimpleDateFormat format = new SimpleDateFormat("hh:mm, MMM d");
                            lostTime.setText(getString(R.string.loose_time_filled, format.format(createPost.lostTime)));
                    },
                    c.get(Calendar.HOUR),
                    c.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(CreatePostActivity.this));

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

        postService.createPost(createPost, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(CreatePostActivity.this, PostActivity.class);
                    intent.putExtra(PostActivity.POST_ID, response.body());
                    CreatePostActivity.this.finish();
                    startActivity(intent);

                } else {
                    Toast.makeText(CreatePostActivity.this, "Failed to create createPost", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        if (ImageHelper.currentFile != null) {
            ImageHelper.currentFile.delete();
            ImageHelper.currentFile = null;
        }

        super.onDestroy();
    }


}
