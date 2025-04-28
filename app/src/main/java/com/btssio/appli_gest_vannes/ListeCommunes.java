package com.btssio.appli_gest_vannes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.btssio.appli_gest_vannes.passerelle.CommuneDAO;
import com.sio.libseg.metier.LibCommune;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListeCommunes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_communes);

        List<LibCommune> listeCommunes = CommuneDAO.getArrayCommunes(ListeCommunes.this);

        ListView listeViewCommunes = this.findViewById(R.id.listeCommunes);

        List<HashMap<String, String>> listeValeursMap = new ArrayList<>();
        HashMap<String, String> element;
        for (int i = 0; i < listeCommunes.size(); i++) {
            element = new HashMap<>();

            element.put("nomCommune", listeCommunes.get(i).getNomCom());

            listeValeursMap.add(element);
        }
        ListAdapter listeAdapter = new SimpleAdapter(this, listeValeursMap, R.layout.layout_communes, new String[]{"nomCommune"}, new int[]{R.id.txtCommune});
        listeViewCommunes.setAdapter(listeAdapter);

        listeViewCommunes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                LibCommune c = listeCommunes.get(i);

                Intent intentSuite = new Intent(ListeCommunes.this, ListeSecteurs.class);
                intentSuite.putExtra("commune", c);

                startActivity(intentSuite);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}