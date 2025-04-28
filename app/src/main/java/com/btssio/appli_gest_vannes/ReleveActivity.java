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
import com.btssio.appli_gest_vannes.passerelle.ReleveDAO;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibReleve;
import com.sio.libseg.metier.LibSecteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReleveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_releve);

        LibCompteurVanne laVanne = (LibCompteurVanne) this.getIntent().getExtras().getSerializable("releve");
        List<LibReleve> listeReleves = ReleveDAO.getArrayReleve(laVanne, ReleveActivity.this);

        ListView listeViewReleves = this.findViewById(R.id.listeReleves);
        TextView txtReleve = this.findViewById(R.id.txtListeReleves);

        txtReleve.setText("Référence Vanne :  " + laVanne.getRefCompteur());

        List<HashMap<String, String>> listeValeursMap = new ArrayList<>();
        HashMap<String, String> element;
        for (int i = 0; i < listeReleves.size(); i++) {
            element = new HashMap<>();

            element.put("annee", String.valueOf(listeReleves.get(i).getDateReleve().getYear()));
            element.put("index", "Index du relevé : " + listeReleves.get(i).getIndexReleve());

            listeValeursMap.add(element);
        }
        ListAdapter listeAdapter = new SimpleAdapter(this, listeValeursMap, R.layout.layout_releves, new String[]{"annee", "index", },
                new int[]{R.id.txtReleveAnnee, R.id.txtReleveIndex});
        listeViewReleves.setAdapter(listeAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}