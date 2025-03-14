package com.example.bike;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private ImageView ThemeSelector;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtém as preferências do usuário
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);

        // Aplica o tema antes de carregar o layout
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        // Encontra o botão de alternância de tema
        ThemeSelector = findViewById(R.id.temas);
        updateIcon(isDarkMode);

        // Adiciona funcionalidade ao botão de tema
        ThemeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
                boolean newMode = !isDarkMode;

                // Salva a escolha do usuário
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("dark_mode", newMode);
                editor.apply();

                // Alterna o modo de exibição
                if (newMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Atualiza o ícone do botão
                updateIcon(newMode);
            }
        });
    }

    // Atualiza o ícone do botão de troca de tema
    private void updateIcon(boolean isDarkMode) {
        if (ThemeSelector != null) {
            if (isDarkMode) {
                ThemeSelector.setImageResource(R.drawable.light_mode);
            } else {
                ThemeSelector.setImageResource(R.drawable.dark_mode);
            }
        }
    }
}
