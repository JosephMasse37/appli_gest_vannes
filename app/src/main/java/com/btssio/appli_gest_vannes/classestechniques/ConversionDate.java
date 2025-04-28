package com.btssio.appli_gest_vannes.classestechniques;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConversionDate {

    /**
     * Convertit une chaîne de caractères en objet LocalDate
     *
     * @param date Une date au format fourni en paramètre
     * @return objet LocalDate ou null si la conversion n'est pas possible
     */
    public static LocalDate stringToDate(String date, String pattern) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convertit un objet LocalDate en chaîne de caractères
     * @param date un objet LocalDate
     * @return une chaîne au format fourni en paramètre ou null si la conversion n'est pas possible
     */
    public static String dateToString(LocalDate date, String pattern) {
        try {
            return date.format(DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
