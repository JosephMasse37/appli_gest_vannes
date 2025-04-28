package com.btssio.appli_gest_vannes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.btssio.appli_gest_vannes.classestechniques.ConversionDate;
import com.btssio.appli_gest_vannes.passerelle.CompteurVanneDAO;
import com.btssio.appli_gest_vannes.passerelle.SecteurDAO;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibSecteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListeVannes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_vannes);

        LibSecteur leSecteur = (LibSecteur) this.getIntent().getExtras().getSerializable("secteur");
        List<LibCompteurVanne> listeVannes = CompteurVanneDAO.getArrayCompteurVanne(leSecteur.getNumSecteur(), ListeVannes.this);

        ListView listeViewVannes = this.findViewById(R.id.listeVannes);
        TextView txtVannes = this.findViewById(R.id.txtListeVannes);

        txtVannes.setText("Liste des Vannes du secteur de " + leSecteur.getNomSecteur());

        List<HashMap<String, String>> listeValeursMap = new ArrayList<>();
        HashMap<String, String> element;
        for (int i = 0; i < listeVannes.size(); i++) {
            element = new HashMap<>();

            element.put("nomVanne", listeVannes.get(i).getRefCompteur());
            element.put("marque", "Marque : " + listeVannes.get(i).getMarque());
            element.put("dateInstallation", "Date d'Installation : " + ConversionDate.dateToString(listeVannes.get(i).getDateInstallation(), "dd/MM/yyyy"));

            listeValeursMap.add(element);
        }
        ListAdapter listeAdapter = new SimpleAdapter(this, listeValeursMap, R.layout.layout_vannes, new String[]{"nomVanne", "marque", "dateInstallation"},
                new int[]{R.id.txtVanne, R.id.txtMarque, R.id.txtDateInstallation});
        listeViewVannes.setAdapter(listeAdapter);

        listeViewVannes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                LibCompteurVanne r = listeVannes.get(i);

                Intent intentSuite = new Intent(ListeVannes.this, ReleveActivity.class);
                intentSuite.putExtra("releve", r);

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