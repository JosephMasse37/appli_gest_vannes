package com.btssio.appli_gest_vannes.services;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServices {
    public static String chargeDonnees(String chaineUrl) {
        final String[] lineJSON = new String[1];
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Callable<String> callbackTask = () -> {
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(chaineUrl);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = url.openConnection().getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        lineJSON[0] = reader.readLine();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return lineJSON[0];
            };
            List<Callable<String>> callableTasks = new ArrayList<>();
            callableTasks.add(callbackTask);
            lineJSON[0] = executor.invokeAny(callableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineJSON[0];
    }

    public static String enregistrerDonnees(String chaineUrl, String donneesJson) {
        final String[] resultat = new String[1];
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Callable<String> callbackTask = () -> {
                URL url;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(chaineUrl);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    writer.write(donneesJson);
                    writer.flush();

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                        resultat[0] = "Enregistrement réussi";
                    } else {
                        resultat[0] = "Erreur Enregistrement : " + httpURLConnection.getResponseMessage();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return resultat[0];
            };
            List<Callable<String>> callableTasks = new ArrayList<>();
            callableTasks.add(callbackTask);
            resultat[0] = executor.invokeAny(callableTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat[0];
    }
}
