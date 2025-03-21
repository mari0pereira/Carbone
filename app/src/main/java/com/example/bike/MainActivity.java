package com.example.bike;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
=======
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bike.databinding.ActivityMainBinding;
>>>>>>> 3bbc52ef55c4b838b76e9e2cb4abb1ea17526c5e

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
<<<<<<< HEAD

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
=======
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button1.setOnClickListener(v -> {
            Intent intent = new Intent(this, Sabermais.class);
            startActivity(intent);
        });
>>>>>>> 3bbc52ef55c4b838b76e9e2cb4abb1ea17526c5e

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
