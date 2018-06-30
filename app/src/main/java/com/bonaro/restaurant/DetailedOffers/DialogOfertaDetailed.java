package com.bonaro.restaurant.DetailedOffers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bonaro.restaurant.ActivityComs;
import com.bonaro.restaurant.Controller;
import com.bonaro.restaurant.Models.Carta;
import com.bonaro.restaurant.Models.Oferta;
import com.bonaro.restaurant.R;

/**
 * Created by lozano on 24/01/17.
 * All rights reserved
 */
public class DialogOfertaDetailed extends DialogFragment {

    private static String CARTA_INDEX = "CARTA_INDEX";
    private static String GRUPO_INDEX = "GRUPO_INDEX";
    private static String OFERTA_INDEX = "OFERTA_INDEX";

    private static int MAX_BUYNG = 99;
    private static int MIN_BUYNG = 1;
    private int mBuyingCount;
    private Oferta mOferta;
    private Carta mCarta;
    private DialogOfertaDetailed mSelf;
    
    private ActivityComs mActivityComs;

    //Updatable content
    private TextView mTxtPrecio;
    private TextView mTxtBuyingCount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityComs = (ActivityComs) context;
        mSelf = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityComs = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBuyingCount = mOferta.getVecesParaComprar();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_item_detailed, null);

        final ScrollView scrollView = (ScrollView) dialogView.findViewById(R.id.scrollView);

        final Button btnContent = (Button) dialogView.findViewById(R.id.btnContent);
        final LinearLayout linearLayoutContent = (LinearLayout) dialogView.findViewById(R.id.layoutContent);
        final Button btnProperties = (Button) dialogView.findViewById(R.id.btnProperties);
        ImageButton btnClose = (ImageButton) dialogView.findViewById(R.id.btnClose);

        // Offer's fields
        ImageView imgOferta = (ImageView) dialogView.findViewById(R.id.imgOferta);
        TextView txtNombre = (TextView) dialogView.findViewById(R.id.txtNombre);
        mTxtPrecio = (TextView) dialogView.findViewById(R.id.txtPrecio);
        TextView txtDescripcion = (TextView) dialogView.findViewById(R.id.txtDescripcion);
        TextView txtEnergia = (TextView) dialogView.findViewById(R.id.txtEnergia);
        TextView txtProteina = (TextView) dialogView.findViewById(R.id.txtProteina);
        TextView txtGrasa = (TextView) dialogView.findViewById(R.id.txtGrasa);
        TextView txtColesterol = (TextView) dialogView.findViewById(R.id.txtColesterol);
        TextView txtCarbohidratos = (TextView) dialogView.findViewById(R.id.txtCarbohidratos);
        TextView txtFibra = (TextView) dialogView.findViewById(R.id.txtFibra);
        TextView txtVitA = (TextView) dialogView.findViewById(R.id.txtVitA);
        TextView txtVitB6 = (TextView) dialogView.findViewById(R.id.txtVitB6);
        TextView txtVitB12 = (TextView) dialogView.findViewById(R.id.txtVitB12);
        TextView txtVitC = (TextView) dialogView.findViewById(R.id.txtVitC);
        TextView txtVitE = (TextView) dialogView.findViewById(R.id.txtVitE);
        TextView txtPotasio = (TextView) dialogView.findViewById(R.id.txtPotasio);
        TextView txtHierro = (TextView) dialogView.findViewById(R.id.txtHierro);
        final TextView txtBiopropiedades = (TextView) dialogView.findViewById(R.id.txtBiopropiedades);

        // NumberPicker component (this could be an option)
