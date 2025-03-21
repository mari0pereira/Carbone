package com.example.bike.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;
<<<<<<< HEAD

=======
>>>>>>> 3bbc52ef55c4b838b76e9e2cb4abb1ea17526c5e
import com.example.bike.models.Bicicleta;
import java.util.List;

@Dao
public interface BicicletaDAO {
    @Query("SELECT * FROM bicicletas")
    List<Bicicleta> getAll();

    @Query("SELECT * FROM bicicletas WHERE categoria = :categoria")
    List<Bicicleta> getBicicletasPorCategoria(String categoria);

    @Insert
    void insert(Bicicleta bicicleta);

    @Update
    void update(Bicicleta bicicleta);

    @Delete
    void delete(Bicicleta bicicleta);
<<<<<<< HEAD
=======

    // Alterar os dados da bicicleta
    @Update
    void update(Bicicleta bicicleta);

    // Buscar todas as bicicleta
    @Query("SELECT * FROM bicicletas")
    List<Bicicleta> getAllBicicletas();
>>>>>>> 3bbc52ef55c4b838b76e9e2cb4abb1ea17526c5e
}
