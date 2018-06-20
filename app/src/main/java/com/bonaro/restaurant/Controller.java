package com.bonaro.restaurant;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.bonaro.restaurant.Models.Carta;
import com.bonaro.restaurant.Models.Grupo;
import com.bonaro.restaurant.Models.Idioma;
import com.bonaro.restaurant.Models.Oferta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lozano on 21/01/17.
 * All rights reserved
 */

public class Controller {
    private static Controller ourInstance = new Controller();

    public static Controller getInstance() {
        return ourInstance;
    }

    private MyDatabase mDatabase;
    private SQLiteDatabase mdb;

    private List<Idioma> mIdiomaList;
    private List<Carta> mCartaList;
    private Map<Integer, Grupo> mGrupoMap;
    private Carta mMyChoosenCard;
    private double mTotalToPayAllCards;

    private int mLanguageSelected = 1;
    private Idioma mSelectedIdioma;

    private LruCache<Integer, Bitmap> mBitmapCache;  // Cache for images

    private Context mContext;


    private Controller() {
        mIdiomaList = new ArrayList<>();
        mCartaList = new ArrayList<>();
        mGrupoMap = new HashMap<>();

        // Use 1/8th of the available memory for this memory cache.
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        final int cacheSize = maxMemory / 8;
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);

        mBitmapCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    private void addBitmapToMemoryCache(Integer key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mBitmapCache.put(key, bitmap);
            }
    }

    private Bitmap getBitmapFromMemCache(Integer key) {
        return mBitmapCache.get(key);
    }

    public void createDatabase(Context context, int language){
        mContext = context;
        mLanguageSelected = language;

        if(mDatabase == null){
            mDatabase = new MyDatabase(context);
        }

        mdb = mDatabase.getReadableDatabase();
        Cursor cursor;
        //Fetching Idioma Table
        cursor = mdb.rawQuery(Idioma.QUERY_SELECT, null);
        cursor.moveToFirst();
        mIdiomaList.clear();
        while (!cursor.isAfterLast()){
            Idioma idioma = new Idioma(cursor.getInt(cursor.getColumnIndex(Idioma.KEY_IDIOMAID)),
                    cursor.getString(cursor.getColumnIndex(Idioma.KEY_NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Idioma.KEY_LOCAL)));
            mIdiomaList.add(idioma);
            cursor.moveToNext();
        }
        cursor.close();

        changeLanguage(mLanguageSelected);
        mdb.close();

    }

    public void changeLanguage(int languageId){
        for (Idioma idioma : mIdiomaList) {
            if(idioma.getId() == languageId)
                mSelectedIdioma = idioma;
        }

        Locale locale = new Locale(mSelectedIdioma.getLocal());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());

        mCartaList.clear();
        mLanguageSelected = languageId;
        mTotalToPayAllCards = 0;

        mdb = mDatabase.getReadableDatabase();

        //Fetching Carta Table
        String cardQuery = Carta.getQuerySelect(mLanguageSelected);
        Cursor cursorCarta = mdb.rawQuery(cardQuery, null);
        cursorCarta.moveToFirst();

        int totalCartas = 0;

        mMyChoosenCard = new Carta(-1, mContext.getString(R.string.title_my_order) , "");

        while (!cursorCarta.isAfterLast()){
            int cartaID = cursorCarta.getInt(cursorCarta.getColumnIndex(Carta.KEY_CARTAID));
            int totalOffersInCard = 0;
            double totalPay = 0;

            //Fetching Grupo Table
            List<Grupo> grupoList = new ArrayList<>();
            String groupsByCardQuery = Grupo.getQueryGroupsByCard(mLanguageSelected, cartaID);
            Cursor cursorGrupo = mdb.rawQuery(groupsByCardQuery, null);
            cursorGrupo.moveToFirst();

            while (!cursorGrupo.isAfterLast()){
                int grupoId = cursorGrupo.getInt(cursorGrupo.getColumnIndex(Oferta.KEY_GRUPOFK));

                String grupoNombre = cursorGrupo.getString(cursorGrupo.getColumnIndex(Grupo.KEY_NOMBRE));
                String grupoDescripcion = cursorGrupo.getString(cursorGrupo.getColumnIndex(Grupo.KEY_DESCRIPCION));

                //Multipliying groupId by 1000 for unique key on the map
                int grupoKeyOnMap = grupoId * 1000;
                Bitmap bitmap = getBitmapFromMemCache(grupoKeyOnMap); // Get cached Bitmap

                if(bitmap == null){
                    byte[] byteArray = cursorGrupo.getBlob(cursorGrupo.getColumnIndex(Oferta.KEY_IMAGEN));

                    if(byteArray != null) {
                        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        addBitmapToMemoryCache(grupoKeyOnMap, bitmap);  //Cache bitmap
                    }
                }

                Grupo grupo = new Grupo(grupoId,
                        grupoNombre,
                        grupoDescripcion,
                        new ArrayList<Oferta>(),
                        bitmap);

                mGrupoMap.put(grupoId, new Grupo(grupoId, grupoNombre, grupoDescripcion, bitmap));

                grupoList.add(grupo);
                cursorGrupo.moveToNext();
            }
            cursorGrupo.close();

            Carta carta = new Carta(cartaID,
                    cursorCarta.getString(cursorCarta.getColumnIndex(Carta.KEY_NOMBRE)),
                    cursorCarta.getString(cursorCarta.getColumnIndex(Carta.KEY_DESCRIPCION)),
                    grupoList,
                    totalCartas,
                    totalPay);

            ++totalCartas;
            mCartaList.add(carta);
            cursorCarta.moveToNext();
        }
        cursorCarta.close();

        mMyChoosenCard.setIndex(totalCartas);
        mCartaList.add(mMyChoosenCard);

        // My offer card

        // ALL Offers
        //Fetching Grupo Table
        String allOffersQuery = Oferta.getAllQuery(mLanguageSelected);
        Cursor cursorAllOffers = mdb.rawQuery(allOffersQuery, null);
        cursorAllOffers.moveToFirst();

        while (!cursorAllOffers.isAfterLast()){
            int oferta_id = cursorAllOffers.getInt(cursorAllOffers.getColumnIndex(Oferta.KEY_OFERTAID));

            String ofertaCartaGrupoQuery = Oferta.getOfertaCartaGrupoQuery(oferta_id);
            Cursor ocgCursor = mdb.rawQuery(ofertaCartaGrupoQuery, null);
            ocgCursor.moveToFirst();
            while (!ocgCursor.isAfterLast()){
                int carta_id = ocgCursor.getInt(ocgCursor.getColumnIndex(Oferta.KEY_CARTAFK));
                int grupo_id = ocgCursor.getInt(ocgCursor.getColumnIndex(Oferta.KEY_GRUPOFK));
                int vecesParaComprar = ocgCursor.getInt(ocgCursor.getColumnIndex(Oferta.KEY_VECESPARACOMPRAR));

                Grupo grupo = getGrupoOfCartaByIds(carta_id, grupo_id);
                if(grupo == null){
                    ocgCursor.moveToNext();
                    continue;
                }

                Oferta oferta = new Oferta(oferta_id,
                            cursorAllOffers.getString(cursorAllOffers.getColumnIndex(Oferta.KEY_NOMBRE)),
                            cursorAllOffers.getString(cursorAllOffers.getColumnIndex(Oferta.KEY_DESCRIPCION)),
                            cursorAllOffers.getString(cursorAllOffers.getColumnIndex(Oferta.KEY_BIOPROPS)),
                            grupo_id,
                            carta_id,
                            cursorAllOffers.getDouble(cursorAllOffers.getColumnIndex(Oferta.KEY_PRECIO)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_ENERGIA)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_PROTEINA)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_GRASA)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_COLESTEROL)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_CARBOHIDRATOS)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_FIBRA)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_VITA)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_VITB6)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_VITB12)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_VITC)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_VITE)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_POTASIO)),
                            cursorAllOffers.getFloat(cursorAllOffers.getColumnIndex(Oferta.KEY_HIERRO)),
                            cursorAllOffers.getInt(cursorAllOffers.getColumnIndex(Oferta.KEY_ESFAVORITO)),
                            vecesParaComprar,
                            getImageByOfertaId(oferta_id),
                            -1
                    );

                grupo.addOferta(oferta);

                ocgCursor.moveToNext();
            }
            ocgCursor.close();

            cursorAllOffers.moveToNext();
        }
        cursorAllOffers.close();

        mdb.close();

        populateOffersList();
    }

    private void populateOffersList(){
        for(int i=0; i<mCartaList.size()-1; ++i){
            Carta carta = mCartaList.get(i);
            for(Grupo grupo: carta.getGrupoList()){
                List<Oferta> myOfertaList = new ArrayList<>();
                for(Oferta oferta: grupo.getOfertaList()){
                    if(oferta.getVecesParaComprar() > 0){
                        myOfertaList.add(oferta);
                        mTotalToPayAllCards += oferta.getPrecio() * oferta.getVecesParaComprar();
                    }
                }
                if(myOfertaList.isEmpty()) continue;
                // If has offers, add the group to myChoosenCard
                Grupo myGrupo = new Grupo(grupo);
                myGrupo.setOfertaList(myOfertaList);
                mMyChoosenCard.addGroup(myGrupo);
            }
        }
    }

    private Carta getCartaById(int carta_id){
        for(Carta carta : mCartaList){
            if(carta.getId() == carta_id) return carta;
        }
        return null;
    }

    private Grupo getGrupoOfCartaByIds(int carta_id, int grupo_id){
        Carta carta = getCartaById(carta_id);
        if(carta == null) return null;
        List <Grupo> grupos = carta.getGrupoList();
        if(grupos.isEmpty()) return null;
        for(Grupo grupo : grupos){
            if(grupo.getId() == grupo_id) return grupo;
        }
        return null;
    }

    private Bitmap getImageByOfertaId(int oferta_id){

            Bitmap bitmap = getBitmapFromMemCache(oferta_id); // Get cached Bitmap

            if(bitmap == null){
                Runtime runtime = Runtime.getRuntime();
                long maxMemory=runtime.maxMemory();
                long usedMemory=runtime.totalMemory() - runtime.freeMemory();
                long availableMemory=maxMemory-usedMemory;

                if(availableMemory > maxMemory*0.1) {
                    // If can load the bitmap on RAM
                    if (mBitmapCache.size() < mBitmapCache.maxSize()) {

                        // Fetching image
                        String imageQuery = Oferta.getOfertaImage(oferta_id);
                        Cursor imageCursor = mdb.rawQuery(imageQuery, null);
                        imageCursor.moveToFirst();
                        byte[] byteArray = imageCursor.getBlob(imageCursor.getColumnIndex(Oferta.KEY_IMAGEN));
                        imageCursor.close();

                        if (byteArray != null) {
                            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            addBitmapToMemoryCache(oferta_id, bitmap);  //Cache bitmap
                        }
                    }
                }
                else{
                    Log.d("maxMem: " + maxMemory + " availableMem: " + availableMemory, " FF");
                }
            }

            return bitmap;
    }

    public List <Carta> getCartaList(){
        return mCartaList;
    }

    public List<Idioma> getIdiomaList() {
        return mIdiomaList;
    }

    public int getLanguageSelected(){
        return mLanguageSelected;
    }

    public long updateOferta(Oferta oferta){
        // Update choosen offers
        mMyChoosenCard.updateOferta(oferta);

        mdb = mDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Oferta.KEY_ESFAVORITO, oferta.getEsFavorito());

        mdb.update(Oferta.TABLE_OFERTA, values, Oferta.KEY_OFERTAID + "=" + oferta.getId(), null);

        int cartaID = oferta.getCartaFK();
        values = new ContentValues();
        values.put(Oferta.KEY_VECESPARACOMPRAR, oferta.getVecesParaComprar());
        long result = mdb.update(Oferta.TABLE_OFERTA_CARTA_GRUPO, values,
                Oferta.KEY_OFERTAFK + "=" + oferta.getId() + " and " + Oferta.KEY_CARTAFK + "=" + cartaID
                        + " and " + Oferta.KEY_GRUPOFK + "=" + oferta.getGrupoFK(),
                null);
        Log.d("loz", "updateOferta: " + oferta.getId() + " carta: " + cartaID +
                " vecesParaComprar: " + oferta.getVecesParaComprar() + " rows afected:" + result);
        mdb.close();
        return result;
    }

    public Grupo getGrupoById(int grupoId){
        return mGrupoMap.get(grupoId);
    }

    public void updateCardTotalPay(Carta carta, double newAmount){
        mTotalToPayAllCards += (newAmount - carta.getTotalPay());
        carta.setTotalPay(newAmount);
    }

    public double getTotalToPayAllCards() {
        return mTotalToPayAllCards;
    }
}
