package com.shalom.itai.theservantexperience.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Utils.Constants;

import java.util.concurrent.Callable;

/**
 * Created by Itai on 28/04/2017.
 */

public class MyAlertDialogFragment extends DialogFragment {
    public static MyAlertDialogFragment newInstance(int title, String posButton, String negButton, String name ) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("posButton",posButton);
        args.putString("negButton",negButton);
        args.putString("name",name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String posButton= getArguments().getString("posButton");
        String negButton= getArguments().getString("negButton");
        final String name= getArguments().getString("name");
        setCancelable(false);
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_favorite_black_48dp)
                .setTitle(title)
                .setPositiveButton(posButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((DialogCaller) getActivity()).doPositive();
                            }
                        })
                .setNegativeButton(negButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                    ((DialogCaller) getActivity()).doNegative();
                                }
                        }).create();
    }
}

