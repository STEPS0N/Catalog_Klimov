package com.example.catalog_klimov.presentations.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catalog_klimov.presentations.MainActivity;
import com.example.catalog_klimov.R;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BthBig;
import com.example.uicomponents.button.BthCustom;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class BottomSheetHelper {

    public static void Create(
            Context context,
            Activity activity,
            Product product,
            BthCustom btnCardAdd,
            ProgressDialogHelper progressDialogHelper) {
        progressDialogHelper.progressDialog.show();
        BottomSheetDialog dialog = new BottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.item_project_description, null);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvDescritption = view.findViewById(R.id.tvDescription);
        TextView tvExpenditure = view.findViewById(R.id.tvExpenditure);
        View btnClose = view.findViewById(R.id.btnClose);
        BthBig btnAdd = view.findViewById(R.id.btnAdd);
        ImageView image = view.findViewById(R.id.imageView);

        if (product.img != null) {
            Picasso
                    .with(context)
                    .load(Settings.URL + "/img/" + product.img)
                    .into(image);
        }

        tvName.setText(product.name);
        tvDescritption.setText(product.description);
        tvExpenditure.setText(product.expenditure);

        btnAdd.init("Добавить за " + product.price + "₽", BthCustom.TypeButton.PRIMARY);

        btnClose.setOnClickListener(v -> {
            dialog.hide();
        });

        btnAdd.Btn.setOnClickListener(v -> {
            ((MainActivity)activity).BasketCreate(product, btnCardAdd);
            dialog.hide();
        });

        dialog.setContentView(view);
        progressDialogHelper.progressDialog.hide();
        dialog.show();
    }
}
