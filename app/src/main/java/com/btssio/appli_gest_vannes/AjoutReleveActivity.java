package com.btssio.appli_gest_vannes;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.btssio.appli_gest_vannes.classestechniques.ConversionDate;
import com.btssio.appli_gest_vannes.passerelle.ReleveDAO;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibReleve;

public class AjoutReleveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajout_releve);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtRefVanne = this.findViewById(R.id.txtRefVanne);
        TextView txtMarqueVanne = this.findViewById(R.id.txtMarqueVanne);
        TextView txtOldIndex = this.findViewById(R.id.txtOldIndex);
        TextView txtDateReleve = this.findViewById(R.id.txtDateReleve);
        TextView txtConso = this.findViewById(R.id.txtConso);

        EditText txtNewIndex = this.findViewById(R.id.txtNewIndex);
        Button btnAddReleve = this.findViewById(R.id.btnAdd);

        Bundle intentAddReleveExtras = this.getIntent().getExtras();

        int oldIndex = intentAddReleveExtras.getInt("indexReleve");
        String date = intentAddReleveExtras.getString("date");
        LibCompteurVanne laVanne = (LibCompteurVanne) intentAddReleveExtras.getSerializable("vanne");

        txtRefVanne.setText("Référence Vanne : " + laVanne.getRefCompteur());
        txtMarqueVanne.setText("Marque Vanne : " + laVanne.getMarque());
        txtOldIndex.setText("Ancien Index : " + oldIndex + " m³");
        txtDateReleve.setText("Date : " + date);


        txtNewIndex.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    int newIndex = Integer.parseInt(txtNewIndex.getText().toString());
                    if (newIndex < oldIndex) {
                        txtConso.setText("Nouvel index trop petit.");
                    } else {
                        int conso = newIndex - oldIndex;
                        txtConso.setText("Consommation : " + conso + " m³");
                    }
                } catch (Exception e) {
                    txtConso.setText("");
                }
                return false;
            }
        });

        btnAddReleve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int newIndex = Integer.parseInt(txtNewIndex.getText().toString());

                    if (newIndex < oldIndex) {
                        Toast.makeText(AjoutReleveActivity.this, "Erreur : Nouvel index trop petit.", Toast.LENGTH_SHORT).show();
                    } else {
                        LibReleve leReleve = new LibReleve(ConversionDate.stringToDate(date, "dd/MM/yyyy"), newIndex, laVanne);
                        ReleveDAO.addReleve(leReleve, 0, AjoutReleveActivity.this);

                        Toast.makeText(AjoutReleveActivity.this, "Ajout du relevé effectué !", Toast.LENGTH_SHORT).show();

                        Intent intentCommunes = new Intent(AjoutReleveActivity.this, ListeCommunes.class);
                        startActivity(intentCommunes);
                    }
                } catch (Exception e) {
                    Toast.makeText(AjoutReleveActivity.this, "Erreur : Nouvel index nul.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}