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
import com.example.catalog_klimov.presentations.adapters.NewsAdapter;
import com.example.catalog_klimov.presentations.utils.BottomSheetHelper;
import com.example.catalog_klimov.presentations.utils.ProgressDialogHelper;
import com.example.network.datas.basket.BasketCreate;
import com.example.network.datas.basket.BasketUpdate;
import com.example.network.datas.product.ProductGet;
import com.example.network.datas.stock.StockGet;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Basket;
import com.example.network.domains.models.Product;
import com.example.network.domains.models.Stock;
import com.example.uicomponents.button.BthCustom;
import com.example.uicomponents.button.BtnSmall;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView llCategory;
    RecyclerView llNews;
    LinearLayout llProducts;
    EditText etSearch;
    ArrayList<Product> Products = new ArrayList<>();
    ArrayList<Сategory> Categorys = new ArrayList<>();
    ArrayList<Stock> Stocks = new ArrayList<>();
    ProgressDialogHelper progressDialogHelper;
    String Token = "0cda6376-cf42-4632-be9b-4a4c52559a99";
    CategoryAdapter categoryAdapter;
    NewsAdapter newsAdapter;
    int currentGender = -1;

    CategoryAdapter.ionClickInterface CategoryClick = new CategoryAdapter.ionClickInterface() {
        @Override
        public void setClick(View view, int position) {
            Сategory selectCategory = Categorys.get(position);
            currentGender = selectCategory.id;
            filterByGender(currentGender);
            categoryAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        llCategory = findViewById(R.id.llCategory);
        llNews = findViewById(R.id.llNews);
        llProducts = findViewById(R.id.llProducts);
        etSearch = findViewById(R.id.etSearch);
        Categorys = CategoryContext.allCategory();

        Bundle arguments = getIntent().getExtras();
        Integer IdCategory = -1;
        if (arguments != null && arguments.containsKey("Category")) {
            IdCategory = Integer.valueOf(arguments.get("Category").toString());
            currentGender = IdCategory;
        }

        categoryAdapter = new CategoryAdapter(this, Categorys, CategoryClick);
        llCategory.setAdapter(categoryAdapter);

        progressDialogHelper = new ProgressDialogHelper(this);

        LoadStocks();

        RequestProductGet();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search(s.toString());
            }
        });
    }

    private void filterByGender(int genderId) {
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : Products) {
            if (genderId == -1 || p.gender == genderId) {
                filtered.add(p);
            }
        }
        llProducts.removeAllViews();
        for (Product product : filtered) {
            Fill(product);
        }
    }

    public void LoadStocks() {
        progressDialogHelper.progressDialog.show();

        StockGet RequestStockGet = new StockGet(
                new MyResponseCallback() {
                    @Override
                    public void onCompile(String result) {
                        Log.d("STOCKS GET", result);

                        Stocks = new GsonBuilder().create().fromJson(
                                result, new TypeToken<ArrayList<Stock>>(){}.getType()
                        );
                        newsAdapter = new NewsAdapter(MainActivity.this, Stocks);
                        llNews.setAdapter(newsAdapter);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("STOCKS GET", error);
                    }
                }
        );
        RequestStockGet.execute();
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

    public void Search(String text) {
        llProducts.removeAllViews();

        for (Product product : Products) {
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

        btnAdd.init("Добавить", BtnSmall.TypeButton.PRIMARY);

        final Product currentProduct = product;
        final BtnSmall currentBtn = btnAdd;

        btnAdd.Btn.setOnClickListener(v -> {
            if (currentBtn.Btn.getText() == "Добавить")
                BasketCreate(currentProduct, currentBtn);
            else
                BasketUpdate(currentProduct, currentBtn);
        });

        item.setOnClickListener(v -> {
            BottomSheetHelper.Create(this, this, product, btnAdd, progressDialogHelper);
        });

        llProducts.addView(item);
    }

    public void CreateProduct(ArrayList<Product> products) {
        this.Products = products;

        if (currentGender != -1) {
            filterByGender(currentGender);
        } else {
            llProducts.removeAllViews();
            String[] NameCategory = new String[] { "Мужское", "Женское", "Unisex" };
            for (Product product : products) {
                View item = LayoutInflater.from(this).inflate(R.layout.item_product, llProducts, false);
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
                    if (btnAdd.Btn.getText().toString().equals("Добавить"))
                        BasketCreate(product, btnAdd);
                    else
                        BasketUpdate(product, btnAdd);
                });
                llProducts.addView(item);
            }
        }
        progressDialogHelper.progressDialog.hide();
    }
}