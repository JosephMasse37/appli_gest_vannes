package com.btssio.appli_gest_vannes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.btssio.appli_gest_vannes.classestechniques.LocalDateAdapter;
import com.btssio.appli_gest_vannes.passerelle.CommuneDAO;
import com.btssio.appli_gest_vannes.passerelle.ReleveDAO;
import com.btssio.appli_gest_vannes.services.WebServices;
import com.btssio.appli_gest_vannes.testBD.TestBDCommune;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.btssio.appli_gest_vannes.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibReleve;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            dialogBox.setMessage("Veuillez choisir la méthode d'importation : \n\n - Lors du " +
                    "début de période, les données seront totalement mises à jour. \nToutes " +
                    "données non-exportées seront perdues. \n\n - Lorsque la période est en " +
                    "cours, seules les données des relevés seront importées sans supprimé celles " +
                    "de l'année en cours. \nToutes données non-exportées seront perdues.");

            dialogBox.setPositiveButton("Début de période", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialog.Builder dialogBoxDebPeriode = new AlertDialog.Builder(MainActivity.this);

                    dialogBoxDebPeriode.setTitle("Importer les données - Début de période");
                    dialogBoxDebPeriode.setMessage("Veuillez choisir la méthode d'importation." +
                            "\n\n - Toutes les données\n\n - Uniquement les relevés");

                    dialogBoxDebPeriode.setPositiveButton("Toutes les données", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                CommuneDAO.deleteCommunes(MainActivity.this);

                                Gson gson = new GsonBuilder()
                                        .setPrettyPrinting()
                                        .disableHtmlEscaping()
                                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                                        .create();

                                // Données des communes, secteurs et vannes
                                String dataCommunes = WebServices.chargeDonnees("http://172.16.30.132:8080/wsgestionvannes_joseph/resources/communes");

                                List<LibCommune> listeCommunes;
                                Type listeType = new TypeToken<ArrayList<LibCommune>>() {}.getType();

                                listeCommunes = gson.fromJson(dataCommunes, listeType);

                                for (LibCommune c : listeCommunes) {
                                    CommuneDAO.addCommune(c, MainActivity.this);
                                }

                                // Données des relevés
                                String dataReleves = WebServices.chargeDonnees("http://172.16.30.132:8080/wsgestionvannes_joseph/resources/releves");

                                List<LibReleve> listeReleves;
                                Type listeTypeR = new TypeToken<ArrayList<LibReleve>>() {}.getType();

                                listeReleves = gson.fromJson(dataReleves, listeTypeR);

                                for (LibReleve r : listeReleves) {
                                    ReleveDAO.addReleve(r, 1, MainActivity.this);
                                }

                                Toast.makeText(MainActivity.this,"Données importées.",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this,"Une erreur est survenue lors de l'import..." + e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });

                    dialogBoxDebPeriode.setNegativeButton("Uniquement les relevés", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                ReleveDAO.deleteReleves(MainActivity.this);

                                Gson gson = new GsonBuilder().setPrettyPrinting()
                                        .disableHtmlEscaping()
                                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                                        .create();

                                // Appel Relevés
                                String dataReleves = WebServices.chargeDonnees("http://172.16.30.132:8080/wsgestionvannes_joseph/resources/releves");

                                List<LibReleve> listeReleves;
                                Type listeType = new TypeToken<ArrayList<LibReleve>>() {}.getType();

                                listeReleves = gson.fromJson(dataReleves, listeType);

                                for (LibReleve r : listeReleves) {
                                    ReleveDAO.addReleve(r, 1, MainActivity.this);
                                }

                                Toast.makeText(MainActivity.this,"Données importées.",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this,"Une erreur est survenue lors de l'import..." + e.getMessage(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
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
                    try {
                        ReleveDAO.deleteRelevesNotInCurrentYear(MainActivity.this);

                        Gson gson = new GsonBuilder().setPrettyPrinting()
                                .disableHtmlEscaping()
                                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                                .create();

                        // Appel Relevés
                        String dataReleves = WebServices.chargeDonnees("http://172.16.30.132:8080/wsgestionvannes_joseph/resources/releves");

                        List<LibReleve> listeReleves;
                        Type listeType = new TypeToken<ArrayList<LibReleve>>() {}.getType();

                        listeReleves = gson.fromJson(dataReleves, listeType);

                        for (LibReleve r : listeReleves) {
                            ReleveDAO.addReleve(r, 0, MainActivity.this);
                        }

                        Toast.makeText(MainActivity.this,"Importation réussie.",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,"Une erreur est survenue...",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            dialogBox.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            dialogBox.show();
        }

        if (id == R.id.menu_export) {
            AlertDialog.Builder dialogBoxExport = new AlertDialog.Builder(MainActivity.this);

            dialogBoxExport.setTitle("Exporter les données");
            dialogBoxExport.setMessage("Vous confirmez vouloir exporter vos relevés ?");

            dialogBoxExport.setPositiveButton("Exporter les données", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .disableHtmlEscaping()
                                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                                .create();

                        String data = gson.toJson(ReleveDAO.getRelevesWS(MainActivity.this));

                        String resultat = WebServices.enregistrerDonnees("http://172.16.30.132:8080/wsgestionvannes_joseph/resources/releves", data);

                        Log.i("test", resultat);

                        Toast.makeText(MainActivity.this,"Données exportées.",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,"Une erreur est survenue lors de l'export..." + e.getMessage(),Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            dialogBoxExport.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            dialogBoxExport.show();
        }

        return super.onOptionsItemSelected(item);
    }
}