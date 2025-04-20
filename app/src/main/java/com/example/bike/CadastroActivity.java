package com.example.bike;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.database.appDatabase;
import com.example.bike.databinding.ActivityCadastroBinding;
import com.example.bike.models.Usuario;
import com.example.bike.utils.Validador;

public class CadastroActivity extends AppCompatActivity {
    private ActivityCadastroBinding binding;
    private appDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = appDatabase.getInstance(this);

        binding.btnCadastrar.setOnClickListener(v -> realizarCadastro());
    }

    private void realizarCadastro() {
        String nome = binding.editTextNome.getText().toString();
        String email = binding.editTextEmailCadastro.getText().toString();
        String senha = binding.editTextSenhaCadastro.getText().toString();
        String telefone = binding.editTextTelefone.getText().toString();

        // Validação de campos vazios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação com expressões regulares
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

        new Thread(() -> {
            int emailExists = db.usuarioDAO().checkEmailExists(email);

            if (emailExists > 0) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            // Usa a senha com hash em vez da senha em texto plano
            Usuario novoUsuario = new Usuario(nome, email, senhaHash, telefone);
            db.usuarioDAO().insert(novoUsuario);

            runOnUiThread(() -> {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}