package com.example.bike.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bike.R;
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

            // Popula o banco com dados iniciais
            Thread thread = new Thread(() -> {
                BicicletaDAO dao = INSTANCE.bicicletaDAO();

                // Verifica se já existem bicicletas
                if (dao.getAll().isEmpty()) {
                    // Adiciona a Scalpel 3
                    Bicicleta scalpel = new Bicicleta(
                            "Scalpel",            // nome
                            3,                      // modelo
                            35990.00,               // preco
                            R.drawable.scalpel_3,   // imageUrl
                            "Tiger Shark",          // cor
                            "MD",                   // tamanho padrão
                            "A 120mm cross-country ripper with Shimano XT/Deore 12-speed, a RockShox SID Select fork, and DownLow dropper post", // descricao
                            "Quadro em carbono com suspensão de 120mm, grupo Shimano XT/Deore 12v, garfo RockShox SID Select e canote retrátil DownLow", // especificacoes
                            "Mountain Bike"         // categoria
                    );
                    dao.insert(scalpel);

                    // Adiciona a Exalt LT Factory
                    Bicicleta exalt = new Bicicleta(
                            "Exalt LT Factory",     // nome
                            2024,                   // modelo (ano)
                            53990.00,               // preco
                            R.drawable.exalt_lt,    // imageUrl
                            "Verde",                // cor
                            "MD",                   // tamanho padrão
                            "Exalt LT Factory entrega performance de competição na medida certa. Com quadro em fibra de carbono Mitsubishi M40/M60 que alia rigidez e conforto, sistema de link anti squat que elimina o efeito bump nas pedaladas e cinemática horst link que deixa o freio traseiro totalmente independente da suspensão.", // descricao
                            "Quadro Sense Carbon Mitsubishi M40 | Suspensão Fox Float Factory 36 160mm | Amortecedor Fox DHX2 140mm | Freios Shimano XTR M9120 4 pistões", // especificacoes
                            "Mountain Bike"         // categoria
                    );
                    dao.insert(exalt);

                    // Adicionar Grizl CF SLX 8 Di2
                    Bicicleta grizl = new Bicicleta(
                            "Grizl CF SLX 8 Di2",   // nome
                            8,                      // modelo
                            21425.40,               // preco
                            R.drawable.grizl,       // imageUrl
                            "Milk Tea",             // cor
                            "MD",                   // tamanho padrão
                            "Cobre grandes distâncias com uma velocidade inigualável. Esta bicicleta combina o quadro Grizl CF SLX superleve e pronto para a aventura com as novas e brilhantes mudanças sem fios GRX Di2 da Shimano e as mais recentes rodas aerodinâmicas gravel de carbono da DT Swiss.", // descricao
                            "Quadro leve de carbono (950g) | Mudanças sem fio Shimano GRX Di2 | Rodas DT Swiss GRC1400 | Espaço para pneus até 50mm | Suportes de forqueta para equipamento adicional", // especificacoes
                            "Gravel"               // categoria
                    );
                    dao.insert(grizl);
                }
            });
            thread.start();
        }
        return INSTANCE;
    }
}
