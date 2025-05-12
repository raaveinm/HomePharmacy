package com.raaveinm.homepharmacy.domain;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.data.Item;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Item> itemList;

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerviewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewItemName.setText(item.itemName);
        holder.textViewCompanyName.setText(item.companyName);
        holder.textViewItemQuantity.setText(String.valueOf(item.itemQuantity));
        holder.textViewItemPrice.setText(String.valueOf(item.itemPrice));
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName = itemView.findViewById(R.id.textViewItemName);
        TextView textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
        TextView textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
        TextView textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);

        public ViewHolder(@NonNull View itemView) { super(itemView); }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void dataChanged(List<Item> itemList) {
        this.itemList = itemList;
        itemList.addAll(Collections.unmodifiableList(itemList));
        notifyDataSetChanged();
    }
}
