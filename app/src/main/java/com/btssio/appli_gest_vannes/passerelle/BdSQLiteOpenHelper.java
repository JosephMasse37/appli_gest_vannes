package com.btssio.appli_gest_vannes.passerelle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BdSQLiteOpenHelper extends SQLiteOpenHelper {

      private String requeteCreateCommune = "create table commune ( "
              + " 	id integer NOT NULL, "
              + " 	nom text NOT NULL, "
              + " 	primary key (id) "
              + " ); ";

    private String requeteCreateSecteur = " create table secteur ( "
            + " 	id integer NOT NULL, "
            + " 	idCommune integer NOT NULL, "
            + " 	libelle text NOT NULL, "
            + " 	primary key (id) "
            + " ); ";

    private String requeteCreateCompteur = " create table vanne ( "
            + " 	ref text NOT NULL, "
            + " 	idSecteur integer NOT NULL, "
            + " 	dateInstallation text NOT NULL, "
            + " 	marque text NOT NULL, "
            + " 	primary key (ref) "
            + " ); ";

    private String requeteCreateReleve = "create table releve ( "
            + " 	dateReleve text NOT NULL, "
            + " 	indexR long NOT NULL, "
            + " 	ref text NOT NULL "
            + " )";

    public BdSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // exécute la requête passée en paramètre (ici la création de la table)
        db.execSQL(requeteCreateCommune);
        db.execSQL(requeteCreateSecteur);
        db.execSQL(requeteCreateCompteur);
        db.execSQL(requeteCreateReleve);

        Log.i("bdseg", "base de tests créée");

        // ajout de lignes pour faire les tests
        ajoutLigne(db);

        Log.i("bdseg","insert ok");

    }

    private void ajoutLigne(SQLiteDatabase db) {
        db.execSQL("insert into commune values(17834,'Antrenas');");
        db.execSQL("insert into commune values(18011,'Grézes');");
        db.execSQL("insert into commune values(17911,'Le Buisson');");
        db.execSQL("insert into commune values(17863,'Montrodat');");
        db.execSQL("insert into commune values(17856,'Marvejols');");

        db.execSQL("insert into secteur values(1,17863,'Marquès');");
        db.execSQL("insert into secteur values(2,17863,'La Barthe');");
        db.execSQL("insert into secteur values(3,17863,'Gimels');");
        db.execSQL("insert into secteur values(4,17856,'Coulagnet');");
        db.execSQL("insert into secteur values(5,17856,'Soubeyran');");
        db.execSQL("insert into secteur values(6,17834,'Quartier d''Antrenas');");
        db.execSQL("insert into secteur values(7,18011,'Quartier de Grèzes');");
        db.execSQL("insert into secteur values(8,17911,'Quartier Buisson');");

        db.execSQL("insert into vanne values('V152634',1,'10/08/2010','BANYO');");
        db.execSQL("insert into vanne values('V589634',1,'11/06/2007','SAPPEL');");
        db.execSQL("insert into vanne values('V458965',1,'18/04/2008','SFERACO');");
        db.execSQL("insert into vanne values('V789521',2,'17/11/2017','SFERACO');");
        db.execSQL("insert into vanne values('V125614',2,'18/06/2016','SFERACO');");
        db.execSQL("insert into vanne values('V785024',3,'15/02/2008','BANYO');");
        db.execSQL("insert into vanne values('V236974',4,'18/05/2015','SAPPEL');");
        db.execSQL("insert into vanne values('V256478',5,'28/04/2012','SAPPEL');");


        db.execSQL("insert into releve values ('23/02/2024',62211,'V458965');");
        db.execSQL("insert into releve values ('23/02/2024',89230,'V589634');");
        db.execSQL("insert into releve values ('24/02/2024',15896,'V152634');");
        db.execSQL("insert into releve values ('24/02/2024',42910,'V789521');");
        db.execSQL("insert into releve values ('24/02/2024',75698,'V125614');");
        db.execSQL("insert into releve values ('25/02/2024',53189,'V785024');");
        db.execSQL("insert into releve values ('25/02/2024',66325,'V236974');");
        db.execSQL("insert into releve values ('19/03/2024',27800,'V256478');");
        db.execSQL("insert into releve values ('17/02/2025',45963,'V256478');");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
