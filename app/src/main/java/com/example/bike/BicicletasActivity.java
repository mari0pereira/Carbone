package com.example.bike;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bike.api.ApiService;
import com.example.bike.databinding.ActivityBicicletasBinding;
import com.example.bike.ui.BicicletaAdapter;

/**
 * Activity que exibe uma lista de bicicletas por categoria
 * Usa a API para buscar os dados do servidor
 */
public class BicicletasActivity extends AppCompatActivity {
    private ActivityBicicletasBinding binding;
    private String categoria;
    private BicicletaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa o ViewBinding
        binding = ActivityBicicletasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recupera a categoria da Intent
        categoria = getIntent().getStringExtra("categoria");

        // Configura o RecyclerView e adapter
        adapter = new BicicletaAdapter();
        binding.recyclerBikes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBikes.setAdapter(adapter);

        // Carrega as bicicletas da API
        carregarBicicletas();
    }

    /**
     * Carrega as bicicletas da API através da categoria selecionada
     * Exemplo: Gravel, Mountain Bike (MTB)
     */
    private void carregarBicicletas() {
        // Chama a API para buscar as bicicletas
        ApiService.getBicicletas(categoria, bicicletas -> {

            // Atualiza o adapter com os dados
            adapter.setBicicletas(bicicletas);

            // Se não houver resultados, exibe uma mensagem
            if (bicicletas.isEmpty()) {
                // Em breve
            }
        });
    }

}