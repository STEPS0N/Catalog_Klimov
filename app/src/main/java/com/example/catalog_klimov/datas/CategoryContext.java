package com.example.catalog_klimov.datas;

import java.util.ArrayList;
import com.example.catalog_klimov.domains.models.Сategory;

public class CategoryContext {
    public static ArrayList<Сategory> allCategory() {
        ArrayList<Сategory> categories = new ArrayList<>();

        categories.add(new Сategory(-1, "Все"));
        categories.add(new Сategory(0, "Мужское"));
        categories.add(new Сategory(1, "Женское"));
        categories.add(new Сategory(2, "Unisex"));

        return categories;
    }
}
