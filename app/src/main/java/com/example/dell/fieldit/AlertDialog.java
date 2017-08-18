package com.example.dell.fieldit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by galna21 on 17/08/2017.
 */

public class AlertDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        String message = getArguments().getString("message");
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Boolean shouldExit = getArguments().getBoolean("shouldExit");
                if (shouldExit) {
                    getActivity().finish();
                }
            }
        });
        return builder.create();
    }
}
