package com.bonaro.restaurant.Models;

/**
 * Created by lozano on 21/01/17.
 */
public class Idioma {
    public static final String TABLE_IDIOMA = "idioma";
    public static final String KEY_IDIOMAID = "id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_LOCAL = "local";

    public static final String QUERY_SELECT = "SELECT * FROM " + TABLE_IDIOMA;

    private int mId;
    private String mNombre;
    private String mLocal;

    public Idioma(int id, String nombre, String local) {
        this.mId = id;
        this.mNombre = nombre;
        this.mLocal = local;

    }

    public int getId() {
        return mId;
    }

    public String getNombre() {
        return mNombre;
    }

    public String getLocal() {
        return mLocal;
    }
}
