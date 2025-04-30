package com.btssio.appli_gest_vannes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.btssio.appli_gest_vannes.passerelle.CommuneDAO;
import com.btssio.appli_gest_vannes.passerelle.ReleveDAO;
import com.btssio.appli_gest_vannes.testBD.TestBDCommune;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.btssio.appli_gest_vannes.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_releves) {
            Intent intent = new Intent(MainActivity.this, ListeCommunes.class);
            startActivity(intent);
        }

        if (id == R.id.menu_import) {
            AlertDialog.Builder dialogBox = new AlertDialog.Builder(MainActivity.this);

            dialogBox.setTitle("Importer les données");
            dialogBox.setMessage("Veuillez choisir la méthode d'importation. \n\nEn cas de début de " +
                    "période, les données seront totalement mises à jour, toutes données " +
                    "non-exportées seront perdues. \n\nEn cas de cours de période, seules les données " +
                    "des relevés seront importées, toutes données non-exportées seront perdues.");

            dialogBox.setPositiveButton("Début de période", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this,"Début de période confirmée",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialogBoxDebPeriode = new AlertDialog.Builder(MainActivity.this);

                    dialogBoxDebPeriode.setTitle("Importer les données - Début de période");
                    dialogBoxDebPeriode.setMessage("Veuillez choisir la méthode d'importation." +
                            "\n\n - Toutes les données\n\n - Uniquement les relevés");

                    dialogBoxDebPeriode.setPositiveButton("Toutes les données", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this,"All Data confirmed",Toast.LENGTH_SHORT).show();
                            CommuneDAO.deleteCommunes(MainActivity.this);

                        }
                    });

                    dialogBoxDebPeriode.setNegativeButton("Uniquement les relevés", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ReleveDAO.deleteReleves(MainActivity.this);


                            Toast.makeText(MainActivity.this,"Table relevé confirmée",Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialogBoxDebPeriode.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    dialogBoxDebPeriode.show();
                }
            });

            dialogBox.setNegativeButton("En cours de période", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this,"En cours de période confirmée",Toast.LENGTH_SHORT).show();
                }
            });

            dialogBox.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            dialogBox.show();
        }

        return super.onOptionsItemSelected(item);
    }
}