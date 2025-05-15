package com.raaveinm.homepharmacy.domain;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.data.Item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Item> itemList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(Item item); }
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

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
        if (itemList != null && position >= 0 && position < itemList.size()) {
            Item item = itemList.get(position);
            holder.textViewItemName.setText(item.itemName);
            holder.textViewCompanyName.setText(item.companyName);
            holder.textViewItemQuantity.setText(String.valueOf(item.itemQuantity));
            holder.textViewItemPrice.setText(String.valueOf(item.itemPrice));

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });
        }
    }

    @Override
    public int getItemCount() { return itemList != null ? itemList.size() : 0; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName = itemView.findViewById(R.id.textViewItemName);
        TextView textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
        TextView textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);
        TextView textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);

        public ViewHolder(@NonNull View itemView) { super(itemView); }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void dataChanged(List<Item> newItemList) {
        this.itemList = (newItemList == null) ? new ArrayList<>() : new ArrayList<>(newItemList);
        notifyDataSetChanged();
    }

    public Item getItemAt(int position) {
        if (itemList != null && position >= 0 && position < itemList.size()) {
            return itemList.get(position);
        }
        return null;
    }
}