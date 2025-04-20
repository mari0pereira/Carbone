package com.example.bike.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bike.dao.BicicletaDAO;
import com.example.bike.dao.UsuarioDAO;
import com.example.bike.models.Bicicleta;
import com.example.bike.models.Usuario;

@Database(entities = {Bicicleta.class, Usuario.class}, version = 1)
public abstract class appDatabase extends RoomDatabase {
    private static volatile appDatabase INSTANCE;

    // Métodos abstratos para acessar os DAOs
    public abstract UsuarioDAO usuarioDAO();
    public abstract BicicletaDAO bicicletaDAO();

    public static synchronized appDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    appDatabase.class,
                    "bike_database"
            ).build();
        }
        return INSTANCE;
    }
}