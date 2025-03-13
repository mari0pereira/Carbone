package com.example.bike.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.bike.DAO.BicicletaDAO;
import com.example.bike.DAO.UsuarioDAO;
import com.example.bike.models.Bicicleta;
import com.example.bike.models.Usuario;

@Database(entities = {Bicicleta.class, Usuario.class}, version = 1)
public abstract class appDatabase extends RoomDatabase {
    private static volatile appDatabase INSTANCE;

    // Métodos abstratos para acessar os DAOs
    public abstract BicicletaDAO bicicletaDAO();
    public abstract UsuarioDAO usuarioDAO();

    public static appDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (appDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    appDatabase.class, "bike_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
