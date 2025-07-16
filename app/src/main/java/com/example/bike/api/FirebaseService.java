package com.example.bike.api;

import android.content.Context;
import android.util.Log;
import com.example.bike.model.Usuario;
import com.example.bike.utils.Validador;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.Timestamp;

public class FirebaseService {

    private static final String TAG = "FirebaseService";
    private static final String COLLECTION_USUARIOS = "usuarios";

    public interface Callback {
        default void onSuccess() {
        }

        default void onSuccess(Map<String, Object> userData) {
        }

        void onError(String errorMessage);
    }

    /**
     * Cadastra um novo usuário no Firebase Firestore
     * Verifica se o email já existe antes de cadastrar
    */
    public static void cadastrarUsuario(Context context, Usuario usuario, Callback callback) {
        FirebaseFirestore db = FirebaseConfig.getFirestore();

        Log.d(TAG, "Iniciando cadastro do usuário: " + usuario.getEmail());

        // Verifica se o email já existe
        db.collection(COLLECTION_USUARIOS)
                .whereEqualTo("email", usuario.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Log.w(TAG, "Email já cadastrado: " + usuario.getEmail());
                            callback.onError("Email já cadastrado");
                            return;
                        }

                        // Gera o hash da senha
                        String senhaHash = Validador.hashSenha(usuario.getSenha());
                        Log.d(TAG, "Hash da senha gerado com sucesso");

                        // Prepara os dados do usuário para salvar no Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("nome", usuario.getNome());
                        userData.put("email", usuario.getEmail());
                        userData.put("senha", senhaHash);
                        userData.put("telefone", usuario.getTelefone());
                        userData.put("dataCadastro", Timestamp.now());

                        // Salva no Firestore
                        db.collection(COLLECTION_USUARIOS)
                                .add(userData)
                                .addOnSuccessListener(documentReference -> {
                                    // Atualiza o documento com o ID gerado
                                    documentReference.update("id", documentReference.getId());

                                    Log.d(TAG, "Usuário cadastrado com sucesso! ID: " + documentReference.getId());
                                    callback.onSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Erro ao cadastrar usuário", e);
                                    callback.onError("Erro ao cadastrar: " + e.getMessage());
                                });
                    } else {
                        Log.e(TAG, "Erro ao verificar email", task.getException());
                        callback.onError("Erro ao verificar email: " + task.getException().getMessage());
                    }
                });
    }

    /**
     * Realiza login do usuário
     * Verifica se o email existe e se a senha está correta
     * @param context Contexto da aplicação
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param callback Callback para retorno do resultado
     */
    public static void login(Context context, String email, String senha, Callback callback) {
        FirebaseFirestore db = FirebaseConfig.getFirestore();

        Log.d(TAG, "Iniciando login do usuário: " + email);

        // Busca o usuário pelo email
        db.collection(COLLECTION_USUARIOS)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.w(TAG, "Email não encontrado: " + email);
                            callback.onError("Email não encontrado");
                            return;
                        }

                        // Pega o primeiro documento encontrado
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String senhaArmazenada = document.getString("senha");

                        // Verifica a senha
                        if (senhaArmazenada != null && Validador.verificarSenha(senha, senhaArmazenada)) {
                            Log.d(TAG, "Login realizado com sucesso para: " + email);

                            // Prepara os dados do usuário para retorno
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("id", document.getId());
                            userData.put("nome", document.getString("nome"));
                            userData.put("email", document.getString("email"));
                            userData.put("telefone", document.getString("telefone"));
                            userData.put("dataCadastro", document.getTimestamp("dataCadastro"));

                            callback.onSuccess(userData);
                        } else {
                            Log.w(TAG, "Senha incorreta para: " + email);
                            callback.onError("Senha incorreta");
                        }
                    } else {
                        Log.e(TAG, "Erro ao fazer login", task.getException());
                        callback.onError("Erro ao fazer login: " + task.getException().getMessage());
                    }
                });
    }

    /**
     * Busca todos os usuários cadastrados (para uso administrativo)
     * @param callback Callback para retorno dos resultados
     */
    public static void listarUsuarios(Callback callback) {
        FirebaseFirestore db = FirebaseConfig.getFirestore();

        Log.d(TAG, "Buscando todos os usuários");

        db.collection(COLLECTION_USUARIOS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> usuarios = new HashMap<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            usuarios.put(document.getId(), document.getData());
                        }
                        Log.d(TAG, "Usuários encontrados: " + usuarios.size());
                        callback.onSuccess(usuarios);
                    } else {
                        Log.e(TAG, "Erro ao buscar usuários", task.getException());
                        callback.onError("Erro ao buscar usuários: " + task.getException().getMessage());
                    }
                });
    }

    /**
     * Método auxiliar para verificar se o Firebase está configurado
     * @return true se estiver configurado, false caso contrário
     */
    public static boolean isFirebaseConfigured() {
        try {
            FirebaseFirestore db = FirebaseConfig.getFirestore();
            return db != null;
        } catch (Exception e) {
            Log.e(TAG, "Firebase não está configurado", e);
            return false;
        }
    }
}