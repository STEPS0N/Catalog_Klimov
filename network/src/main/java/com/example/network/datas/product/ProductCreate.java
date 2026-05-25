package com.example.network.datas.product;

import android.content.Context;
import android.net.Uri;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;
import com.google.gson.GsonBuilder;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProductCreate extends MyAsyncTask {
    Context context;
    String token;
    Product product;
    Uri uri;

    public ProductCreate(Context context, String token, Product product, Uri uri, MyResponseCallback callback) {
        super(callback);

        this.context = context;
        this.token = token;
        this.product = product;
        this.uri = uri;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(this.uri);
            File tempFile = createTempFileFromServer(inputStream);

            Map<String, String> params = new HashMap<>();
            params.put("Name", this.product.name);
            params.put("Description", this.product.description);
            params.put("Gender", this.product.gender.toString());
            params.put("Expenditure", this.product.expenditure);
            params.put("Price", this.product.price.toString());

            Connection.Response response = Jsoup.connect(Settings.URL + "/api/product/create")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .header("Content-type", "multipart/form-data")
                    .header("token", token)
                    .data(params)
                    .data("InputFile", tempFile.getName(), new FileInputStream(tempFile))
                    .execute();

            return response.statusCode() == 200 ? response.body() : "Error: " + response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public File createTempFileFromServer(InputStream inputStream) {
        try {
            File tempFile = File.createTempFile("upload_", ".jpg");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
