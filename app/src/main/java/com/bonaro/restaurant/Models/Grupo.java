package com.bonaro.restaurant.Models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lozano on 21/01/17.
 * All rights reserved
 */
public class Grupo {
    public static final String TABLE_GRUPO = "Grupo";
    public static final String KEY_GRUPOID = "id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_IMAGEN = "imagen";

    private int mId;

    //Multilingual fields
    private String mNombre;
    private String mDescripcion;
    private Bitmap mImage;

    List <Oferta> mOfertaList;

    public Grupo(int id, String nombre, String descripcion, List<Oferta> ofertaList, Bitmap image) {
        this.mId = id;
        this.mNombre = nombre;
        this.mDescripcion = descripcion;
        this.mOfertaList = ofertaList;
        this.mImage = image;
    }

    public Grupo(int id, String nombre, String descripcion, Bitmap image) {
        this.mId = id;
        this.mNombre = nombre;
        this.mDescripcion = descripcion;
        this.mImage = image;
        this.mOfertaList = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public String getNombre() {
        return mNombre;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public List<Oferta> getOfertaList() {
        return mOfertaList;
    }

    public Bitmap getImage(){
        return mImage;
    }

    public void addOferta(Oferta oferta){
        mOfertaList.add(oferta);
    }

//    static public String getQueryGroupsByCard(int languageId, int cartaID){
//        return "Select Grupo_idioma.grupo_fk as id, Grupo_idioma.nombre, Grupo_idioma.descripcion, Grupo.imagen \n" +
//                "from Carta , Oferta, Grupo inner join Grupo_idioma on Grupo.id = Grupo_idioma.grupo_fk\n" +
//                "where Carta.id = Oferta.carta_fk and Grupo.id = Oferta.grupo_fk and Carta.id = " + cartaID +
//                "\nand Grupo_idioma.idioma_fk = " + languageId + " group by Grupo_idioma.grupo_fk";
//    }

    static public String getQueryGroupsByCard(int languageId, int cartaID){
        return "Select grupo_idioma.grupo_fk as id, grupo_idioma.nombre, grupo_idioma.descripcion, grupo.imagen\n" +
                "from carta , oferta, oferta_carta, grupo\n" +
                "inner join grupo_idioma on grupo.id = grupo_idioma.grupo_fk\n" +
                "where carta.id = oferta_carta.carta_fk and  oferta_carta.oferta_fk = oferta.id and grupo.id = oferta.grupo_fk and \n" +
                "carta.id = " + cartaID + "\n" +
                "and grupo_idioma.idioma_fk = " + languageId +"\n" +
                "group by grupo_idioma.grupo_fk";
    }
}
