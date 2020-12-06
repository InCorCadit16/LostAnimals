package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pbl.animals.R;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;
import com.pbl.animals.models.inner.IdentityError;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.utils.ImageHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    AuthenticationService authService;

    private ImageView userImage;
    private RelativeLayout addImage;

    @Email
    @NotEmpty
    private EditText registerEmail;

    @NotEmpty
    private EditText registerFirstName;

    private EditText registerLastName;

    @Digits(integer = 9, messageResId = R.string.phone_format_error)
    private EditText registerPhone;

    @Password(min = 8)
    private EditText registerPassword;

    @ConfirmPassword
    private EditText registerPasswordRepeat;

    private Button registerButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authService = AuthenticationService.getAuthenticationService(this);

        userImage = findViewById(R.id.register_image);
        addImage = findViewById(R.id.register_image_button);
        registerEmail = findViewById(R.id.register_email);
        registerFirstName = findViewById(R.id.register_first_name);
        registerLastName = findViewById(R.id.register_last_name);
        registerPhone = findViewById(R.id.register_phone);
        registerPassword = findViewById(R.id.register_password);
        registerPasswordRepeat = findViewById(R.id.register_password_repeat);
        registerButton = findViewById(R.id.register_button);

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        registerButton.setOnClickListener((View view) -> {
            validator.validate();
        });

        addImage.setOnClickListener((View view) -> {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile;
                try {
                    photoFile = ImageHelper.createImageFile(RegisterActivity.this);
                } catch (IOException ioe) {
                    Toast.makeText(RegisterActivity.this, "Error while taking image", Toast.LENGTH_LONG).show();
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            userImage.setBackgroundColor(Color.BLACK);
            userImage.setImageBitmap(ImageHelper.getScaledBitmap(ImageHelper.DpToPx(200, this)));
        }
    }

    @Override
    public void onValidationSucceeded() {
        RegistrationRequest request = new RegistrationRequest();
        request.email = registerEmail.getText().toString();
        request.firstName = registerFirstName.getText().toString();

        if (!registerLastName.getText().toString().isEmpty()) {
            request.lastName = registerLastName.getText().toString();
        }

        if (!registerPhone.getText().toString().isEmpty()) {
            request.phone = registerPhone.getText().toString();
        }

        if (ImageHelper.currentFile != null) {
            request.imageSource = ImageHelper.imageToByteArray();
        }

        request.password = registerPassword.getText().toString();
        request.passwordRepeat = registerPasswordRepeat.getText().toString();

        authService.register(request, new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().result.succeeded) {
                        authService.token = response.body().token;
                        authService.user = response.body().user;
                        authService.saveToken(RegisterActivity.this);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        for (IdentityError error: response.body().result.errors) {
                            Toast.makeText(RegisterActivity.this, error.description, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, R.string.request_error, Toast.LENGTH_LONG).show();
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
