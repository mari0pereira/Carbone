package com.example.bike.api;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConfig {
    private static FirebaseFirestore firestore;

    public static void inicializarFirebase(Context context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context);
            }
            firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}