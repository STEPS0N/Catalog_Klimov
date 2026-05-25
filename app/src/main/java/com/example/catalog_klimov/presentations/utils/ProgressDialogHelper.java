package com.example.catalog_klimov.presentations.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.catalog_klimov.R;

public class ProgressDialogHelper {
    public AlertDialog progressDialog;

    public ProgressDialogHelper(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

        builder.setView(dialogView);

        builder.setCancelable(false);

        progressDialog = builder.create();
    }
}