//        NumberPicker np = (NumberPicker) dialogView.findViewById(R.id.numberPicker);
//        np.setMinValue(1);// restricted number to minimum value i.e 1
//        np.setMaxValue(31);// restricked number to maximum value i.e. 31
//        np.setOrientation(NumberPicker.HORIZONTAL);
//        np.setWrapSelectorWheel(false);

        //Close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelf.dismiss();
            }
        });

        // Buying count
        final LinearLayout layoutBuyingCount = (LinearLayout) dialogView.findViewById(R.id.layoutBuyingCount);
        final ImageButton imgPlus = (ImageButton) dialogView.findViewById(R.id.imgPlus);
        final ImageButton imgMinus = (ImageButton) dialogView.findViewById(R.id.imgMinus);
        mTxtBuyingCount = (TextView) dialogView.findViewById(R.id.txtBuyingCount);
        updateContent();

        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuyingCount < MAX_BUYNG){
                    ++mBuyingCount;
                    updateContent();
                }
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuyingCount > MIN_BUYNG){
                    --mBuyingCount;
                    updateContent();
                }
            }
        });


        final ImageButton imgFavorte = (ImageButton) dialogView.findViewById(R.id.imgFavorite);
        final ImageButton imgShop = (ImageButton) dialogView.findViewById(R.id.imgShop);

        if(mOferta.getEsFavorito() == 1)
            imgFavorte.setImageResource(R.drawable.favorite_on);
        else
            imgFavorte.setImageResource(R.drawable.favorite_off);

        if(mBuyingCount > 0) {
            imgShop.setImageResource(R.drawable.shopping_on);
            layoutBuyingCount.setVisibility(View.VISIBLE);
        }
        else{
            imgShop.setImageResource(R.drawable.shopping_off);
            layoutBuyingCount.setVisibility(View.GONE);
        }

        imgOferta.setImageBitmap(mOferta.getImage());
        if(mOferta.getImage() == null)
            imgOferta.setVisibility(View.GONE);

        String nombreOfertaItem = mOferta.getId() + " " + mOferta.getNombre().trim();
        txtNombre.setText(nombreOfertaItem);
        txtDescripcion.setText(mOferta.getDescripcion());

        int cont = 0;

        if(mOferta.getEnergiaKca() > 0)
            txtEnergia.setText(String.format("%s: %.3f", getString(R.string.energy), mOferta.getEnergiaKca()));
        else{
            txtEnergia.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getProteinaG() > 0)
            txtProteina.setText(String.format("%s: %.3f", getString(R.string.protein), mOferta.getProteinaG()));
        else {
            txtProteina.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getGrasaG() > 0)
            txtGrasa.setText(String.format("%s: %.3f", getString(R.string.fat), mOferta.getGrasaG()));
        else {
            txtGrasa.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getColesterolMg() > 0)
            txtColesterol.setText(String.format("%s: %.3f", getString(R.string.cholesterol), mOferta.getColesterolMg()));
        else{
            txtColesterol.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getCarbohidratosG() > 0)
            txtCarbohidratos.setText(String.format("%s: %.3f", getString(R.string.carbohydrate), mOferta.getCarbohidratosG()));
        else{
            txtCarbohidratos.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getFibraG() > 0)
            txtFibra.setText(String.format("%s: %.3f", getString(R.string.fiber), mOferta.getFibraG()));
        else{
            txtFibra.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getVitAUg() > 0)
            txtVitA.setText(String.format("%s: %.3f", getString(R.string.vitamin_a), mOferta.getVitAUg()));
        else{
            txtVitA.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getVitB6Mg() > 0)
            txtVitB6.setText(String.format("%s: %.3f", getString(R.string.vitamin_b6), mOferta.getVitB6Mg()));
        else{
            txtVitB6.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getVitB12Ug() > 0)
            txtVitB12.setText(String.format("%s: %.3f", getString(R.string.vitamin_b12), mOferta.getVitB12Ug()));
        else{
            txtVitB12.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getVitCMg() > 0)
            txtVitC.setText(String.format("%s: %.3f", getString(R.string.vitamin_c), mOferta.getVitCMg()));
        else{
            txtVitC.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getVitEMg() > 0)
            txtVitE.setText(String.format("%s: %.3f", getString(R.string.vitamin_e), mOferta.getVitEMg()));
        else{
            txtVitE.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getPotasioMg() > 0)
            txtPotasio.setText(String.format("%s: %.3f", getString(R.string.potassium), mOferta.getPotasioMg()));
        else{
            txtPotasio.setVisibility(View.GONE);
            ++cont;
        }

        if(mOferta.getHierroMg() > 0)
            txtHierro.setText(String.format("%s: %.3f", getString(R.string.iron), mOferta.getHierroMg()));
        else{
            txtHierro.setVisibility(View.GONE);
            ++cont;
        }

        // Hide some elements if they're empty
        if(cont == 13) btnContent.setVisibility(View.GONE);

        String bioprops = mOferta.getBiopropiedades();
        if(bioprops == null || bioprops.equals("")) btnProperties.setVisibility(View.GONE);

        txtBiopropiedades.setText(mOferta.getBiopropiedades());

        imgFavorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOferta.getEsFavorito() == 0)
                    imgFavorte.setImageResource(R.drawable.favorite_on);
                else
                    imgFavorte.setImageResource(R.drawable.favorite_off);
                // Favorite == 1, Not favorite == 0, this swaps the favorite field
                mOferta.setEsFavorito((mOferta.getEsFavorito()+1)%2);
