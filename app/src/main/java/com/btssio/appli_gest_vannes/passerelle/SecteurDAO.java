package com.btssio.appli_gest_vannes.passerelle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibSecteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Massé on 14/01/2025.
 */
public class SecteurDAO {
    public static final String SECTEUR_KEY = "id";
    public static final String SECTEUR_NOM = "libelle";
    public static final String SECTEUR_COMMUNE = "idCommune";

    /**
     * récupère tous les secteurs pour une commune
     *
     * @param idCommune identifiant de la commune
     * @param ct Le contexte
     * @return List d'objets Secteur
     */
    public static List<LibSecteur> getArraySecteur(int idCommune, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        List<LibSecteur> listeSecteurs = new ArrayList<>();
        Cursor curseurSecteur;
        // l'accès à la base sera en lecture
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        String req = "select id, libelle from secteur where idCommune =" + idCommune + " order by libelle";
        curseurSecteur = bd.rawQuery(req, null);

        curseurSecteur.moveToFirst();
        while (!curseurSecteur.isAfterLast()) {
            LibSecteur secteur = new LibSecteur(curseurSecteur.getInt(0), curseurSecteur.getString(1));
            secteur.setListeCompteursVanne(CompteurVanneDAO.getArrayCompteurVanne(secteur.getNumSecteur(),ct));
            listeSecteurs.add(secteur);
            curseurSecteur.moveToNext();
        }

        return listeSecteurs;
    }

    public static long addSecteur(LibSecteur s, int idCommune, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;
        SQLiteDatabase bd = accesBD.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(SECTEUR_KEY, s.getNumSecteur());
        value.put(SECTEUR_NOM, s.getNomSecteur());
        value.put(SECTEUR_COMMUNE, idCommune);
        retour = bd.insert("secteur", null, value);

        for (LibCompteurVanne v : s.getListeCompteursVanne()) {
            CompteurVanneDAO.addVanne(v, s.getNumSecteur(), ct);
        }

        return retour;
    }

    public static long deleteSecteurs(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        long retourV = CompteurVanneDAO.deleteVannes(ct);

        retour = bd.delete("secteur", null,null);
        return retour;
    }

}
