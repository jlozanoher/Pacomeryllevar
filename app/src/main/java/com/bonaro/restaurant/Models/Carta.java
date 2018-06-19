package com.bonaro.restaurant.Models;

import com.bonaro.restaurant.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lozano on 21/01/17.
 * All rights reserved
 */

public class Carta {
    public static final String TABLE_CARTA = "Carta";
    public static final String KEY_CARTAID = "id";
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DESCRIPCION = "descripcion";

    private int mId;
    private int mIndex;

    //Multilingual fields
    private String mNombre;
    private String mDescripcion;

    // Fields for app
    private double mTotalPay;
    private List<Grupo> mGrupoList;

    public Carta(int id, String nombre, String descripcion, List<Grupo> grupoList, int listIndex, double totalPay) {
        this.mId = id;
        this.mNombre = nombre;
        this.mDescripcion = descripcion;
        this.mGrupoList = grupoList;
        this.mIndex = listIndex;
        this.mTotalPay = totalPay;
    }

    public Carta(int id, String nombre, String descripcion){
        this.mGrupoList = new ArrayList<>();
        this.mId = id;
        this.mNombre = nombre;
        this.mDescripcion = descripcion;
        this.mIndex = -1;
        this.mTotalPay = 0;
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

    public List<Grupo> getGrupoList() {
        return mGrupoList;
    }

    public void addGroup(Grupo grupo){
        // If grupo already exists on the mGrupoList
        for(Grupo g : mGrupoList){
            if(g.getId() == grupo.getId()){
                List<Oferta> ofertas = grupo.getOfertaList();
                for(Oferta oferta : ofertas){
                    g.addOferta(oferta);
                }
                return;
            }
        }
        //Else, add the new grupo
        mGrupoList.add(grupo);
    }

    public void updateOferta(Oferta oferta){
        int groupId = oferta.getGrupoFK();
        Grupo groupForDeleting = null;
        boolean flag = false;
        for(Grupo g : mGrupoList){
            if(g.getId() == groupId){
                flag = true;
                List<Oferta> ofertas = g.getOfertaList();
                int index = ofertas.indexOf(oferta);
                // If the offer was previously in the choosen list
                if(index != -1){
                    if(oferta.getVecesParaComprar() == 0){
                        ofertas.remove(oferta);  // remove de offer
                        if(ofertas.size() == 0) // If the group is empty
                            groupForDeleting = g;
                    }
                    else {
                        ofertas.set(index, oferta);  // update de offer
                    }
                }
                else {
                    if(oferta.getVecesParaComprar() > 0){
                        ofertas.add(oferta); // Add de offer
                    }
                }
            }
        }

        // If the offer belong to a group that isn't in the ChoosenOffers card, it needs to be created
        if(!flag && oferta.getVecesParaComprar() > 0){
            Grupo grupo = Controller.getInstance().getGrupoById(groupId);
            if(grupo != null){
                grupo.addOferta(oferta);
                mGrupoList.add(grupo);
            }
        }

        if(groupForDeleting != null)
            mGrupoList.remove(groupForDeleting);
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }

    public double getTotalPay() {
        return mTotalPay;
    }

    public void setTotalPay(double totalPay) {
        this.mTotalPay = totalPay;
    }

    public static String getQuerySelect(int languageId){
        return "Select Carta.id, Carta_idioma.nombre, Carta_idioma.descripcion\n" +
                " from Carta inner join Carta_idioma on Carta.id = Carta_idioma.carta_fk\n" +
                " where Carta_idioma.idioma_fk = " + languageId;
    }
}
