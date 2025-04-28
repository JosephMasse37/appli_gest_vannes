package com.btssio.appli_gest_vannes.passerelle;

import android.content.Context;

public class ConnexionDAO {
    private static BdSQLiteOpenHelper accesBD;
    private static String base = "BDSEG";
    private static int version = 1;

    /**
     * initialise une instance de la classe BdSQLiteOpenHelper
     * @param ct un objet de la classe Context
     */
    private ConnexionDAO(Context ct) {
        accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
    }

    /**
     * vérifie si un l'objet accesBD est existe, si non il est créé
     * @param ct un objet de la classe Context permettant de placer la BD SQLite dans les ressources de l'application
     * @return un objet permettant l'accès à la BD "BDSEG"
     */
    public static BdSQLiteOpenHelper getAccesBD(Context ct) {
        if (accesBD == null) {
            accesBD = new BdSQLiteOpenHelper(ct, base, null, version);
        }
        return accesBD;
    }
}

