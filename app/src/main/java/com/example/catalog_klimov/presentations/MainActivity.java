package com.example.catalog_klimov.presentations;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalog_klimov.R;
import com.example.catalog_klimov.datas.CategoryContext;
import com.example.catalog_klimov.presentations.adapters.CategoryAdapter;
import com.example.catalog_klimov.presentations.utils.BottomSheetHelper;
import com.example.catalog_klimov.presentations.utils.ProgressDialogHelper;
import com.example.network.datas.basket.BasketCreate;
import com.example.network.datas.basket.BasketUpdate;
import com.example.network.datas.product.ProductGet;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Basket;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BthCustom;
import com.example.uicomponents.button.BtnSmall;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import kotlin.text.UStringsKt;

public class MainActivity extends AppCompatActivity {
    RecyclerView llCategory;
    LinearLayout llProducts;
    EditText etSearch;
    private ArrayList<Product> allProducts;
    ProgressDialogHelper progressDialogHelper;
    String Token = "7c8fdde2-cc31-44bc-9643-582434e35925";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        llCategory = findViewById(R.id.llCategory);
        llProducts = findViewById(R.id.llProducts);
        etSearch = findViewById(R.id.etSearch);

        CategoryAdapter categoryAdapter = new CategoryAdapter(this, CategoryContext.allCategory());
        llCategory.setAdapter(categoryAdapter);

        progressDialogHelper = new ProgressDialogHelper(this);
        RequestProductGet();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                Search(query);
            }
        });


    }

    public void RequestProductGet() {
        progressDialogHelper.progressDialog.show();

        ProductGet RequestProductGet = new ProductGet(
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("PRODUCTS GET", result);

                        ArrayList<Product> Products = new GsonBuilder().create().fromJson(
                                result, new TypeToken<ArrayList<Product>>(){}.getType()
                        );
                        CreateProduct(Products);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("PRODUCTS GET", error);
                    }
                }
        );
        RequestProductGet.execute();
    }

    public void BasketCreate(Product product, BthCustom btnAdd) {
        progressDialogHelper.progressDialog.show();

        BasketCreate RequestBasketCreate = new BasketCreate(
                Token,
                new Basket(product.id, 0),
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET CREATE", result);
                        ChangeBtnState(btnAdd, true);
                        progressDialogHelper.progressDialog.hide();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("BASKET CREATE", error);
                        progressDialogHelper.progressDialog.hide();
                    }
                }
        );
        RequestBasketCreate.execute();
    }

    public void BasketUpdate(Product product, BthCustom btnAdd) {
        progressDialogHelper.progressDialog.show();

        BasketUpdate RequestBasketUpdate = new BasketUpdate(
                Token,
                new Basket(product.id, 0),
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("BASKET UPDATE", result);
                        ChangeBtnState(btnAdd, false);
                        progressDialogHelper.progressDialog.hide();
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("BASKET UPDATE", error);
                        progressDialogHelper.progressDialog.hide();
                    }
                }
        );
        RequestBasketUpdate.execute();
    }

    public void ChangeBtnState(BthCustom btnAdd, boolean isBasket) {
        if (isBasket) btnAdd.init("Убрать", BthCustom.TypeButton.SECONDARY);
        else btnAdd.init("Добавить", BthCustom.TypeButton.PRIMARY);
    }

    public void Search(String text) {
        llProducts.removeAllViews();

        for (Product product : allProducts) {
            if (product.name.toLowerCase().contains(text.toLowerCase())){
                Fill(product);
            }
        }
    }

    public void Fill(Product product){
        View item = LayoutInflater.from(this).inflate(R.layout.item_product, llProducts, false);

        TextView tvName = item.findViewById(R.id.tvName);
        TextView tvCategory = item.findViewById(R.id.tvCategory);
        TextView tvPrice = item.findViewById(R.id.tvPrice);
        BtnSmall btnAdd = item.findViewById(R.id.btnAdd);

        String[] NameCategory = new String[] { "Мужское", "Женское", "Unisex" };

        tvName.setText(product.name);
        if (product.gender >= 0 && product.gender <= 2)
            tvCategory.setText(NameCategory[product.gender]);
        else
            tvCategory.setText("Неизвестно");
        tvPrice.setText(product.price + "₽");

        if (btnAdd.Btn != null) {
            ChangeBtnState(btnAdd, false);
        }
        btnAdd.init("Добавить", BtnSmall.TypeButton.PRIMARY);

        item.setOnClickListener(v -> {
            BottomSheetHelper.Create(this, this, product, btnAdd, progressDialogHelper);
        });

        btnAdd.Btn.setOnClickListener(v -> {
            if (btnAdd.Btn.getText().toString().equals("Добавить"))
                BasketCreate(product, btnAdd);
            else
                BasketUpdate(product, btnAdd);
        });

        llProducts.addView(item);
    }

    public void CreateProduct(ArrayList<Product> products) {
        this.allProducts = products;
        llProducts.removeAllViews();

        String[] NameCategory = new String[] { "Мужское", "Женское", "Unisex" };

        for (Product product : products) {
            View item = LayoutInflater.from(this).inflate(R.layout.item_product,
                    llProducts, false);

            TextView tvName = item.findViewById(R.id.tvName);
            TextView tvCategory = item.findViewById(R.id.tvCategory);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            BtnSmall btnAdd = item.findViewById(R.id.btnAdd);

            tvName.setText(product.name);

            if (product.gender >= 0 && product.gender <= 2)
                tvCategory.setText(NameCategory[product.gender]);
            else
                tvCategory.setText("Неизвестно");

            tvPrice.setText(product.price + "₽");

            if (btnAdd.Btn != null) {
                ChangeBtnState(btnAdd, false);
            }

            btnAdd.init("Добавить", BtnSmall.TypeButton.PRIMARY);

            item.setOnClickListener(v -> {
                BottomSheetHelper.Create(this, this, product, btnAdd, progressDialogHelper);
            });

            btnAdd.Btn.setOnClickListener(v -> {
                if (btnAdd.Btn.getText() == "Добавить")
                    BasketCreate(product, btnAdd);
                else
                    BasketUpdate(product, btnAdd);
            });

            llProducts.addView(item);
        }
        progressDialogHelper.progressDialog.hide();
    }
}