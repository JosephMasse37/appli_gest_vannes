package com.btssio.appli_gest_vannes.passerelle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.btssio.appli_gest_vannes.classestechniques.ConversionDate;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibSecteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph Massé on 14/01/2025.
 */
public class CompteurVanneDAO {

    public static final String VANNE_KEY = "ref";
    public static final String VANNE_DATE = "dateInstallation";
    public static final String VANNE_MARQUE = "marque";
    public static final String VANNE_SECTEUR = "idSecteur";


    /**
     * récupère tous les compteurs vanne pour un secteur
     *
     * @param idSecteur identifiant du secteur
     * @param ct Le contexte
     * @return List d'objets CompteurVanne
     */
    public static List<LibCompteurVanne> getArrayCompteurVanne(int idSecteur, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        List<LibCompteurVanne> listeCompteursVanne = new ArrayList<>();
        Cursor curseurCompteurs;

        // l'accès à la base sera en lecture
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        String req = "select ref, dateInstallation, marque from vanne where idSecteur =" + idSecteur;
        curseurCompteurs = bd.rawQuery(req, null);

        curseurCompteurs.moveToFirst();
        while (!curseurCompteurs.isAfterLast()) {
            LibCompteurVanne vanne = new LibCompteurVanne(curseurCompteurs.getString(0),
                    ConversionDate.stringToDate(curseurCompteurs.getString(1),"dd/MM/yyyy"),
                    curseurCompteurs.getString(2) );
            listeCompteursVanne.add(vanne);
            curseurCompteurs.moveToNext();
        }

        return listeCompteursVanne;
    }

    public static long addVanne(LibCompteurVanne v, int idSecteur, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;
        SQLiteDatabase bd = accesBD.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(VANNE_KEY, v.getRefCompteur());
        value.put(VANNE_MARQUE, v.getMarque());
        value.put(VANNE_DATE, ConversionDate.dateToString(v.getDateInstallation(),"dd/MM/yyyy"));
        value.put(VANNE_SECTEUR, idSecteur);
        retour = bd.insert("vanne", null, value);

        return retour;
    }

    public static long deleteVannes(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        long retourR = ReleveDAO.deleteReleves(ct);

        retour = bd.delete("vanne", null,null);
        return retour;
    }
}
