package com.example.covidapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class layout_dialog extends AppCompatDialogFragment {

    EditText editTextState;
    EditText editTextDistrict;
    IntefaceDialogListener intefaceDialogListener;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_layout_dialog, null);
        builder.setView(view).setTitle("Search state and district")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String stateName = editTextState.getText().toString();
                        String districtName = editTextDistrict.getText().toString();
                        intefaceDialogListener.applyTexts(stateName, districtName);
                    }
                });
        editTextState = view.findViewById(R.id.enterState);
        editTextDistrict = view.findViewById(R.id.enterDistrict);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            intefaceDialogListener = (IntefaceDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface IntefaceDialogListener{
        void applyTexts(String state, String District);
    }
}
