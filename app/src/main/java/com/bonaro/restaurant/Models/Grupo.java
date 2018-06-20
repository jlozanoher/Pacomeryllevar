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

    public Grupo(Grupo grupo){
        this.mId = grupo.getId();
        this.mNombre = grupo.getNombre();
        this.mDescripcion = grupo.getDescripcion();
        this.mImage = grupo.getImage();
        this.mOfertaList = null;
    }

    public void setOfertaList(List<Oferta> ofertaList){
        this.mOfertaList = ofertaList;
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
        return "Select ocg.grupo_fk, g.imagen, gi.nombre, gi.descripcion\n" +
                "from Oferta_Carta_Grupo ocg inner join Grupo g on ocg.grupo_fk = g.id\n" +
                "inner join Grupo_idioma gi on g.id = gi.grupo_fk\n" +
                "where ocg.carta_fk = " + cartaID +
                " and gi.idioma_fk = " + languageId + "\n" +
                "group by ocg.grupo_fk";
    }
}
