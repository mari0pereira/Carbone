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

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

           // binding.btnSaberMaisSense.setOnClickListener(v -> {
                // CLICK no botão "SABER MAIS" na seção da Sense
           // });

          //  binding.btnSaberMaisSwift.setOnClickListener(v -> {
                // CLICK no botão "SABER MAIS" na seção da Swift
           // });
        }
    }
