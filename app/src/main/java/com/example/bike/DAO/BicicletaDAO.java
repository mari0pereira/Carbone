package com.example.bike.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bike.model.Bicicleta;
import java.util.List;

@Dao
public interface BicicletaDAO {

    @Query("SELECT * FROM bicicletas")
    List<Bicicleta> getAll();

    @Query("SELECT * FROM bicicletas WHERE categoria = :categoria")
    List<Bicicleta> getBicicletasByCategoria(String categoria);

    @Query("SELECT * FROM bicicletas WHERE id = :id")
    Bicicleta getBicicletaById(int id);

    @Insert
    void insert(Bicicleta bicicleta);

    @Update
    void update(Bicicleta bicicleta);

    @Delete
    void delete(Bicicleta bicicleta);


}
