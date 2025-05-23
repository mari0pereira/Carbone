package com.example.bike;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bike.api.SupabaseRetrofitService;
import com.example.bike.databinding.ActivityPerfilBinding;
import com.example.bike.model.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {
    private ActivityPerfilBinding binding;
    private SharedPreferences prefs;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences("BikeAppPrefs", MODE_PRIVATE);

        // Configurar bottom navigation
        BottomNavigationView bottomNavigation = binding.bottomNavigation;
        bottomNavigation.setSelectedItemId(R.id.nav_perfil);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_carrinho) {
                // OBS: Implementar CarrinhoActivity quando estiver pronto ==================
                Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_perfil) {
                return true;
            }
            return false;
        });

        // Configurar botão voltar
        binding.btnVoltar.setOnClickListener(v -> {
            onBackPressed();
        });

        carregarDadosUsuario();
        configurarListeners();
    }

    private void carregarDadosUsuario() {
        String nome = prefs.getString("UsuarioNome", "Nome do Usuário");
        String email = prefs.getString("UsuarioEmail", "email@exemplo.com");
        String fotoUrl = prefs.getString("UsuarioFotoUrl", "");

        binding.tvNomeUsuario.setText(nome);
        binding.tvEmailUsuario.setText(email);

        if (!fotoUrl.isEmpty()) {
            Picasso.get()
                    .load(fotoUrl)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(binding.imgAvatar);
        } else {
            binding.imgAvatar.setImageResource(R.drawable.user);
        }
    }

    private void configurarListeners() {
        // Upload de foto
        binding.imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        /**
         * OBSERVAÇÃO: OS NOMES DAS FUNCIONALIDADES SÃO APENAS PARA NÃO FICAR VAZIO,
         *          SE QUISER PODE TROCAR OS NOMES DO PERFIL =====
         */
        // Editar perfil
        binding.btnEditarPerfil.setOnClickListener(v -> {
            // OBS: Implementar EditarPerfilActivity quando estiver pronta ======
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Notificações
        binding.btnNotificacoes.setOnClickListener(v -> {
            // OBS: Implementar funcionalidade de notificações ========
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Lista de desejos
        binding.btnListaDesejos.setOnClickListener(v -> {
            // OBS: Implementar funcionalidade de lista de desejos ======= ou pode trocar o nome para wishlist
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Termos de uso
        binding.btnTermosUso.setOnClickListener(v -> {
            // OBS: Implementar visualização dos termos de uso ========
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Política de privacidade
        binding.btnPoliticaPrivacidade.setOnClickListener(v -> {
            // OBS: Implementar visualização da política de privacidade ======
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Reportar bug
        binding.btnReportarBug.setOnClickListener(v -> {
            // OBS: Implementar funcionalidade de reportar bugs ======
            Toast.makeText(this, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
        });

        // Logout
        binding.btnLogout.setOnClickListener(v -> {
            ((BikeSession) getApplication()).logout();
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            String nomeArquivo = UUID.randomUUID().toString() + ".jpg";
            String authUid = prefs.getString("UsuarioAuthId", "");

            if (authUid.isEmpty()) {
                Toast.makeText(this, "Usuário não identificado", Toast.LENGTH_SHORT).show();
                return;
            }

            SupabaseRetrofitService.uploadFotoPerfil(this, authUid, imageUri, nomeArquivo, new SupabaseRetrofitService.StringCallback() {
                @Override
                public void onResult(String url) {
                    runOnUiThread(() -> {
                        if (url != null) {
                            // Atualiza a imagem e salva a URL no SharedPreferences
                            Picasso.get()
                                    .load(url)
                                    .into(binding.imgAvatar);

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("UsuarioFotoUrl", url);
                            editor.apply();

                            Toast.makeText(PerfilActivity.this, "Foto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PerfilActivity.this, "Falha ao atualizar foto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}