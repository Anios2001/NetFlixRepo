package com.example.netflix.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.Modal.Category;
import com.example.netflix.Modal.CategoryItem;
import com.example.netflix.R;

import java.util.List;

public class MainRecyclerAdapter  extends RecyclerView.Adapter<MainRecyclerAdapter.MainRecyclerViewHolder> {
    Context context;
    List<Category> categoryItemList;

    public MainRecyclerAdapter(Context context,List<Category> categoryItems) {
        this.context = context;
        this.categoryItemList=categoryItems;
    }

    @NonNull
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.mainrecyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewHolder holder, int position) {
       holder.textView.setText(categoryItemList.get(position).getCategoryTitle());
       setCategoryItemList(holder.recyclerView,categoryItemList.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerView;
        public MainRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView= itemView.findViewById(R.id.mainRecyclerText_id);
            recyclerView= itemView.findViewById(R.id.item_recycler);
        }
    }
    public void setCategoryItemList(RecyclerView recyclerView,List<CategoryItem> categoryItemList){
         ItemRecyclerAdapter itemRecyclerAdapter= new ItemRecyclerAdapter(context,categoryItemList);
         recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
         recyclerView.setAdapter(itemRecyclerAdapter);
    }
}
