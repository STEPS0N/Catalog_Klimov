package com.example.catalog_klimov.datas;

import java.util.ArrayList;
import com.example.catalog_klimov.domains.models.Сategory;
import com.example.catalog_klimov.presentations.adapters.CategoryAdapter;

public class CategoryContext {
    public static ArrayList<Сategory> allCategory() {
        ArrayList<Сategory> categories = new ArrayList<>();

        categories.add(new Сategory(-1, "Все"));
        categories.add(new Сategory(0, "Мужское"));
        categories.add(new Сategory(1, "Женское"));
        categories.add(new Сategory(2, "Unisex"));

        return categories;
    }

    public static ArrayList<Сategory> GetCategory(Integer idGender) {
        if (idGender == 0) return allCategory();

        ArrayList<Сategory> Categorys = new ArrayList<>();

        for (Сategory category : allCategory()){
            if (category.id.equals(idGender))
                Categorys.add(category);
        }
        return Categorys;
    }
}
