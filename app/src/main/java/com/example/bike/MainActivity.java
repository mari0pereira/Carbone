package com.example.bike;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica o login usando apenas BikeSession
        BikeSession bikeSession = (BikeSession) getApplication();
        if (!bikeSession.isLoggedIn()) {
            // Se não estiver logado, vai para LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Configura o ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}