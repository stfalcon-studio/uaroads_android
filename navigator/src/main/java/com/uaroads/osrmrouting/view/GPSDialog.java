package com.uaroads.osrmrouting.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.uaroads.osrmrouting.R;

public class GPSDialog extends AppCompatDialogFragment {

    private OnDialogDismissCallback dialogDismissCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity == null) return;

        dialogDismissCallback = (OnDialogDismissCallback) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false)
                .setMessage(getString(R.string.text_gps_is_disabled))
                .setPositiveButton(android.R.string.yes, onYesClick)
                .setNegativeButton(android.R.string.no, onNoClick);


        return builder.create();
    }

    private DialogInterface.OnClickListener onYesClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (dialogDismissCallback != null) dialogDismissCallback.onAcceptEnableGPS();
            getDialog().dismiss();
        }
    };

    private DialogInterface.OnClickListener onNoClick = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            getDialog().dismiss();
        }
    };

    public interface OnDialogDismissCallback {
        void onAcceptEnableGPS();
    }
}
