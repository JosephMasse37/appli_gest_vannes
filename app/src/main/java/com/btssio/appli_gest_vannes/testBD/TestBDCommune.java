package com.btssio.appli_gest_vannes.testBD;

import android.content.Context;
import android.util.Log;

import com.btssio.appli_gest_vannes.MainActivity;
import com.btssio.appli_gest_vannes.passerelle.CommuneDAO;
import com.btssio.appli_gest_vannes.passerelle.ReleveDAO;
import com.sio.libseg.metier.LibCommune;
import com.sio.libseg.metier.LibCompteurVanne;
import com.sio.libseg.metier.LibReleve;
import com.sio.libseg.metier.LibSecteur;

import java.util.List;

public class TestBDCommune {

    public static void afficheCommune(Context ct) {
        List<LibCommune> lesCommunes = CommuneDAO.getArrayCommunes(ct);

        for (LibCommune c : lesCommunes) {
            Log.i("TEST C : ", c.getNumCom() + " " + c.getNomCom());
            for (LibSecteur s : c.getListeSecteurs()) {
                Log.i("TEST S : ", s.getNumSecteur() + " " + s.getNomSecteur());
                for (LibCompteurVanne v : s.getListeCompteursVanne()) {
                    Log.i("TEST V : ", v.getRefCompteur() + " " + v.getMarque() + " " + v.getDateInstallation());

                    // Récupération des relevés
                    List<LibReleve> lesReleves = ReleveDAO.getArrayReleve(v, ct);
                    for (LibReleve r : lesReleves) {
                        Log.i("TEST R : ", r.getIndexReleve() + " " + r.getDateReleve());
                    }
                }
            }
        }
    }
}
