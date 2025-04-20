package com.example.bike;

import android.app.Application;

// Classe global que mantém o estado da sessão de login
public class BikeSession extends Application {
    private boolean isLoggedIn = false;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        isLoggedIn = true;
    }

    public void logout() {
        isLoggedIn = false;
    }
}