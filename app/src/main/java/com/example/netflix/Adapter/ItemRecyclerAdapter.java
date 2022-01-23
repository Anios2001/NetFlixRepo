package com.example.netflix.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.MainScreens.MovieDetails;
import com.example.netflix.Modal.Category;
import com.example.netflix.Modal.CategoryItem;
import com.example.netflix.R;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
    Context context;
    List<CategoryItem> categoryItemList;
    List<CategoryItem> movieItemList;

    public ItemRecyclerAdapter(Context context, List<CategoryItem> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
        movieItemList= new ArrayList<>(categoryItemList);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Glide.with(context).load(categoryItemList.get(position).getImageUrl()).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(context, MovieDetails.class);
                i.putExtra("MOVIE_ID",categoryItemList.get(position).getId());
                i.putExtra("MOVIE_NAME",categoryItemList.get(position).getMovieName());
                i.putExtra("IMAGE_URL",categoryItemList.get(position).getImageUrl());
                i.putExtra("FILE_URL",categoryItemList.get(position).getFileUrl());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage= itemView.findViewById(R.id.itemImage);
        }


    }
}

