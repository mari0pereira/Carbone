package com.example.bike.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import com.example.bike.models.Bicicleta;
import com.example.bike.models.Usuario;

import java.util.List;

@Dao
public interface UsuarioDAO {
    // Inserir um usuário no banco de dados
    @Insert
    void insert(Usuario usuario);

    // Excluir um usuário
    @Delete
    void delete(Usuario usuario);

    // Lista todos os usuários cadastrados
    @Query("SELECT * FROM usuarios")
    List<Usuario> getAllUsuarios();
}
