package com.example.bike.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bike.model.Bicicleta;
import com.example.bike.model.Usuario;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Service para integração com Supabase usando Retrofit
 * Essa classe gerencia a comunicação com a API REST do Supabase e a autenticação
 */
public class SupabaseRetrofitService {
    private static final String TAG = "SupabaseRetrofitService";
    private static final String SUPABASE_URL = "https://mfpqabvrqtynazlsphkm.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1mcHFhYnZycXR5bmF6bHNwaGttIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY3MTU4NzYsImV4cCI6MjA2MjI5MTg3Nn0.mmQLS9qMo5bfJasYdnteEthfO24tA1icnC8ESD3yK4g";

    private static final String STORAGE_URL = SUPABASE_URL + "/storage/v1/object";
    private static final String STORAGE_BUCKET = "avatars";

    private static Retrofit retrofit;
    private static SupabaseApiService supabaseApiService;
    private static SupabaseAuthService supabaseAuthService;

    public interface StringCallback {
        void onResult(String result);
    }

    // Interfaces para callbacks
    public interface BooleanCallback {
        void onResult(boolean result);
    }

    public interface UsuarioCallback {
        void onResult(Usuario usuario);
    }

    public interface BicicletasCallback {
        void onResult(List<Bicicleta> bicicletas);
    }

    /**
     * Inicializa os serviços Retrofit para Supabase
     * @param context Contexto da aplicação
     */
    public static void init(Context context) {
        // Configuração do OkHttpClient com headers apropriados para Supabase
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Adiciona headers padrão para todas as requisições
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + SUPABASE_KEY)
                                .header("apikey", SUPABASE_KEY)
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Configuração do Retrofit para API do Supabase
        retrofit = new Retrofit.Builder()
                .baseUrl(SUPABASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criação dos serviços
        supabaseApiService = retrofit.create(SupabaseApiService.class);
        supabaseAuthService = retrofit.create(SupabaseAuthService.class);
    }

    // ===================== AUTENTICAÇÃO =====================

