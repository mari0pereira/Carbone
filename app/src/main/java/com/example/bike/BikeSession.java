package com.example.bike;

import android.app.Application;
import com.example.bike.api.FirebaseConfig;

// Classe global que mantém o estado da sessão de login
public class BikeSession extends Application {
    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializa o Firebase quando o app é iniciado
        FirebaseConfig.inicializarFirebase(this);
    }

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