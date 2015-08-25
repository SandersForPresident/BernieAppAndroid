package com.spielpark.steve.bernieapp.bernrate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.spielpark.steve.bernieapp.R;
import com.spielpark.steve.bernieapp.dagger.Dagger;
import com.spielpark.steve.bernieapp.persistence.DataStore;

import javax.inject.Inject;

/**
 * Created by AndrewOrobator on 8/25/15.
 */
public class BernRateDialogFragment extends DialogFragment {
    @Inject
    DataStore mDataStore;
    private CheckBox mCheckBox;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dagger.applicationComponent(getContext()).inject(this);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialogue_checkbox, null);
        mCheckBox = (CheckBox) view.findViewById(R.id.dia_checkbox);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.bern_rate_intro_dialog)
                .setTitle(R.string.bern_rate_welcome)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        persistChoice();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        persistChoice();
                    }
                })
                .setView(view);
        return builder.create();
    }

    public void persistChoice() {
        if (mCheckBox.isChecked()) {
            mDataStore.stopShowingBernRateDialog();
        }
    }
}
