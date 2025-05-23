package com.example.bike;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.SupabaseRetrofitService;
import com.example.bike.databinding.ActivityCadastroBinding;
import com.example.bike.model.Usuario;
import com.example.bike.utils.Mascara;
import com.example.bike.utils.Validador;

import java.time.LocalDate;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa o Supabase
        SupabaseRetrofitService.init(getApplicationContext());

        // Inicializa o ViewBinding
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aplica a máscara de telefone
        aplicarMascaras();

        // Configura o click do botão de cadastro
        binding.btnCadastrar.setOnClickListener(v -> realizarCadastro());
    }

    private void aplicarMascaras() {
        // Aplica máscara de telefone no formato (XX)XXXXX-XXXX
        Mascara.aplicarMascaraTelefone(binding.editTextTelefone);
    }

    /**
     * Realiza o processo de cadastro do usuário
     * Valida os campos e envia os dados para o Supabase
     */
    private void realizarCadastro() {
        String nome = binding.editTextNome.getText().toString().trim();
        String email = binding.editTextEmailCadastro.getText().toString().trim();
        String senha = binding.editTextSenhaCadastro.getText().toString().trim();
        String telefone = binding.editTextTelefone.getText().toString().trim();
        // OBS: trim() remove espaços em branco

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validador.isEmailValido(email)) {
            binding.editTextEmailCadastro.setError("Email inválido");
            return;
        }

        if (!Validador.isSenhaValida(senha)) {
            binding.editTextSenhaCadastro.setError("A senha deve ter pelo menos 6 caracteres, uma letra maiúscula e um número");
            return;
        }

        if (!Validador.isTelefoneValido(telefone)) {
            binding.editTextTelefone.setError("Telefone inválido. Use o formato (XX)XXXXX-XXXX");
            return;
        }

        // Desativa o botão durante o processo
        binding.btnCadastrar.setEnabled(false);

        // Verifica se o email já existe
        SupabaseRetrofitService.checkEmailExists(email, exists -> {
            runOnUiThread(() -> {
                if (exists) {
                    // Email já existe no sistema
                    binding.btnCadastrar.setEnabled(true);
                    Toast.makeText(CadastroActivity.this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cria um novo objeto Usuario com os dados fornecidos
                Usuario novoUsuario = new Usuario(
                        nome,
                        email,
                        senha,
                        telefone,
                        LocalDate.now(), // Data de nascimento
                        "cliente" // Tipo de usuário padrão
                );

                // Envia os dados para cadastro no Supabase
                SupabaseRetrofitService.cadastrarUsuario(novoUsuario, success -> {
                    runOnUiThread(() -> {
                        // Reativa o botão
                        binding.btnCadastrar.setEnabled(true);

                        if (success) {
                            // Cadastro realizado com sucesso
                            Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                            finish(); // Volta para a tela anterior (LoginActivity)
                        } else {
                            // Erro durante o cadastro
                            Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
        });
    }
}