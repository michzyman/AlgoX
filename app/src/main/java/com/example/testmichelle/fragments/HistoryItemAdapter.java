package com.example.testmichelle.fragments;

import com.example.testmichelle.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView history_stock;
        public TextView history_transactiontype;
        public TextView history_amount;
        public TextView history_date;
        public TextView history_algorithm;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            history_stock = (TextView) itemView.findViewById(R.id.history_stock);
            history_transactiontype = (TextView) itemView.findViewById(R.id.history_transactiontype);
            history_amount = (TextView) itemView.findViewById(R.id.history_amount);
            history_date = (TextView) itemView.findViewById(R.id.history_date);
            history_algorithm = (TextView) itemView.findViewById(R.id.history_algorithm);
        }
    }

    private ArrayList<ArrayList<Object>> historyData;

    // Pass in the contact array into the constructor
    public HistoryItemAdapter(ArrayList<ArrayList<Object>> historyData) {
        this.historyData = historyData;
    }

    @Override
    public HistoryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(HistoryItemAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        ArrayList<Object> dataItem = historyData.get(position);

        // Set item views based on your views and data model
        TextView history_stock = holder.history_stock;
        history_stock.setText(dataItem.get(1).toString());

        TextView history_transactiontype = holder.history_transactiontype;
        history_transactiontype.setText(dataItem.get(2).toString());

        TextView history_amount = holder.history_amount;
        history_amount.setText(dataItem.get(3).toString());

        TextView history_date = holder.history_date;
        history_date.setText(dataItem.get(5).toString());

        TextView history_algorithm = holder.history_algorithm;
        history_algorithm.setText(dataItem.get(0).toString());

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return historyData.size();
    }
}