package com.pbl.animals.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pbl.animals.R;
import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.UpdateUserRequest;
import com.pbl.animals.services.AuthenticationService;
import com.pbl.animals.ui.activities.RegisterActivity;
import com.pbl.animals.utils.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment implements Validator.ValidationListener {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private AuthenticationService authService;
    private UpdateUserRequest updateRequest;

    private ImageView userImage;
    private RelativeLayout addImage;

    @Email
    @NotEmpty
    private EditText email;

    @NotEmpty
    private EditText firstName;

    private EditText lastName;

    @Digits(integer = 9, messageResId = R.string.phone_format_error)
    private EditText phone;

    private Button updateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        authService = AuthenticationService.getAuthenticationService(getContext());
        updateRequest = new UpdateUserRequest();

        userImage = v.findViewById(R.id.user_image);
        addImage = v.findViewById(R.id.image_button);
        email = v.findViewById(R.id.email);
        firstName = v.findViewById(R.id.first_name);
        lastName = v.findViewById(R.id.last_name);
        phone = v.findViewById(R.id.phone);
        updateButton = v.findViewById(R.id.update_button);

        setupUserData();

        addImage.setOnClickListener((View btn) -> {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile;
                try {
                    photoFile = ImageHelper.createImageFile(getActivity());
                } catch (IOException ioe) {
                    Toast.makeText(getActivity(), "Error while taking image", Toast.LENGTH_LONG).show();
                    return;
                }

                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(getActivity(),
                            "com.pbl.animals.fileprovider",
                            photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        updateButton.setOnClickListener((View btn) -> {
            validator.validate();
        });
        return v;
    }

    private void setupUserData() {
        if (authService.user.imageSource != null) {
            userImage.setBackgroundColor(Color.BLACK);
            userImage.setImageBitmap(authService.user.getImage());
        }

        firstName.setText(authService.user.firstName);
        lastName.setText(authService.user.lastName);
        email.setText(authService.user.email);
        phone.setText(authService.user.phoneNumber);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            userImage.setBackgroundColor(Color.BLACK);
            userImage.setImageBitmap(ImageHelper.getScaledBitmap(ImageHelper.DpToPx(200, getActivity())));
        }
    }

    @Override
    public void onValidationSucceeded() {
        updateRequest.firstName = firstName.getText().toString();
        updateRequest.lastName = lastName.getText().toString();
        updateRequest.phoneNumber = phone.getText().toString();
        updateRequest.imageResource = ImageHelper.imageToByteArray();

        authService.updateUser(updateRequest, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Account information updated", Toast.LENGTH_LONG).show();
                    authService.user = response.body();
                    setupUserData();
                }
                else {
                    Toast.makeText(getContext(), "Failed to update account information", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), R.string.request_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error: errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
