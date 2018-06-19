package com.bonaro.restaurant;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bonaro.restaurant.Models.Idioma;

import java.util.List;

/**
 * Created by lozano on 04/04/17.
 * All rights reserved
 */
public class SettingsFragment extends DialogFragment {

    private SettingsFragment mSelf;
    private ActivityComs mActivityComs;
    private Context mContext;
    private RadioGroup mRadioGroup;

    private List<Idioma> mIdiomaList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mSelf = this;
        mActivityComs = (ActivityComs) context;
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
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mIdiomaList = Controller.getInstance().getIdiomaList();
        int languageSelected = Controller.getInstance().getLanguageSelected();

        View view = inflater.inflate(R.layout.settings, null);

        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelf.dismiss();
            }
        });

        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        for(Idioma idioma : mIdiomaList){
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(idioma.getNombre());
            radioButton.setId(idioma.getId());
            if(idioma.getId() == languageSelected)
                radioButton.setChecked(true);
            mRadioGroup.addView(radioButton);
        }


        AlertDialog resultDialog = builder.create();
        resultDialog.setView(view, 0, 0, 0, 0);
        return resultDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        int id = mRadioGroup.getCheckedRadioButtonId();

        if(id == -1) return;

        // Write SharedPreferences
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.shared_language), id);
        editor.apply();

        Controller.getInstance().changeLanguage(id);

        if(mActivityComs == null) return;
        mActivityComs.updateSectionAdapter();
    }

}