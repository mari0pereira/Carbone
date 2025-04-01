package com.example.bike;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bike.dao.BicicletaDAO;
import com.example.bike.database.appDatabase;
import com.example.bike.databinding.ActivityBicicletasBinding;
import com.example.bike.models.Bicicleta;
import com.example.bike.ui.BicicletaAdapter;

import java.util.List;

public class BicicletasActivity extends AppCompatActivity {
    private ActivityBicicletasBinding binding;
    private appDatabase db;
    private String categoria;
    private BicicletaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBicicletasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Recupera a categoria da Intent
        categoria = getIntent().getStringExtra("CATEGORIA");

        // Inicializa o banco de dados
        db = appDatabase.getInstance(this);

        // Configura o RecyclerView e adapter
        adapter = new BicicletaAdapter();
        binding.recyclerBikes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerBikes.setAdapter(adapter);

        // Carrega as bicicletas em uma aba separada
        new Thread(() -> {
            BicicletaDAO dao = db.bicicletaDAO();
            final List<Bicicleta> bicicletas = dao.getBicicletasByCategoria(categoria);

            // Atualiza a UI na thread principal
            runOnUiThread(() -> {
                adapter.setBicicletas(bicicletas);
            });
        }).start();
    }
}