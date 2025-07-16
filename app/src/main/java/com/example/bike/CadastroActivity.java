package com.example.bike;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.FirebaseService;
import com.example.bike.databinding.ActivityCadastroBinding;
import com.example.bike.model.Usuario;
import com.example.bike.utils.Mascara;
import com.example.bike.utils.Validador;

import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;
    private static final String TAG = "CadastroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configura o ViewBinding
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aplica máscara no campo de telefone
        Mascara.aplicarMascaraTelefone(binding.editTextTelefone);

        // Configura o click do botão de cadastro
        binding.btnCadastrar.setOnClickListener(v -> realizarCadastro());
    }

    private void realizarCadastro() {
        String nome = binding.editTextNome.getText().toString().trim();
        String email = binding.editTextEmailCadastro.getText().toString().trim();
        String senha = binding.editTextSenhaCadastro.getText().toString().trim();
        String telefone = binding.editTextTelefone.getText().toString().trim();

        // Validação dos campos obrigatórios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação do nome
        if (nome.length() < 2) {
            binding.editTextNome.setError("Nome deve ter pelo menos 2 caracteres");
            return;
        }

        // Validações
        if (!Validador.isEmailValido(email)) {
            binding.editTextEmailCadastro.setError("Email inválido");
            return;
        }

        if (!Validador.isSenhaValida(senha)) {
            binding.editTextSenhaCadastro.setError("Senha deve ter pelo menos 6 caracteres, uma maiúscula e um número");
            return;
        }

        if (!Validador.isTelefoneValido(telefone)) {
            binding.editTextTelefone.setError("Telefone inválido. Use o formato (XX)XXXXX-XXXX");
            return;
        }

        // Desabilita o botão e mostra o loading
        binding.btnCadastrar.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);

        // Cria o objeto Usuario
        Usuario usuario = new Usuario(nome, email, senha, telefone);

        Log.d(TAG, "Iniciando cadastro do usuário: " + email);

        // Realiza o cadastro usando o FirebaseService
        FirebaseService.cadastrarUsuario(this, usuario, new FirebaseService.Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnCadastrar.setEnabled(true);

                    Log.d(TAG, "Cadastro realizado com sucesso para: " + email);
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Fecha a activity de cadastro e volta para login
                    finish();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnCadastrar.setEnabled(true);

                    Log.e(TAG, "Erro no cadastro: " + errorMessage);
                    Toast.makeText(CadastroActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}