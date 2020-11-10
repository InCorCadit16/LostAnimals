package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pbl.animals.R;
import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;
import com.pbl.animals.models.inner.IdentityError;
import com.pbl.animals.services.AuthenticationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {
    AuthenticationService authService;

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


}
