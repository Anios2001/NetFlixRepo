package com.example.netflix.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.Modal.Category;
import com.example.netflix.Modal.CategoryItem;
import com.example.netflix.R;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> implements Filterable {
   Context context;
   List<Category> categoryList;
   List<Category> fullList;

    public SearchRecyclerAdapter(Context context,List<Category> categoryList) {
        this.categoryList = categoryList;
        this.context= context;
    }

    private Filter exampleFilter= new Filter() {
       @Override
       protected FilterResults performFiltering(CharSequence charSequence) {
           List<Category> categories= new ArrayList<>();
           if(charSequence==null || charSequence.length()==0){
               categories.addAll(categoryList);
           }
           else
           {
               String input= charSequence.toString().toLowerCase().trim();
               for(Category i: categoryList){
                   if(i.getCategoryTitle().toLowerCase().contains(input)){
                       categories.add(i);
                   }
               }
           }
           FilterResults filterResults= new FilterResults();
           filterResults.values= categories;
           return filterResults;
       }

       @Override
       protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            categoryList.clear();
            categoryList.addAll((List)filterResults.values);
            notifyDataSetChanged();
       }
   };
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRecyclerAdapter.SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.searchrecyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
       setSearchItemList(holder.itemRecycler,categoryList.get(position).getCategoryItemList());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemRecycler;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            itemRecycler= itemView.findViewById(R.id.search_recycler);
        }

    }
    public void setSearchItemList(RecyclerView recyclerView,List<CategoryItem> categoryItemList){
        ItemRecyclerAdapter itemRecyclerAdapter= new ItemRecyclerAdapter(context,categoryItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(itemRecyclerAdapter);
    }

}
