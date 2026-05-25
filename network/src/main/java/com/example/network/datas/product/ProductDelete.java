package com.example.network.datas.product;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

public class ProductDelete extends MyAsyncTask {
    String token;
    Integer id;

    public ProductDelete(String token, Integer id, MyResponseCallback callback) {
        super(callback);
        this.token = token;
        this.id = id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/delete")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.DELETE)
                    .header("Content-type", "multipart/form-data")
                    .header("token", token)
                    .data("id", String.valueOf(id))
                    .execute();
            return response.statusCode() == 200 ?
                    response.body() :
                    "Error: " + response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
