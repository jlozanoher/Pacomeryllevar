package com.bonaro.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonaro.restaurant.Models.Carta;
import com.bonaro.restaurant.Models.Grupo;
import com.bonaro.restaurant.Models.Oferta;

import java.util.List;

/**
 * Created by lozano on 23/01/17.
 * All rights reserved
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Carta mCarta;
    private List <Grupo> mGrupoList;

    public ExpandableListAdapter(Context context, Carta mCarta) {
        mContext = context;
        this.mCarta = mCarta;
        mGrupoList = mCarta.getGrupoList();
    }

    @Override
    public int getGroupCount() {
        return mGrupoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGrupoList.get(groupPosition).getOfertaList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGrupoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGrupoList.get(groupPosition).getOfertaList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
//        return mGrupoList.get(groupPosition).getId();
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
//        return mGrupoList.get(groupPosition).getOfertaList().get(childPosition).getId();
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Grupo grupo = (Grupo) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_expandable, null);
        }

        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
//        TextView txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
        ImageView imgGrupo = (ImageView) convertView.findViewById(R.id.imgGrupo);

        txtNombre.setText(grupo.getNombre());
//        txtDescripcion.setText(grupo.getDescripcion());
        imgGrupo.setImageBitmap(grupo.getImage());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Oferta oferta = (Oferta) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item, null);
        }

        TextView txtNombre = (TextView) convertView.findViewById(R.id.txtNombre);
        TextView txtPrecio = (TextView) convertView.findViewById(R.id.txtPrecio);
        TextView txtBuyingCount = (TextView) convertView.findViewById(R.id.txtBuyingCount);
        ImageView imgFavorte = (ImageView) convertView.findViewById(R.id.imgFavorite);
        ImageView imgShop = (ImageView) convertView.findViewById(R.id.imgShop);
        ImageView imgOferta = (ImageView) convertView.findViewById(R.id.imgOferta);


        txtNombre.setText(oferta.getNombre().trim());
        txtPrecio.setText(String.format("$%.2f", oferta.getPrecio()));
        String aux = "";
        if(oferta.getVecesParaComprar() > 0) aux = String.format("(x%d)=$%.2f", oferta.getVecesParaComprar(), oferta.getVecesParaComprar() * oferta.getPrecio());
        txtBuyingCount.setText(aux);
        imgOferta.setImageBitmap(oferta.getImage());

        if(oferta.getEsFavorito() == 1) imgFavorte.setVisibility(View.VISIBLE);
        else imgFavorte.setVisibility(View.GONE);

        if(oferta.getVecesParaComprar() > 0) imgShop.setVisibility(View.VISIBLE);
        else imgShop.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
