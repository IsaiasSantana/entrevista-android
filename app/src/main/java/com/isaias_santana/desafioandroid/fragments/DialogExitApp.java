package com.isaias_santana.desafioandroid.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.isaias_santana.desafioandroid.R;


/**
 * @author Isaías Santana
 * @since 02/04/17.
 */


public class DialogExitApp extends DialogFragment
{

    public interface NoticeDialogListener
    {
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    private NoticeDialogListener mListener;


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof Activity)
        {
            try
            {
                 mListener = (NoticeDialogListener)  context;
            }
            catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString()+" Deve implementar NoticeDialogListener");
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.exit_app)
                .setPositiveButton(R.string.exit_app_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // FIRE ZE MISSILES!
                        mListener.onDialogPositiveClick(DialogExitApp.this);
                    }
                })
                .setNegativeButton(R.string.exit_app_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(DialogExitApp.this);
                       // activity.finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
