package com.example.bike.api;

import android.util.Log;

import com.example.bike.models.Bicicleta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ApiService {
    private static final String TAG = "BicicletaApiService";

    // Base URL do seu servidor - substitua pelo URL base do InfinityFree
    private static final String BASE_URL = "https://seu-site.infinityfree.net/";

    // Executor para operações em background
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Busca todas as bicicletas do servidor
     * @param categoria Categoria de bicicleta ou null para todas
     * @param callback Callback com a lista de bicicletas
     */
    public static void getBicicletas(String categoria, Consumer<List<Bicicleta>> callback) {
        executor.execute(() -> {
            try {
                String urlStr = BASE_URL + "get_bicicletas.php";

                // Adiciona parâmetro de categoria se especificado
                if (categoria != null && !categoria.isEmpty()) {
                    urlStr += "?categoria=" + categoria;
                }

                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    List<Bicicleta> bicicletas = parseBicicletasJson(response.toString());
                    // Retorna no thread principal
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(bicicletas));
                } else {
                    Log.e(TAG, "Erro HTTP: " + responseCode);
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(new ArrayList<>()));
                }

                connection.disconnect();
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(new ArrayList<>()));
            }
        });
    }

    /**
     * Busca uma bicicleta específica pelo ID
     * @param id ID da bicicleta
     * @param callback Callback com a bicicleta ou null
     */
    public static void getBicicletaById(int id, Consumer<Bicicleta> callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(BASE_URL + "get_bicicleta_by_id.php?id=" + id);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Bicicleta bicicleta = parseBicicletaJson(response.toString());
                    // Retorna no thread principal
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(bicicleta));
                } else {
                    Log.e(TAG, "Erro HTTP: " + responseCode);
                    android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                    mainHandler.post(() -> callback.accept(null));
                }

                connection.disconnect();
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar com a API: " + e.getMessage());
                android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
                mainHandler.post(() -> callback.accept(null));
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
}
