package com.bonaro.restaurant.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lozano on 21/01/17.
 */
public class Oferta implements Parcelable {
    public static final String TABLE_OFERTA = "Oferta";
    public static final String TABLE_OFERTA_LANG = "Oferta_idioma";
    public static final String TABLE_OFERTA_CARTA = "Oferta_carta";

    public static final String KEY_OFERTAID = "id";

    public static final String KEY_IDIOMAFK = "idioma_fk";
    public static final String KEY_GRUPOFK = "grupo_fk";
    public static final String KEY_CARTAFK = "carta_fk";

    public static final String KEY_PRECIO = "precio";
    public static final String KEY_IMAGEN = "imagen";
    public static final String KEY_ENERGIA = "energiaKca";
    public static final String KEY_PROTEINA = "proteinaG";
    public static final String KEY_GRASA = "grasaG";
    public static final String KEY_COLESTEROL = "colesterolMg";
    public static final String KEY_CARBOHIDRATOS = "carbohidratosG";
    public static final String KEY_FIBRA = "fibraG";
    public static final String KEY_VITA = "vitaminaAug";
    public static final String KEY_VITB6 = "vitaminaBseismg";
    public static final String KEY_VITB12 = "vitaminaBdoceUg";
    public static final String KEY_VITC = "vitaminaCmg";
    public static final String KEY_VITE = "vitaminaEmg";
    public static final String KEY_POTASIO = "potasioMg";
    public static final String KEY_HIERRO = "hierroMg";
    public static final String KEY_ESFAVORITO = "esFavorito";
    public static final String KEY_VECESPARACOMPRAR = "vecesParaComprar";

    //Table Oferta_idioma
    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_BIOPROPS = "biopropiedades";
    public static final String KEY_OFERTAFK = "oferta_fk";

    //Multilingual fields
    private String mNombre;
    private String mDescripcion;
    private String mBiopropiedades;

    private int mId;
    private int mGrupoFK;
    private int mCartaFK;
    private double mPrecio;
//    private Image mImage;
    private float mEnergiaKca;
    private float mProteinaG;
    private float mGrasaG;
    private float mColesterolMg;
    private float mCarbohidratosG;
    private float mFibraG;
    private float mVitAUg;
    private float mVitB6Mg;
    private float mVitB12Ug;
    private float mVitCMg;
    private float mVitEMg;
    private float mPotasioMg;
    private float mHierroMg;
    private int mEsFavorito;
    private int mVecesParaComprar;
    private Bitmap mImage;

    // Aux vars
    private int mIndexInCard;
    private int mIndexGroup;

    public Oferta(int id, String nombre, String descripcion, String biopropiedades, int grupoFK, int cartaFK,
                  double precio, float energiaKca,
                  float proteinaG, float grasaG, float colesterolMg,
                  float carbohidratosG, float fibraG, float vitAUg, float vitB6Mg,
                  float vitB12Ug, float vitCMg, float vitEMg, float potasioMg,
                  float hierroMg, int esFavorito, int vecesParaComprar, Bitmap image, int indexInCard) {
        mId = id;
        mNombre = nombre;
        mDescripcion = descripcion;
        mBiopropiedades = biopropiedades;
        mGrupoFK = grupoFK;
        mCartaFK = cartaFK;
        mPrecio = precio;
        mEnergiaKca = energiaKca;
        mProteinaG = proteinaG;
        mGrasaG = grasaG;
        mColesterolMg = colesterolMg;
        mCarbohidratosG = carbohidratosG;
        mFibraG = fibraG;
        mVitAUg = vitAUg;
        mVitB6Mg = vitB6Mg;
        mVitB12Ug = vitB12Ug;
        mVitCMg = vitCMg;
        mVitEMg = vitEMg;
        mPotasioMg = potasioMg;
        mHierroMg = hierroMg;
        mEsFavorito = esFavorito;
        mVecesParaComprar = vecesParaComprar;
        mImage = image;
        mIndexInCard = indexInCard;
    }

    protected Oferta(Parcel in) {
        mNombre = in.readString();
        mDescripcion = in.readString();
        mBiopropiedades = in.readString();
        mId = in.readInt();
        mGrupoFK = in.readInt();
        mCartaFK = in.readInt();
        mPrecio = in.readDouble();
        mEnergiaKca = in.readFloat();
        mProteinaG = in.readFloat();
        mGrasaG = in.readFloat();
        mColesterolMg = in.readFloat();
        mCarbohidratosG = in.readFloat();
        mFibraG = in.readFloat();
        mVitAUg = in.readFloat();
        mVitB6Mg = in.readFloat();
        mVitB12Ug = in.readFloat();
        mVitCMg = in.readFloat();
        mVitEMg = in.readFloat();
        mPotasioMg = in.readFloat();
        mHierroMg = in.readFloat();
        mEsFavorito = in.readInt();
        mVecesParaComprar = in.readInt();
        mImage = in.readParcelable(Bitmap.class.getClassLoader());
        mIndexInCard = in.readInt();
    }

