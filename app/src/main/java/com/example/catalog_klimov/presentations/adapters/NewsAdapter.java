package com.example.catalog_klimov.presentations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalog_klimov.R;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Stock;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    Context context;
    ArrayList<Stock> stocks;

    public NewsAdapter(Context context, ArrayList<Stock> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        Stock stock = stocks.get(position);

        if (stock != null && stock.product != null) {
            holder.tvName.setText(stock.product.name);
            holder.tvPrice.setText(stock.price + " ₽");

            if (stock.product.img != null && !stock.product.img.isEmpty()) {
                String imageUrl = Settings.URL + "/img/" + stock.product.img;
                Picasso.with(context)
                        .load(imageUrl)
                        .into(holder.ivImage);
            }

        }
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPrice;
        ImageView ivImage;
        ConstraintLayout clStockNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            clStockNews = itemView.findViewById(R.id.clStockNews);
        }
    }
}
