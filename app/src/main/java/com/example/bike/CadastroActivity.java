package com.example.bike;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.ApiService;
import com.example.bike.databinding.ActivityCadastroBinding;
import com.example.bike.models.Usuario;
import com.example.bike.utils.Validador;

/**
 * Utiliza a API para cadastrar o usuário no servidor
 */
public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicia o ViewBinding
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Click do botão de cadastro
        binding.btnCadastrar.setOnClickListener(v -> realizarCadastro());
    }

    /**
     * Realiza o cadastro do usuário com validação de campos e integração com a API
     */
    private void realizarCadastro() {
        // Obtém os valores dos campos
        String nome = binding.editTextNome.getText().toString();
        String email = binding.editTextEmailCadastro.getText().toString();
        String senha = binding.editTextSenhaCadastro.getText().toString();
        String telefone = binding.editTextTelefone.getText().toString();

        // Validação de campos vazios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação
        if (!Validador.isEmailValido(email)) {
            binding.editTextEmailCadastro.setError("Email inválido");
            return;
        }

        if (!Validador.isSenhaValida(senha)) {
            binding.editTextSenhaCadastro.setError("A senha deve ter pelo menos 6 caracteres, uma letra e um número");
            return;
        }

        if (!Validador.isTelefoneValido(telefone)) {
            binding.editTextTelefone.setError("Telefone inválido. Use o formato (XX)XXXXX-XXXX");
            return;
        }

        // Hash da senha antes de armazenar
        String senhaHash = Validador.hashSenha(senha);

        // Verificar se o email já existe
        ApiService.checkEmailExists(email, exists -> {
            if (exists) {
                Toast.makeText(CadastroActivity.this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Criar novo usuário com a senha com hash
            Usuario novoUsuario = new Usuario(nome, email, senhaHash, telefone);

            // Cadastrar usuário via API
            ApiService.cadastrarUsuario(novoUsuario, success -> {
                if (success) {
                    Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish(); // Volta para a tela anterior
                } else {
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}