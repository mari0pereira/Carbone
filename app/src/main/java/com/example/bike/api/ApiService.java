package com.example.bike.api;

import android.util.Log;

import com.example.bike.models.Bicicleta;
import com.example.bike.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * ApiService para comunicação com a API do InfinityFree
 */
public class ApiService {
    private static final String TAG = "ApiService";

    // URL base do servidor InfinityFree -- Não sei se é essa.
    private static final String BASE_URL = "www.carbone.ct.ws/api/";

    // Executor para operações em background
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Método para realizar requisições GET
     *
     * @param endpoint Endpoint da API
     * @param params Parâmetros para a requisição
     * @return String de resposta da API
     */
    private static String performGetRequest(String endpoint, Map<String, String> params) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + endpoint);

        // Adiciona parâmetros à URL se existirem
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!first) {
                    urlBuilder.append("&");
                }
                urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                urlBuilder.append("=");
                urlBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                first = false;
            }
        }

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return response.toString();
        } else {
            Log.e(TAG, "Erro HTTP: " + responseCode);
            connection.disconnect();
            return null;
        }
    }

    /**
     * Método para realizar requisições POST
     *
     * @param endpoint Endpoint da API
     * @param params Parâmetros para a requisição
     * @return String de resposta da API
     */
    private static String performPostRequest(String endpoint, Map<String, String> params) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Prepara os parâmetros para envio
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
        }

        // Envia os dados
        try (OutputStream os = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            writer.write(postData.toString());
            writer.flush();
        }

        // Lê a resposta
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return response.toString();
        } else {
            Log.e(TAG, "Erro HTTP: " + responseCode);
            connection.disconnect();
            return null;
        }
    }

    /**
     * Método para realizar requisições POST com JSON
     *
     * @param endpoint Endpoint da API
     * @param jsonObject Objeto JSON para envio
     * @return String de resposta da API
     */
    private static String performJsonPostRequest(String endpoint, JSONObject jsonObject) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Envia os dados JSON
        try (OutputStream os = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            writer.write(jsonObject.toString());
            writer.flush();
        }

        // Lê a resposta
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return response.toString();
        } else {
            Log.e(TAG, "Erro HTTP: " + responseCode);
            connection.disconnect();
            return null;
        }
    }

    /**
     * Busca todas as bicicletas ou filtra por categoria
     *
     * @param categoria Categoria da bicicleta (ou null para todas)
     * @param callback Callback com a lista de bicicletas
     */
    public static void getBicicletas(String categoria, Consumer<List<Bicicleta>> callback) {
        executor.execute(() -> {
            try {
                Map<String, String> params = null;
                if (categoria != null && !categoria.isEmpty()) {
                    params = new HashMap<>();
                    params.put("categoria", categoria);
                }

                String response = performGetRequest("get_bicicletas.php", params);

                if (response != null) {
                    List<Bicicleta> bicicletas = parseBicicletasJson(response);
                    // Retorna no thread principal
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(bicicletas));
                } else {
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(new ArrayList<>()));
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(new ArrayList<>()));
            }
        });
    }

    /**
     * Busca uma bicicleta pelo ID
     *
     * @param id ID da bicicleta
     * @param callback Callback com a bicicleta encontrada ou null
     */
    public static void getBicicletaById(int id, Consumer<Bicicleta> callback) {
        executor.execute(() -> {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));

                String response = performGetRequest("get_bicicleta_by_id.php", params);

                if (response != null) {
                    Bicicleta bicicleta = parseBicicletaJson(response);
                    // Retorna no thread principal
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(bicicleta));
                } else {
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(null));
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(null));
            }
        });
    }

    /**
     * Cadastra um novo usuário
     *
     * @param usuario Objeto Usuario a ser cadastrado
     * @param callback Callback com true em caso de sucesso e false em caso de erro
     */
    public static void cadastrarUsuario(Usuario usuario, Consumer<Boolean> callback) {
        executor.execute(() -> {
            try {
                // Cria o objeto JSON para envio
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nome", usuario.getNome());
                jsonObject.put("email", usuario.getEmail());
                jsonObject.put("senha", usuario.getSenha());
                jsonObject.put("telefone", usuario.getTelefone());

                String response = performJsonPostRequest("cadastrar_usuario.php", jsonObject);
                Log.d(TAG, "Resposta do servidor: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                        mainHandler.post(() -> callback.accept(success));
                    } catch (JSONException e) {
                        Log.e(TAG, "Erro ao analisar JSON: " + e.getMessage());
                        android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                        mainHandler.post(() -> callback.accept(false));
                    }
                } else {
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(false));
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(false));
            }
        });
    }

    /**
     * Realiza o login do usuário
     *
     * @param email Email do usuário
     * @param senhaHash Hash da senha do usuário
     * @param callback Callback com o usuário encontrado ou null
     */
    public static void login(String email, String senhaHash, Consumer<Usuario> callback) {
        executor.execute(() -> {
            try {
                // Cria o objeto JSON para envio
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("email", email);
                jsonObject.put("senha", senhaHash);

                String response = performJsonPostRequest("login.php", jsonObject);
                Log.d(TAG, "Resposta do servidor: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            JSONObject usuarioJson = jsonResponse.getJSONObject("usuario");
                            int id = usuarioJson.getInt("id");
                            String nome = usuarioJson.getString("nome");
                            String emailUsuario = usuarioJson.getString("email");
                            String telefone = usuarioJson.getString("telefone");

                            Usuario usuario = new Usuario(nome, emailUsuario, "", telefone);
                            usuario.setId(id);

                            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                            mainHandler.post(() -> callback.accept(usuario));
                        } else {
                            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                            mainHandler.post(() -> callback.accept(null));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Erro ao analisar JSON: " + e.getMessage());
                        android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                        mainHandler.post(() -> callback.accept(null));
                    }
                } else {
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(null));
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(null));
            }
        });
    }

    /**
     * Verifica se um email já existe
     *
     * @param email Email a ser verificado
     * @param callback Callback com true se o email existir e false caso contrário
     */
    public static void checkEmailExists(String email, Consumer<Boolean> callback) {
        executor.execute(() -> {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);

                String response = performGetRequest("cadastrar_usuario.php", params);
                Log.d(TAG, "Resposta do servidor: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            boolean exists = jsonResponse.getBoolean("exists");
                            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                            mainHandler.post(() -> callback.accept(exists));
                        } else {
                            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                            mainHandler.post(() -> callback.accept(false));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Erro ao analisar JSON: " + e.getMessage());
                        android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                        mainHandler.post(() -> callback.accept(false));
                    }
                } else {
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(false));
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(false));
            }
        });
    }

    /**
     * Converte JSON para lista de Bicicletas
     */
    private static List<Bicicleta> parseBicicletasJson(String json) {
        List<Bicicleta> bicicletas = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Bicicleta bicicleta = new Bicicleta(
                        obj.getInt("id"),
                        obj.getString("nome"),
                        obj.getString("modelo"),
                        obj.getDouble("preco"),
                        obj.getString("cor"),
                        obj.getString("tamanho"),
                        obj.getString("descricao"),
                        obj.getString("especificacoes"),
                        obj.getString("categoria"),
                        BASE_URL + obj.getString("image_url")  // Adiciona o BASE_URL à imagem
                );
                bicicletas.add(bicicleta);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao analisar JSON: " + e.getMessage());
        }
        return bicicletas;
    }

    /**
     * Converte JSON para um único objeto Bicicleta
     */
    private static Bicicleta parseBicicletaJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return new Bicicleta(
                    obj.getInt("id"),
                    obj.getString("nome"),
                    obj.getString("modelo"),
                    obj.getDouble("preco"),
                    obj.getString("cor"),
                    obj.getString("tamanho"),
                    obj.getString("descricao"),
                    obj.getString("especificacoes"),
                    obj.getString("categoria"),
                    BASE_URL + obj.getString("image_url")  // Adiciona o BASE_URL à imagem
            );
        } catch (JSONException e) {
            Log.e(TAG, "Erro ao analisar JSON: " + e.getMessage());
            return null;
        }
    }
}