package com.bonaro.restaurant.DetailedOffers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by lozano on 05/03/17.
 */
public class DetailedOfferPagerAdapter extends FragmentStatePagerAdapter {

    private int mTotalGrupos;
    private Carta mCarta;
    private int mGrupos[];
    private int mTotal;
    private int mCartaIndex;

    public DetailedOfferPagerAdapter(FragmentManager fm, int cardIndex){
        super(fm);
        mCartaIndex = cardIndex;
        mCarta = Controller.getInstance().getCartaList().get(cardIndex);
        mTotalGrupos = mCarta.getGrupoList().size();
        mGrupos = new int[mTotalGrupos];
        mTotal = 0;
        // mGrupos store the offers by group
        for(int i=0; i<mTotalGrupos; ++i){
            mGrupos[i] = mCarta.getGrupoList().get(i).getOfertaList().size();
            mTotal += mGrupos[i];
        }
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment fragment = new PageFragment();
        int grupoPosition = 0;
        for (int i = 0; i < mTotalGrupos; i++) {
            if(position >= mGrupos[i])
                position -= mGrupos[i];
            else {
                grupoPosition = i;
                break;
            }
        }
        Bundle args = new Bundle();
        args.putInt("CARTA_POSITION", mCartaIndex);
        args.putInt("GRUPO_POSITION", grupoPosition);
        args.putInt("OFERTA_POSITION", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTotal;
    }

    public static class PageFragment extends Fragment{

        private static int MAX_BUYNG = 99;
        private static int MIN_BUYNG = 1;
        private int mBuyingCount;
        private Oferta mOferta;
        private Carta mCarta;

        private ActivityComs mActivityComs;

        //Updatable content
        private TextView mTxtPrecio;
        private TextView mTxtBuyingCount;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_item_detailed, null);

            int cartaPosition = getArguments().getInt("CARTA_POSITION");
            int grupoPosition = getArguments().getInt("GRUPO_POSITION");
            int ofertaPosition = getArguments().getInt("OFERTA_POSITION");
            mOferta = Controller.getInstance().getCartaList().get(cartaPosition)
                    .getGrupoList().get(grupoPosition).getOfertaList().get(ofertaPosition);

            mBuyingCount = mOferta.getVecesParaComprar();
            final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);

            final Button btnContent = (Button) view.findViewById(R.id.btnContent);
            final LinearLayout linearLayoutContent = (LinearLayout) view.findViewById(R.id.layoutContent);
            final Button btnProperties = (Button) view.findViewById(R.id.btnProperties);

            // Offer's fields
            ImageView imgOferta = (ImageView) view.findViewById(R.id.imgOferta);
            TextView txtNombre = (TextView) view.findViewById(R.id.txtNombre);
            mTxtPrecio = (TextView) view.findViewById(R.id.txtPrecio);
            TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);
            TextView txtEnergia = (TextView) view.findViewById(R.id.txtEnergia);
            TextView txtProteina = (TextView) view.findViewById(R.id.txtProteina);
            TextView txtGrasa = (TextView) view.findViewById(R.id.txtGrasa);
            TextView txtColesterol = (TextView) view.findViewById(R.id.txtColesterol);
            TextView txtCarbohidratos = (TextView) view.findViewById(R.id.txtCarbohidratos);
            TextView txtFibra = (TextView) view.findViewById(R.id.txtFibra);
            TextView txtVitA = (TextView) view.findViewById(R.id.txtVitA);
            TextView txtVitB6 = (TextView) view.findViewById(R.id.txtVitB6);
            TextView txtVitB12 = (TextView) view.findViewById(R.id.txtVitB12);
            TextView txtVitC = (TextView) view.findViewById(R.id.txtVitC);
            TextView txtVitE = (TextView) view.findViewById(R.id.txtVitE);
            TextView txtPotasio = (TextView) view.findViewById(R.id.txtPotasio);
            TextView txtHierro = (TextView) view.findViewById(R.id.txtHierro);
            final TextView txtBiopropiedades = (TextView) view.findViewById(R.id.txtBiopropiedades);

            // NumberPicker component (this could be an option)