//                Toast.makeText(mContext, "Click on image favorite", Toast.LENGTH_LONG).show();
            }
        });

        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBuyingCount == 0) {
                    imgShop.setImageResource(R.drawable.shopping_on);
                    layoutBuyingCount.setVisibility(View.VISIBLE);
                    mBuyingCount = 1;

                }
                else{
                    layoutBuyingCount.setVisibility(View.GONE);
                    imgShop.setImageResource(R.drawable.shopping_off);
                    mBuyingCount = 0;
                }
                updateContent();
//                Toast.makeText(mContext, "Click on image favorite", Toast.LENGTH_LONG).show();
            }
        });

        // Swipe visibility of LinearLayoutContent
        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutContent.getVisibility() == View.GONE){
                    btnContent.setCompoundDrawablesWithIntrinsicBounds( R.drawable.arriba, 0, 0, 0);
                    linearLayoutContent.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, scrollView.getBottom());
//                            scrollView.scrollTo(0, scrollView.getBottom());
//                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else{
                    btnContent.setCompoundDrawablesWithIntrinsicBounds( R.drawable.abajo, 0, 0, 0);
                    linearLayoutContent.setVisibility(View.GONE);
                }
            }
        });

        btnProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtBiopropiedades.getVisibility() == View.GONE) {
                    btnProperties.setCompoundDrawablesWithIntrinsicBounds( R.drawable.arriba, 0, 0, 0);
                    txtBiopropiedades.setVisibility(View.VISIBLE);
                }
                else{
                    btnProperties.setCompoundDrawablesWithIntrinsicBounds( R.drawable.abajo, 0, 0, 0);
                    txtBiopropiedades.setVisibility(View.GONE);
                }
//                txtBiopropiedades.requestFocus();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, scrollView.getBottom());
//                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });



//        builder.setView(dialogView); //.setMessage("Oferta detallada");
        AlertDialog resultDialog = builder.create();
        resultDialog.setView(dialogView, 0, 0, 0, 0);
        return resultDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        double initialPrice = mOferta.getPrecio() * mOferta.getVecesParaComprar();
        mOferta.setVecesParaComprar(mBuyingCount);  // Update VecesParaComprar field
        Controller.getInstance().updateCardTotalPay(mCarta, mCarta.getTotalPay() - initialPrice + mOferta.getPrecio() * mBuyingCount);

        Controller.getInstance().updateOferta(mOferta);
        if(mActivityComs == null) return;
        mActivityComs.updateData();
    }


    public void setOfertaAndCard(Oferta oferta, Carta carta) {
        this.mOferta = oferta;
        this.mCarta = carta;
    }

    private void updateContent(){
        mTxtBuyingCount.setText(String.format("%d", mBuyingCount));
        String aux = "";
        if(mBuyingCount > 0)
            aux += String.format(" (%.2f)", mOferta.getPrecio() * mBuyingCount);
        mTxtPrecio.setText(String.format("$%.2f%s", mOferta.getPrecio(), aux));
    }

}
