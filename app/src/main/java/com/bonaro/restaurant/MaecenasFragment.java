package com.bonaro.restaurant;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by lozano on 30/03/17.
 * All rights reserved
 */
public class MaecenasFragment extends DialogFragment {

    private MaecenasFragment mSelf;
    private ActivityComs mActivityComs;
    private Context mContext;

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

        View view = inflater.inflate(R.layout.maecenas, null);

        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);

        //Close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelf.dismiss();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActivityComs != null)
                    mActivityComs.enableInviteFriends();
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.putExtra("address", getString(R.string.phone_number));
                smsIntent.putExtra("sms_body", getString(R.string.maecenas_request));
//                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                try {
                    startActivity(smsIntent);
                } catch (Exception e){
                    Log.d("SMS error", "");
                }
            }
        });

        AlertDialog resultDialog = builder.create();
        resultDialog.setView(view, 0, 0, 0, 0);
        return resultDialog;
    }


}