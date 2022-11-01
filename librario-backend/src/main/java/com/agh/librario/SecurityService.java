package com.agh.librario;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}