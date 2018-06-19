package com.bonaro.restaurant;

/**
 * Created by lozano on 23/01/17.
 * All rights reserved
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bonaro.restaurant.DetailedOffers.DialogOfertaDetailed;
import com.bonaro.restaurant.Models.Carta;
import com.bonaro.restaurant.Models.Grupo;
import com.bonaro.restaurant.Models.Oferta;

/**
 * A placeholder fragment containing the section view, this section is the card
 * that is being presented.
 */
public class SectionFragment extends Fragment implements SectionInterface {

    private static final String INDEX_CARTA = "INDEX_CARTA";
    private static final int MENU_SEE = 0;
    private static final int MENU_ADD = 1;
    private static final int MENU_REMOVE = 2;
    private static final int MENU_FAVORITE = 3;


    private ExpandableListAdapter mExpandableListAdapter;
    private ExpandableListView mExpandableListView;
    private Parcelable mState;

    private Carta mCarta;
    private int mIndexCard;

    View mRootView;
    private TextView mTxtPay;
    private TextView mTxtTitlePay;

    private ActivityComs mActivityComs;

    public SectionFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SectionFragment newInstance(int indexCarta) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX_CARTA, indexCarta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityComs = (ActivityComs) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityComs = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        registerForContextMenu(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mIndexCard = getArguments().getInt(INDEX_CARTA);

        mCarta = Controller.getInstance().getCartaList().get(mIndexCard);

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

        // If it's the choosen card
        if(mIndexCard == Controller.getInstance().getCartaList().size() - 1){
            FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearAllOffers();
                    Snackbar snack = Snackbar.make(mRootView, "Snack", Snackbar.LENGTH_LONG);
                    View snackView = snack.getView();
                    TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.setText(R.string.my_order_erased);
                    snack.show();
                }
            });
        }

        mTxtPay = (TextView) mRootView.findViewById(R.id.txtPay);
        mTxtTitlePay = (TextView) mRootView.findViewById(R.id.txtTitlePay);
        TextView txtDescription = (TextView) mRootView.findViewById(R.id.txtDescripcion);

        if(mCarta.getDescripcion() == null || mCarta.getDescripcion().equals(""))
            txtDescription.setVisibility(View.GONE);
        else {
            txtDescription.setText(mCarta.getDescripcion());
        }

        mExpandableListAdapter = new ExpandableListAdapter(getActivity(), mCarta);

        mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.expadableListView);
        mExpandableListView.setAdapter(mExpandableListAdapter);

        // Register the long click to a menu
        registerForContextMenu(mExpandableListView);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mState = mExpandableListView.onSaveInstanceState();
                DialogOfertaDetailed dialogOfertaDetailed = new DialogOfertaDetailed();
//                Toast.makeText(getActivity(), "group: " + groupPosition + " child: " + childPosition, Toast.LENGTH_LONG).show();
                Oferta oferta = Controller.getInstance().getCartaList().get(mCarta.getIndex()).getGrupoList().get(groupPosition)
                .getOfertaList().get(childPosition);
                dialogOfertaDetailed.setOfertaAndCard(oferta, mCarta);
                dialogOfertaDetailed.show(getFragmentManager(), "123");
                return true;
            }
        });

        updateData();

        return mRootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        // Only create a context menu for child items
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            // Array created earlier when we built the expandable list
//            String page = mListStringArray[group][child];

            menu.setHeaderTitle("Opciones");
            menu.add(0, MENU_ADD, 0, R.string.menu_add);
            menu.add(0, MENU_REMOVE, 0, R.string.menu_remove);
            menu.add(0, MENU_FAVORITE, 0, R.string.menu_favorite);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if( getUserVisibleHint() == false )
        {
            return false;
        }

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        int groupPos = 0, childPos = 0;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
        {
            groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
            childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
        }

//        Toast.makeText(getActivity(), "group: " + groupPos + " child: " + childPos, Toast.LENGTH_LONG).show();

        int cardIndex = mCarta.getIndex();
        Oferta oferta = Controller.getInstance().getCartaList().get(cardIndex).getGrupoList().get(groupPos)
                .getOfertaList().get(childPos);

        int buyingCount = 0;
        double initialPrice = oferta.getPrecio() * oferta.getVecesParaComprar();

        Snackbar snack = Snackbar.make(mRootView, "Snack", Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        switch (item.getItemId())
        {
            case MENU_ADD:
                buyingCount = oferta.getVecesParaComprar() + 1;
                oferta.setVecesParaComprar(buyingCount);
                Controller.getInstance().updateCardTotalPay(mCarta, mCarta.getTotalPay() - initialPrice + oferta.getPrecio() * buyingCount);
                Controller.getInstance().updateOferta(oferta);

                snack.setText(getString(R.string.added_one) + ": " + oferta.getNombre());
                snack.show();

                if(mActivityComs == null) return true;
                mActivityComs.updateData();
                return true;

            case MENU_REMOVE:
                removeOfferFromChossenCard(oferta);
                snack.setText(getString(R.string.order_eliminated) + ": " + oferta.getNombre());
                snack.show();
                return true;

            case MENU_FAVORITE:
                oferta.setEsFavorito((oferta.getEsFavorito()+1)%2);

                snack.setText( getString(R.string.toggled_favorites) + ": " + oferta.getNombre());
                snack.show();

                if(mActivityComs == null) return true;
                mActivityComs.updateData();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void updateData(){
        mExpandableListAdapter.notifyDataSetChanged();
        mTxtPay.setVisibility(View.GONE);
        mTxtTitlePay.setVisibility(View.GONE);

        // Auto expand groups
        if(mIndexCard == Controller.getInstance().getCartaList().size()-1 || mCarta.getGrupoList().size() == 1){
            int groups = mExpandableListAdapter.getGroupCount();
            for ( int i = 0; i < groups; i++ )
                mExpandableListView.expandGroup(i);

            if(mIndexCard == Controller.getInstance().getCartaList().size()-1){
                mTxtPay.setVisibility(View.VISIBLE);
                mTxtTitlePay.setVisibility(View.VISIBLE);
                // Update the prices
                try{
                    mTxtPay.setText(String.format("$%.2f", Controller.getInstance().getTotalToPayAllCards()));
                }
                catch (Exception e){
                    Log.d("My app", "trying to set the price text", e);
                }
            }
        }


        if(mState != null && mExpandableListView != null){
                mExpandableListView.onRestoreInstanceState(mState);
        }
    }

    @Override
    public void updateLanguage(){
        mExpandableListAdapter.notifyDataSetChanged();
    }

    private void removeOfferFromChossenCard(Oferta oferta){
        int buyingCount = 0;
        double initialPrice = oferta.getPrecio() * oferta.getVecesParaComprar();
        oferta.setVecesParaComprar(buyingCount);
        Controller.getInstance().updateCardTotalPay(mCarta, mCarta.getTotalPay() - initialPrice + oferta.getPrecio() * buyingCount);
        Controller.getInstance().updateOferta(oferta);
        if(mActivityComs == null) return;
        mActivityComs.updateData();
    }

    private void clearAllOffers(){
        while (mCarta.getGrupoList().size() > 0){
            Grupo grupo = mCarta.getGrupoList().get(0);
            for (int j=0; j<grupo.getOfertaList().size(); ++j){
                Oferta oferta = grupo.getOfertaList().get(j);
                removeOfferFromChossenCard(oferta);
            }
        }
    }

}