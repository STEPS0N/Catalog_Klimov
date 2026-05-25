package com.example.catalog_klimov.presentations.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.catalog_klimov.domains.models.Сategory;

import com.example.catalog_klimov.R;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Сategory> categories;
    int selectedPosition = 0;

    public CategoryAdapter(Context context, ArrayList<Сategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Сategory category = categories.get(position);

        int BackgroundResource = selectedPosition == position ?
                R.color.category_active : R.color.category_disabled;
        int BackgroundColor = context.getResources().getColor(BackgroundResource, context.getTheme());

        int TextResource = selectedPosition == position ?
                R.color.white : R.color.black;
        int TextColor = context.getResources().getColor(TextResource, context.getTheme());

        holder.tvName.setText(category.name);
        holder.clParent.setBackgroundTintList(ColorStateList.valueOf(BackgroundColor));
        holder.tvName.setTextColor(TextColor);

        holder.clParent.setOnClickListener(v -> {
            notifyItemChanged(selectedPosition);
            selectedPosition = position;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName;
            ConstraintLayout clParent;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
                clParent = itemView.findViewById(R.id.clParent);
            }
    }
}
