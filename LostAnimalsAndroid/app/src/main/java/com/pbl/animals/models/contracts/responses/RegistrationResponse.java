package com.pbl.animals.models.contracts.responses;

import com.pbl.animals.models.User;
import com.pbl.animals.models.inner.IdentityResult;

public class RegistrationResponse {
    public String token;
    public User user;
    public IdentityResult result;
}
