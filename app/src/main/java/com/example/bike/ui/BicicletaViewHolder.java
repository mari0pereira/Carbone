package com.example.bike.ui;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bike.databinding.ItemBikeBinding;
import com.example.bike.models.Bicicleta;

import java.text.NumberFormat;
import java.util.Locale;

public class BicicletaViewHolder extends RecyclerView.ViewHolder {
    private final ItemBikeBinding binding;
    private final NumberFormat currencyFormat;

    public BicicletaViewHolder(ItemBikeBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }

    public void bind(Bicicleta bike) {
        // Configura a imagem
        binding.imgBike.setImageResource(bike.getImageUrl());

        // Configura o nome e modelo
        binding.txtNome.setText(bike.getNome());
        binding.txtModelo.setText(" " + bike.getModelo());

        // Formata e configura o preço
        String preco = currencyFormat.format(bike.getPreco());
        binding.txtPreco.setText(preco);

        // Configura a descrição
        binding.txtDescricao.setText(bike.getDescricao());
    }
}