    /**
     * Verifica se um email já existe no banco.
     * @param email Email a ser verificado
     * @param callback Callback para o resultado
     */
    public static void checkEmailExists(String email, BooleanCallback callback) {
        // Busca usuários com este email diretamente
        Map<String, String> query = new HashMap<>();
        query.put("email", "eq." + email);
        query.put("select", "id");

        supabaseApiService.getUsers(query).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, retrofit2.Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Se retornou algum usuário, email já existe
                    boolean exists = !response.body().isEmpty();
                    callback.onResult(exists);
                } else {
                    Log.e(TAG, "Erro ao verificar email: " + response.message());
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e(TAG, "Falha ao verificar email", t);
                callback.onResult(false);
            }
        });
    }

    /**
     * Cadastra um novo usuário.
     * @param usuario Dados do usuário
     * @param callback Callback para sucesso/erro
     */
    public static void cadastrarUsuario(Usuario usuario, BooleanCallback callback) {
        Map<String, Object> authData = new HashMap<>();
        authData.put("email", usuario.getEmail());
        authData.put("password", usuario.getSenha());

        Map<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("nome", usuario.getNome());
        userMetadata.put("telefone", usuario.getTelefone());
        userMetadata.put("dt_nascimento", usuario.getDt_nascimento().toString());
        userMetadata.put("tipo_usuario", usuario.getTipoUsuario());

        authData.put("data", new HashMap<String, Object>() {{
            put("user_metadata", userMetadata);
        }});

        Log.d(TAG, "Iniciando cadastro para email: " + usuario.getEmail());

        supabaseAuthService.signUp(authData).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Autenticação criada com sucesso");

                    try {
                        // Extrai o auth_uid do objeto de resposta
                        Map<String, Object> user = (Map<String, Object>) response.body().get("user");
                        if (user != null && user.containsKey("id")) {
                            String authUid = (String) user.get("id");
                            Log.d(TAG, "Auth UID obtido: " + authUid);

                            // Salva os dados do usuário na tabela usuarios
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("auth_uid", authUid);
                            userData.put("nome", usuario.getNome());
                            userData.put("email", usuario.getEmail());
                            userData.put("telefone", usuario.getTelefone());
                            userData.put("dt_nascimento", usuario.getDt_nascimento().toString());
                            userData.put("tipo_usuario", usuario.getTipoUsuario());

                            supabaseApiService.insertUser(userData).enqueue(new Callback<List<Map<String, Object>>>() {
                                @Override
                                public void onResponse(Call<List<Map<String, Object>>> call, retrofit2.Response<List<Map<String, Object>>> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "Usuário inserido na tabela com sucesso");
                                        callback.onResult(true);
                                    } else {
                                        Log.e(TAG, "Erro ao inserir usuário na tabela: " + response.message());
                                        if (response.errorBody() != null) {
                                            try {
                                                Log.e(TAG, "Detalhes do erro: " + response.errorBody().string());
                                            } catch (IOException e) {
                                                Log.e(TAG, "Erro ao ler errorBody", e);
                                            }
                                        }
                                        callback.onResult(false);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                                    Log.e(TAG, "Falha ao inserir usuário na tabela", t);
                                    callback.onResult(false);
                                }
                            });
                        } else {
                            Log.e(TAG, "Auth UID não encontrado na resposta");
                            callback.onResult(false);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar resposta de autenticação", e);
                        callback.onResult(false);
                    }
                } else {
                    Log.e(TAG, "Erro na autenticação: " + response.code() + " - " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Detalhes do erro de auth: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "Erro ao ler errorBody de auth", e);
                        }
                    }
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Falha na autenticação", t);
                callback.onResult(false);
            }
        });
    }

    /**
     * Realiza login do usuário.
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param callback Callback que retorna o usuário (ou null se falhou)
     */
    public static void login(String email, String senha, UsuarioCallback callback) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", senha);

        Log.d(TAG, "Tentando fazer login para: " + email);

        supabaseAuthService.signIn(loginData).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, retrofit2.Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Login de autenticação bem-sucedido");

                    try {
                        // Extrai dados da resposta
                        Map<String, Object> responseBody = response.body();
                        Map<String, Object> user = (Map<String, Object>) responseBody.get("user");
                        String authToken = (String) responseBody.get("access_token");

                        if (user == null || authToken == null) {
                            Log.e(TAG, "Resposta de login incompleta");
                            callback.onResult(null);
                            return;
                        }

                        String authUid = (String) user.get("id");
                        if (authUid == null) {
                            Log.e(TAG, "Auth UID não encontrado");
                            callback.onResult(null);
                            return;
                        }

                        // Busca dados completos do usuário
                        Map<String, String> query = new HashMap<>();
                        query.put("auth_uid", "eq." + authUid);

                        // Usa o token para buscar na tabela usuarios
                        supabaseApiService.getUserByAuthUid("Bearer " + authToken, query).enqueue(new Callback<List<Usuario>>() {
                            @Override
                            public void onResponse(Call<List<Usuario>> call, retrofit2.Response<List<Usuario>> response) {
                                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                                    Usuario usuario = response.body().get(0);
                                    usuario.setAuthUid(authUid);
                                    callback.onResult(usuario);
                                } else {
                                    Log.e(TAG, "Erro ao buscar dados do usuário: " + response.message());
                                    callback.onResult(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                                Log.e(TAG, "Falha ao buscar dados do usuário", t);
                                callback.onResult(null);
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, "Erro ao processar login", e);
                        callback.onResult(null);
                    }
                } else {
                    Log.e(TAG, "Erro no login: " + response.code());
                    callback.onResult(null);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Falha na requisição de login", t);
                callback.onResult(null);
            }
        });
    }

    public static void uploadFotoPerfil(Context context, String userId, Uri fileUri, String fileName, StringCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("apikey", SUPABASE_KEY)
                                .header("Authorization", "Bearer " + SUPABASE_KEY)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            RequestBody requestBody = RequestBody.create(bytes, MediaType.parse("image/*"));

            Request request = new Request.Builder()
                    .url(STORAGE_URL + "/" + STORAGE_BUCKET + "/" + userId + "/" + fileName)
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    Log.e(TAG, "Falha no upload da foto", e);
                    callback.onResult(null);
                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String publicUrl = STORAGE_URL.replace("/object", "/object/public") +
                                "/" + STORAGE_BUCKET + "/" + userId + "/" + fileName;
                        callback.onResult(publicUrl);
                    } else {
                        Log.e(TAG, "Erro no upload: " + response.body().string());
                        callback.onResult(null);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao processar imagem", e);
            callback.onResult(null);
        }
    }

    /**
     * Busca bicicletas do banco, opcionalmente filtrando por categoria.
     * @param categoria Categoria para filtrar (opcional)
     * @param callback Callback que retorna a lista de bicicletas
     */
    public static void getBicicletas(String categoria, BicicletasCallback callback) {
        Map<String, String> query = new HashMap<>();
        if (categoria != null && !categoria.isEmpty()) {
            query.put("categoria", "eq." + categoria);
        }

        supabaseApiService.getBicicletas(query).enqueue(new Callback<List<Bicicleta>>() {
            @Override
            public void onResponse(Call<List<Bicicleta>> call, retrofit2.Response<List<Bicicleta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    Log.e(TAG, "Erro ao buscar bicicletas: " + response.message());
                    callback.onResult(null);
                }
            }

            @Override
            public void onFailure(Call<List<Bicicleta>> call, Throwable t) {
                Log.e(TAG, "Falha ao buscar bicicletas", t);
                callback.onResult(null);
            }
        });
    }

    // Interfaces Retrofit para Supabase

    /**
     * Interface para operações de banco de dados com Supabase REST API.
     */
    interface SupabaseApiService {
        @GET("rest/v1/usuarios")
        Call<List<Usuario>> getUsers(@QueryMap Map<String, String> queryMap);

        @POST("rest/v1/usuarios")
        @Headers("Prefer: return=representation")
        Call<List<Map<String, Object>>> insertUser(@Body Map<String, Object> user);

        @GET("rest/v1/usuarios")
        Call<List<Usuario>> getUserByAuthUid(@Header("Authorization") String authToken, @QueryMap Map<String, String> queryMap);

        @GET("rest/v1/bicicletas")
        Call<List<Bicicleta>> getBicicletas(@QueryMap Map<String, String> queryMap);
    }

    /**
     * Interface para operações de autenticação com Supabase Auth API.
     */
    interface SupabaseAuthService {
        @Headers({"Content-Type: application/json"})

        @POST("auth/v1/signup")
        Call<Map<String, Object>> signUp(@Body Map<String, Object> signUpData);

        @Headers({
                "Content-Type: application/json"
        })
        @POST("auth/v1/token?grant_type=password")
        Call<Map<String, Object>> signIn(@Body Map<String, String> signInData);
    }
}