//        NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker);
//        np.setMinValue(1);// restricted number to minimum value i.e 1
//        np.setMaxValue(31);// restricked number to maximum value i.e. 31
//        np.setOrientation(NumberPicker.HORIZONTAL);
//        np.setWrapSelectorWheel(false);

            // Buying count
            final LinearLayout layoutBuyingCount = (LinearLayout) view.findViewById(R.id.layoutBuyingCount);
            final ImageButton imgPlus = (ImageButton) view.findViewById(R.id.imgPlus);
            final ImageButton imgMinus = (ImageButton) view.findViewById(R.id.imgMinus);
            mTxtBuyingCount = (TextView) view.findViewById(R.id.txtBuyingCount);
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


            final ImageButton imgFavorte = (ImageButton) view.findViewById(R.id.imgFavorite);
            final ImageButton imgShop = (ImageButton) view.findViewById(R.id.imgShop);

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

            txtNombre.setText(mOferta.getNombre().trim());
            txtDescripcion.setText(mOferta.getDescripcion());

            if(mOferta.getEnergiaKca() > 0)
                txtEnergia.setText(String.format("%s: %.3f", getString(R.string.energy), mOferta.getEnergiaKca()));
            else
                txtEnergia.setVisibility(View.GONE);

            if(mOferta.getProteinaG() > 0)
                txtProteina.setText(String.format("%s: %.3f", getString(R.string.protein), mOferta.getProteinaG()));
            else
                txtProteina.setVisibility(View.GONE);

            if(mOferta.getGrasaG() > 0)
                txtGrasa.setText(String.format("%s: %.3f", getString(R.string.fat), mOferta.getGrasaG()));
            else
                txtGrasa.setVisibility(View.GONE);

            if(mOferta.getColesterolMg() > 0)
                txtColesterol.setText(String.format("%s: %.3f", getString(R.string.cholesterol), mOferta.getColesterolMg()));
            else
                txtColesterol.setVisibility(View.GONE);

            if(mOferta.getCarbohidratosG() > 0)
                txtCarbohidratos.setText(String.format("%s: %.3f", getString(R.string.carbohydrate), mOferta.getCarbohidratosG()));
            else
                txtCarbohidratos.setVisibility(View.GONE);

            if(mOferta.getFibraG() > 0)
                txtFibra.setText(String.format("%s: %.3f", getString(R.string.fiber), mOferta.getFibraG()));
            else
                txtFibra.setVisibility(View.GONE);

            if(mOferta.getVitAUg() > 0)
                txtVitA.setText(String.format("%s: %.3f", getString(R.string.vitamin_a), mOferta.getVitAUg()));
            else
                txtVitA.setVisibility(View.GONE);

            if(mOferta.getVitB6Mg() > 0)
                txtVitB6.setText(String.format("%s: %.3f", getString(R.string.vitamin_b6), mOferta.getVitB6Mg()));
            else
                txtVitB6.setVisibility(View.GONE);

            if(mOferta.getVitB12Ug() > 0)
                txtVitB12.setText(String.format("%s: %.3f", getString(R.string.vitamin_b12), mOferta.getVitB12Ug()));
            else
                txtVitB12.setVisibility(View.GONE);

            if(mOferta.getVitCMg() > 0)
                txtVitC.setText(String.format("%s: %.3f", getString(R.string.vitamin_c), mOferta.getVitCMg()));
            else
                txtVitC.setVisibility(View.GONE);

            if(mOferta.getVitEMg() > 0)
                txtVitE.setText(String.format("%s: %.3f", getString(R.string.vitamin_e), mOferta.getVitEMg()));
            else
                txtVitE.setVisibility(View.GONE);

            if(mOferta.getPotasioMg() > 0)
                txtPotasio.setText(String.format("%s: %.3f", getString(R.string.potassium), mOferta.getPotasioMg()));
            else
                txtPotasio.setVisibility(View.GONE);

            if(mOferta.getHierroMg() > 0)
                txtHierro.setText(String.format("%s: %.3f", getString(R.string.iron), mOferta.getHierroMg()));
            else
                txtHierro.setVisibility(View.GONE);

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

            return view;
        }

        private void updateContent(){
            mTxtBuyingCount.setText(String.format("%d", mBuyingCount));
            String aux = "";
            if(mBuyingCount > 0)
                aux += String.format(" (%.2f)", mOferta.getPrecio() * mBuyingCount);
            mTxtPrecio.setText(String.format("$%.2f%s", mOferta.getPrecio(), aux));
        }
    }
}
