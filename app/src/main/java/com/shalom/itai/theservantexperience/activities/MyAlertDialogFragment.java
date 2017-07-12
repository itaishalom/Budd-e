package com.shalom.itai.theservantexperience.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 28/04/2017.
 */

public class MyAlertDialogFragment extends DialogFragment {
    public static MyAlertDialogFragment newInstance(int title, String posButton, String negButton, String name,String msg) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("posButton", posButton);
        args.putString("negButton", negButton);
        args.putString("name", name);
        if(msg !=null)
            args.putString("Message",msg);
        frag.setArguments(args);
        return frag;
    }

    public static MyAlertDialogFragment newInstance(String title, String posButton, String negButton, String name) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("posButton", posButton);
        args.putString("negButton", negButton);
        args.putString("name", name);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Object myTitle = getArguments().get("title");
        AlertDialog.Builder ab;
        if (myTitle instanceof Integer) {
            int title = Integer.parseInt(myTitle.toString());
            ab = new AlertDialog.Builder(getActivity()).setTitle(title);
        } else {
            String title = (myTitle.toString());
            ab = new AlertDialog.Builder(getActivity()).setTitle(title);
        }
        if(getArguments().get("Message") !=null)
            ab.setMessage(getArguments().get("Message").toString());
        //  int title = getArguments().getInt("title");
        String posButton = getArguments().getString("posButton");
        String negButton = getArguments().getString("negButton");
        setCancelable(false);
        return ab.setIcon(R.drawable.rel_friend)
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

