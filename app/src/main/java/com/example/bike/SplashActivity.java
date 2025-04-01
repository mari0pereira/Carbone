package com.example.bike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VIEWBINDING
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aguardar um curto período (opcional, para mostrar uma tela de splash)
        new Handler().postDelayed(() -> {
            // Verificar se o usuário está logado
            SharedPreferences prefs = getSharedPreferences("BikeAppPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("Logado", false);

            Intent intent;
            if (isLoggedIn) {
                // Se estiver logado, vai para MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Se não estiver logado, vai para LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 1500); // 1.5 segundos
    }
}