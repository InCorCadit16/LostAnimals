package com.pbl.animals.models.contracts.responses;

import com.pbl.animals.models.User;
import com.pbl.animals.models.inner.SignInResult;

public class LoginResponse {
    public String token;
    public User user;
    public SignInResult result;
}