    public static final Creator<Oferta> CREATOR = new Creator<Oferta>() {
        @Override
        public Oferta createFromParcel(Parcel in) {
            return new Oferta(in);
        }

        @Override
        public Oferta[] newArray(int size) {
            return new Oferta[size];
        }
    };

    public String getNombre() {
        return mNombre;
    }

    public String getDescripcion() {
        return mDescripcion;
    }

    public String getBiopropiedades() {
        return mBiopropiedades;
    }

    public int getId() {
        return mId;
    }

    public int getGrupoFK() {
        return mGrupoFK;
    }

    public int getCartaFK() {
        return mCartaFK;
    }

    public double getPrecio() {
        return mPrecio;
    }

    public float getEnergiaKca() {
        return mEnergiaKca;
    }

    public float getProteinaG() {
        return mProteinaG;
    }

    public float getGrasaG() {
        return mGrasaG;
    }

    public float getColesterolMg() {
        return mColesterolMg;
    }

    public float getCarbohidratosG() {
        return mCarbohidratosG;
    }

    public float getFibraG() {
        return mFibraG;
    }

    public float getVitAUg() {
        return mVitAUg;
    }

    public float getVitB6Mg() {
        return mVitB6Mg;
    }

    public float getVitB12Ug() {
        return mVitB12Ug;
    }

    public float getVitCMg() {
        return mVitCMg;
    }

    public float getVitEMg() {
        return mVitEMg;
    }

    public float getPotasioMg() {
        return mPotasioMg;
    }

    public float getHierroMg() {
        return mHierroMg;
    }

    public int getEsFavorito() {
        return mEsFavorito;
    }

    public void setEsFavorito(int esFavorito) {
        this.mEsFavorito = esFavorito;
    }

    public void setVecesParaComprar(int vecesParaComprar) {
        this.mVecesParaComprar = vecesParaComprar;
    }

    public int getVecesParaComprar() {
        return mVecesParaComprar;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public int getIndexInCard() {
        return mIndexInCard;
    }

//    public static String getSelectQuery(int idioma_id, int carta_id, int grupo_id){
//        return  "select * from Oferta inner join Oferta_idioma on Oferta.id = Oferta_idioma.oferta_fk" +
//                " where Oferta_idioma.idioma_fk = " + idioma_id +
//                " and Oferta.carta_fk = " + carta_id +
//                " and Oferta.grupo_fk = " + grupo_id +
//                " group by Oferta_idioma.oferta_fk";
//    }

    public static String getSelectQuery(int idioma_id, int carta_id, int grupo_id){
        return  "select o.*, oi.descripcion, oi.nombre, oi.biopropiedades, oc.vecesParaComprar\n" +
                "from oferta o inner join oferta_idioma oi on o.id = oi.oferta_fk\n" +
                "inner join Oferta_carta oc on oc.oferta_fk = o.id\n" +
                "inner join carta c on c.id = oc.carta_fk\n" +
                "where c.id =" + carta_id + "\n" +
                "and oi.idioma_fk = " + idioma_id + "\n" +
                "and o.grupo_fk = " + grupo_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Multilingual fields
        dest.writeString(mNombre);
        dest.writeString(mDescripcion);
        dest.writeString(mBiopropiedades);

        dest.writeInt(mId);
        dest.writeInt(mGrupoFK);
        dest.writeInt(mCartaFK);
        dest.writeDouble(mPrecio);

        dest.writeFloat(mEnergiaKca);
        dest.writeFloat(mProteinaG);
        dest.writeFloat(mGrasaG);
        dest.writeFloat(mColesterolMg);
        dest.writeFloat(mCarbohidratosG);
        dest.writeFloat(mFibraG);
        dest.writeFloat(mVitAUg);
        dest.writeFloat(mVitB6Mg);
        dest.writeFloat(mVitB12Ug);
        dest.writeFloat(mVitCMg);
        dest.writeFloat(mVitEMg);
        dest.writeFloat(mPotasioMg);
        dest.writeFloat(mHierroMg);
        dest.writeInt(mEsFavorito);
        dest.writeInt(mVecesParaComprar);

        dest.writeParcelable(mImage, flags);

        // Aux vars
        dest.writeInt(mIndexInCard);


    }
}
