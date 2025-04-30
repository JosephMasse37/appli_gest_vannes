package com.btssio.appli_gest_vannes.passerelle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sio.libseg.metier.LibCommune;

import java.util.ArrayList;
import java.util.List;

public class CommuneDAO {
    public static List<LibCommune> getArrayCommunes(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        List<LibCommune> listeCommunes = new ArrayList<>();
        Cursor curseurCommunes;

        // l'accès à la base sera en lecture
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        String req = "select id, nom from commune";
        curseurCommunes = bd.rawQuery(req, null);

        curseurCommunes.moveToFirst();
        while (!curseurCommunes.isAfterLast()) {
            LibCommune uneCommune = new LibCommune(curseurCommunes.getInt(0),
                    curseurCommunes.getString(1));
            listeCommunes.add(uneCommune);

            uneCommune.setListeSecteurs(SecteurDAO.getArraySecteur(uneCommune.getNumCom(), ct));
            curseurCommunes.moveToNext();
        }

        return listeCommunes;
    }

    public static long deleteCommunes(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        long retourS = SecteurDAO.deleteSecteurs(ct);

        retour = bd.delete("commune", null,null);
        return retour;
    }

    public static ArrayList<LibCommune> communesWS (Context ct) {
        List<LibCommune> oldCommunes = getArrayCommunes(ct);
        ArrayList<LibCommune> newCommunes = new ArrayList<>();

        for (LibCommune c : oldCommunes) {
            LibCommune tempCommune = new LibCommune(c.getNumCom(), c.getNomCom());
            tempCommune.setListeSecteurs(c.getListeSecteurs());
            newCommunes.add(tempCommune);
        }

        return newCommunes;
    }
}