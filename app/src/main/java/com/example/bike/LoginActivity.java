package com.example.bike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.SupabaseRetrofitService;
import com.example.bike.databinding.ActivityLoginBinding;
import com.example.bike.utils.Validador;

/**
 * Activity responsável pelo processo de autenticação do usuário
 * Realiza login e gerencia o redirecionamento para telas adequadas
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa o serviço do Supabase
        SupabaseRetrofitService.init(getApplicationContext());

        prefs = getSharedPreferences("BikeAppPrefs", MODE_PRIVATE);

        // Inicializa o ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura os listeners dos botões
        configurarListeners();
    }

    private void configurarListeners() {
        // Botão de login
        binding.btnLogin.setOnClickListener(v -> realizarLogin());
        binding.textViewCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Realiza o processo de autenticação do usuário
     * Valida os campos e autentica no Supabase
     */
    private void realizarLogin() {
        String email = binding.editTextEmail.getText().toString().trim();
        String senha = binding.editTextSenha.getText().toString().trim();
        // OBS: trim() remove espaços em branco

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validador.isEmailValido(email)) {
            binding.editTextEmail.setError("Email inválido");
            return;
        }

        // Desativa o botão durante o processo para evitar vários clicks
        binding.btnLogin.setEnabled(false);

        // Tenta fazer login no Supabase
        SupabaseRetrofitService.login(email, senha, usuario -> {
            runOnUiThread(() -> {
                // Reativa o botão
                binding.btnLogin.setEnabled(true);

                if (usuario != null) {
                    // Login realizado com sucesso
                    LoginSucesso(usuario);
                } else {
                    // Dados inválidos
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * Processa o login bem-sucedido e salva os dados do usuário
     * @param usuario Objeto usuário retornado pela API
     */
    private void LoginSucesso(com.example.bike.model.Usuario usuario) {
        // Atualiza o estado de login no BikeSession (classe global)
        ((BikeSession) getApplication()).login();

        // Salva as informações do usuário no SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Logado", true);
        editor.putString("UsuarioId", usuario.getId());
        editor.putString("UsuarioAuthId", usuario.getAuthUid());
        editor.putString("UsuarioNome", usuario.getNome());
        editor.putString("UsuarioEmail", usuario.getEmail());
        editor.putString("UsuarioTipo", usuario.getTipoUsuario());
        editor.apply(); // Salva as alterações

        // Redireciona para a tela principal
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // Limpa o stack de activities para que o usuário não volte para o login
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finaliza a LoginActivity
    }
}