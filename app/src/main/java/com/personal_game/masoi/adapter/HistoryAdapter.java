package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemHistoryBinding;
import com.personal_game.masoi.databinding.ItemMainBinding;
import com.personal_game.masoi.object.HistoryObject;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private final List<HistoryObject> historyList;
    private final HistoryListeners historyListeners;
    private final Context context;

    public HistoryAdapter(List<HistoryObject> historyList, Context context, HistoryListeners historyListeners){
        this.historyList = historyList;
        this.historyListeners = historyListeners;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryAdapter.ViewHolder(ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.setData(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemHistoryBinding binding;

        public ViewHolder(@NonNull ItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(HistoryObject historyObject) {
            binding.txtTimeStart.setText(historyObject.getStartTime());
            binding.txtQuantity.setText("Số lượng: "+historyObject.getSl());

            binding.layoutMain.setOnClickListener(v -> {
                historyListeners.onClick(historyObject);
            });
        }
    }

    public interface HistoryListeners {
        void onClick(HistoryObject historyObject);
    }
}
