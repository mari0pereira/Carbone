package com.example.bike.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bike.databinding.ItemBikeBinding;
import com.example.bike.models.Bicicleta;

import java.util.ArrayList;
import java.util.List;

public class BicicletaAdapter extends RecyclerView.Adapter<BicicletaViewHolder> {
    private List<Bicicleta> bicicletas;

    public BicicletaAdapter() {
        this.bicicletas = new ArrayList<>();
    }

    @NonNull
    @Override
    public BicicletaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBikeBinding binding = ItemBikeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BicicletaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BicicletaViewHolder holder, int position) {
        holder.bind(bicicletas.get(position));
    }

    @Override
    public int getItemCount() {
        return bicicletas.size();
    }

    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
        notifyDataSetChanged();
    }
}
