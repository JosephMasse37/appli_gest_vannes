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

import com.btssio.appli_gest_vannes.passerelle.CommuneDAO;
import com.btssio.appli_gest_vannes.passerelle.SecteurDAO;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibSecteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListeSecteurs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_liste_secteurs);

        LibCommune laCommune = (LibCommune) this.getIntent().getExtras().getSerializable("commune");
        List<LibSecteur> listeSecteurs = SecteurDAO.getArraySecteur(laCommune.getNumCom(), ListeSecteurs.this);

        ListView listeViewSecteurs = this.findViewById(R.id.listeSecteurs);
        TextView txtSecteur = this.findViewById(R.id.txtListeSecteurs);

        txtSecteur.setText("Liste des secteurs de la commune de " + laCommune.getNomCom());

        List<HashMap<String, String>> listeValeursMap = new ArrayList<>();
        HashMap<String, String> element;
        for (int i = 0; i < listeSecteurs.size(); i++) {
            element = new HashMap<>();

            element.put("nomSecteur", listeSecteurs.get(i).getNomSecteur());

            listeValeursMap.add(element);
        }
        ListAdapter listeAdapter = new SimpleAdapter(this, listeValeursMap, R.layout.layout_secteurs, new String[]{"nomSecteur"}, new int[]{R.id.txtSecteur});
        listeViewSecteurs.setAdapter(listeAdapter);

        listeViewSecteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                LibSecteur s = listeSecteurs.get(i);

                Intent intentSuite = new Intent(ListeSecteurs.this, ListeVannes.class);
                intentSuite.putExtra("secteur", s);

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