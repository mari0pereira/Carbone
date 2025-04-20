package com.example.bike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.database.appDatabase;
import com.example.bike.databinding.ActivityLoginBinding;
import com.example.bike.models.Usuario;
import com.example.bike.utils.Validador;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private appDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = appDatabase.getInstance(this);

        // Botão de login
        binding.btnLogin.setOnClickListener(v -> RealizarLogin());

        // CADASTRO -----
        binding.textViewCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void RealizarLogin() {
        String email = binding.editTextEmail.getText().toString();
        String senha = binding.editTextSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato do email
        if (!Validador.isEmailValido(email)) {
            binding.editTextEmail.setError("Email inválido");
            return;
        }

        // Hash da senha para comparação
        String senhaHash = Validador.hashSenha(senha);

        new Thread(() -> {
            // Busca pelo email e senha com hash
            Usuario usuario = db.usuarioDAO().loginComHash(email, senhaHash);

            runOnUiThread(() -> {
                if (usuario != null) {
                    // Salvar login
                    SharedPreferences prefs = getSharedPreferences("BikeAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Logado", true);
                    editor.putInt("UsuarioId", usuario.getId());
                    editor.putString("UsuarioNome", usuario.getNome());
                    editor.apply();

                    // Ir para MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}