package com.example.bike.ui;

import android.annotation.SuppressLint;
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
    private OnBicicletaClickListener clickListener;

    public interface OnBicicletaClickListener {
        void onLongClick(Bicicleta bicicleta);
    }

    public BicicletaAdapter() {
        this.bicicletas = new ArrayList<>();
    }

    public void setClickListener(OnBicicletaClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public BicicletaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBikeBinding binding = ItemBikeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BicicletaViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BicicletaViewHolder holder, int position) {
        Bicicleta bicicleta = bicicletas.get(position);
        holder.bind(bicicleta);
        // As imagens serão tratadas quando implementar a API
    }

    @Override
    public int getItemCount() {
        return bicicletas.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBicicletas(List<Bicicleta> bicicletas) {
        this.bicicletas = bicicletas;
        notifyDataSetChanged();
    }

    public void removeBicicleta(Bicicleta bicicleta) {
        int position = bicicletas.indexOf(bicicleta);
        if (position > -1) {
            bicicletas.remove(position);
            notifyItemRemoved(position);
        }
    }
}