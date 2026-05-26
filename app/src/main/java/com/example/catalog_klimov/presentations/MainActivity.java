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
import com.example.catalog_klimov.domains.models.Сategory;
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
    private ArrayList<Product> filteredProducts;
    ProgressDialogHelper progressDialogHelper;
    String Token = "16594ef1-1c9d-4ebe-bd9b-5a3c3a3bba9b";
    private int currentGenderFilter = -1;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        llCategory = findViewById(R.id.llCategory);
        llProducts = findViewById(R.id.llProducts);
        etSearch = findViewById(R.id.etSearch);

        ArrayList<Сategory> allCategories = CategoryContext.allCategory();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, allCategories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int position, Сategory category) {
                currentGenderFilter = category.id;
                applyFilters();
            }
        });
        llCategory.setAdapter(categoryAdapter);

        progressDialogHelper = new ProgressDialogHelper(this);
        RequestProductGet();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                applyFilters();
            }
        });
    }

    private void applyFilters() {
        if (allProducts == null) return;

        ArrayList<Product> filteredProducts = new ArrayList<>();

        for (Product product : allProducts) {
            boolean matchesGender = true;
            boolean matchesSearch = true;

            if (currentGenderFilter != -1) {
                matchesGender = (product.gender == currentGenderFilter);
            }

            if (!currentSearchQuery.isEmpty()) {
                matchesSearch = product.name.toLowerCase().contains(currentSearchQuery.toLowerCase());
            }

            if (matchesGender && matchesSearch) {
                filteredProducts.add(product);
            }
        }

        CreateProduct(filteredProducts);
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
        if (isBasket)
            btnAdd.init("Убрать", BthCustom.TypeButton.SECONDARY);
        else
            btnAdd.init("Добавить", BthCustom.TypeButton.PRIMARY);
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
            ChangeBtnState(btnAdd, false);

            if (product.gender >= 0 && product.gender <= 2)
                tvCategory.setText(NameCategory[product.gender]);
            else
                tvCategory.setText("Неизвестно");

            tvPrice.setText(product.price + "₽");

            item.setOnClickListener(v -> {
                BottomSheetHelper.Create(this, this, product, btnAdd, progressDialogHelper);
            });

            btnAdd.Btn.setOnClickListener(v -> {
                if (btnAdd.Btn.getText() == "Добавить")
                    BasketCreate(product, btnAdd);
                else
                    BasketUpdate(product, btnAdd);
            });

            String test = (String) btnAdd.Btn.getText();
            Log.d("Button", test);

            llProducts.addView(item);
        }
        progressDialogHelper.progressDialog.hide();
    }
}