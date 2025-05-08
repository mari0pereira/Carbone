package com.example.bike.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.example.bike.database.appDatabase;
import com.example.bike.models.Bicicleta;
import com.example.bike.models.Usuario;
import com.example.bike.utils.PasswordUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ApiService {
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static appDatabase database;

    public static void init(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(
                    context.getApplicationContext(),
                    appDatabase.class,
                    "bike_database"
            ).build();
        }
    }

    // Interfaces de callback
    public interface BooleanCallback {
        void onResult(boolean result);
    }

    public interface UsuarioCallback {
        void onResult(Usuario usuario);
    }

    public interface BicicletasCallback {
        void onResult(List<Bicicleta> bicicletas);
    }

    // Métodos para usuários
    public static void checkEmailExists(String email, BooleanCallback callback) {
        executor.execute(() -> {
            int count = database.usuarioDAO().checkEmailExists(email);
            boolean exists = count > 0;
            mainHandler.post(() -> callback.onResult(exists));
        });
    }

    public static void cadastrarUsuario(Usuario usuario, BooleanCallback callback) {
        executor.execute(() -> {
            try {
                database.usuarioDAO().insert(usuario);
                mainHandler.post(() -> callback.onResult(true));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onResult(false));
            }
        });
    }

    public static void login(String email, String senhaHash, UsuarioCallback callback) {
        executor.execute(() -> {
            Usuario usuario = database.usuarioDAO().getUsuarioByEmail(email);
            if (usuario != null && PasswordUtils.verifyPassword(senhaHash, usuario.getSenha())) {
                mainHandler.post(() -> callback.onResult(usuario));
            } else {
                mainHandler.post(() -> callback.onResult(null));
            }
        });
    }

    // Método temporário para bicicletas (serão implementados depois)
    public static void getBicicletas(String categoria, BicicletasCallback callback) {
        executor.execute(() -> {
            try {
                // Lista vazia temporária - será substituída pela implementação real (Será no Supabase/Firebase)
                List<Bicicleta> bicicletas = database.bicicletaDAO().getBicicletasByCategoria(categoria);
                mainHandler.post(() -> callback.onResult(bicicletas));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onResult(null));
            }
        });
    }
}