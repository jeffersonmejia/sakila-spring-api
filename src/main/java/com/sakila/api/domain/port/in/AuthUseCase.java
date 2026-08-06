package com.sakila.api.domain.port.in;

import com.sakila.api.domain.model.LoginResult;

public interface AuthUseCase {

    LoginResult login(String username, String password);
}
