package com.example.bike.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;
import com.example.bike.models.Bicicleta;
import java.util.List;

@Dao
public interface BicicletaDAO {
    // Inserir uma bicicleta no banco de dados
    @Insert
    void insert(Bicicleta bicicleta);

    // Excluir uma bicicleta
    @Delete
    void delete(Bicicleta bicicleta);

    // Alterar os dados da bicicleta
    @Update
    void update(Bicicleta bicicleta);

    // Buscar todas as bicicleta
    @Query("SELECT * FROM bicicletas")
    List<Bicicleta> getAllBicicletas();
}
