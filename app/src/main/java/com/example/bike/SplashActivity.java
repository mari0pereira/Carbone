package com.example.bike;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Infla o layout usando ViewBinding
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aguarda um curto período para mostrar o splash
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Verifica se o usuário está logado usando apenas BikeSession
            BikeSession bikeSession = (BikeSession) getApplication();
            boolean isLoggedIn = bikeSession.isLoggedIn();

            // Define a próxima tela
            Intent intent;
            if (isLoggedIn) {
                // Se estiver logado, vai para MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Se não estiver logado, vai para LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            // Inicia a próxima atividade e finaliza a splash
            startActivity(intent);
            finish();
        }, 1500); // 1.5 segundos
    }
}