package com.bonaro.restaurant;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by lozano on 05/04/17.
 * All rights reserved
 */
public class AboutusFragment extends DialogFragment {

    private AboutusFragment mSelf;
    private boolean mReadMoreOpened;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSelf = this;
        mReadMoreOpened = false;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.about_us, null);

//        TextView tx = (TextView)view.findViewById(R.id.txtTitle);
//        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/CASBANTN.TTF");
//        tx.setTypeface(custom_font);

//        final Button btnReadMore = (Button) view.findViewById(R.id.btnReadMore);
        final TextView txtDescription = (TextView) view.findViewById(R.id.txtDescripcion);
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

//        btnReadMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!mReadMoreOpened){
//                    txtDescription.setMaxLines(Integer.MAX_VALUE);
//                    btnReadMore.setCompoundDrawablesWithIntrinsicBounds( R.drawable.arriba, 0, 0, 0);
//                }
//                else{
//                    txtDescription.setMaxLines(5);
//                    btnReadMore.setCompoundDrawablesWithIntrinsicBounds( R.drawable.abajo, 0, 0, 0);
//                }
//                mReadMoreOpened = !mReadMoreOpened;
//            }
//        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelf.dismiss();
            }
        });

        AlertDialog resultDialog = builder.create();
        resultDialog.setView(view, 0, 0, 0, 0);
        return resultDialog;
    }

}