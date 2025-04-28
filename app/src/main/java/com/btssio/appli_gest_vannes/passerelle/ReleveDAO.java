package com.btssio.appli_gest_vannes.passerelle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.btssio.appli_gest_vannes.classestechniques.ConversionDate;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibReleve;

import java.util.ArrayList;
import java.util.List;

public class ReleveDAO {
    public static List<LibReleve> getArrayReleve(LibCompteurVanne leCompteur, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        List<LibReleve> listeReleves = new ArrayList<>();
        Cursor curseurReleves;

        // l'accès à la base sera en lecture
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        String req = "select dateReleve, indexR from Releve where ref = '" + leCompteur.getRefCompteur() + "';";
        curseurReleves = bd.rawQuery(req, null);

        curseurReleves.moveToFirst();
        while (!curseurReleves.isAfterLast()) {
            LibReleve unReleve = new LibReleve(ConversionDate.stringToDate(curseurReleves.getString(0), "dd/MM/yyyy"),
                    curseurReleves.getInt(1),
                    leCompteur);

            listeReleves.add(unReleve);
            curseurReleves.moveToNext();
        }

        return listeReleves;
    }
}
