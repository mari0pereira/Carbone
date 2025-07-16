package com.example.bike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.FirebaseService;
import com.example.bike.databinding.ActivityLoginBinding;
import com.example.bike.utils.Validador;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configura o ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura os click dos botões
        binding.btnLogin.setOnClickListener(v -> realizarLogin());
        binding.textViewCadastro.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }

    private void realizarLogin() {
        String email = binding.editTextEmail.getText().toString().trim();
        String senha = binding.editTextSenha.getText().toString().trim();

        // Validação dos campos
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validador.isEmailValido(email)) {
            binding.editTextEmail.setError("Email inválido");
            return;
        }

        // Desabilita o botão e mostra o loading
        binding.btnLogin.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        // Realiza o login usando o FirebaseService
        FirebaseService.login(this, email, senha, new FirebaseService.Callback() {
            @Override
            public void onSuccess(Map<String, Object> userData) {
                runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);

                    // Atualiza a sessão
                    BikeSession bikeSession = (BikeSession) getApplication();
                    bikeSession.login();

                    // Mostra mensagem de sucesso
                    Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Redireciona para MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}