package com.btssio.appli_gest_vannes.passerelle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.btssio.appli_gest_vannes.classestechniques.ConversionDate;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibReleve;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReleveDAO {
    public static final String RELEVE_INDEX = "indexR";
    public static final String RELEVE_DATE = "dateReleve";
    public static final String RELEVE_COMPTEUR = "ref";
    public static List<LibReleve> getArrayReleve(LibCompteurVanne leCompteur, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        List<LibReleve> listeReleves = new ArrayList<>();
        Cursor curseurReleves;

        // l'accès à la base sera en lecture
        SQLiteDatabase bd = accesBD.getReadableDatabase();

        String req = "select " + RELEVE_DATE + ", " + RELEVE_INDEX + " from Releve where ref = '" +
                leCompteur.getRefCompteur() + "' order by substr("+ RELEVE_DATE + ", 7, 4) asc;";
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

    public static long addReleve(LibReleve r, Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(RELEVE_INDEX, r.getIndexReleve());
        value.put(RELEVE_DATE, ConversionDate.dateToString(r.getDateReleve(), "dd/MM/yyyy"));
        value.put(RELEVE_COMPTEUR, r.getLeCompteur().getRefCompteur());

        retour = bd.insert("releve", null, value);
        return retour;
    }

    public static long deleteReleves(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        retour = bd.delete("releve", null,null);
        return retour;
    }

    public static long deleteRelevesNotInCurrentYear(Context ct) {
        BdSQLiteOpenHelper accesBD = ConnexionDAO.getAccesBD(ct);
        long retour;

        SQLiteDatabase bd = accesBD.getWritableDatabase();

        retour = bd.delete("releve", "substr("+ RELEVE_DATE + ", 7, 4) != '" + LocalDate.now().getYear() + "'", null);
        return retour;
    }
}
