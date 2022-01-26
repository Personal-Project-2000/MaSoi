package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.R;
import com.personal_game.masoi.databinding.ItemMainBinding;
import com.personal_game.masoi.object.RoomObject;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private final List<RoomObject> roomList;
    private final Context context;
    private final MainListeners mainListeners;

    public MainAdapter(List<RoomObject> roomList, Context context, MainListeners mainListeners){
        this.roomList = roomList;
        this.context = context;
        this.mainListeners = mainListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMainBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(roomList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemMainBinding binding;

        public ViewHolder(@NonNull ItemMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(RoomObject roomObject) {
            if(roomObject.getPass().equals("")){
                binding.imgPass.setImageResource(R.drawable.unlock);
            }else{
                binding.imgPass.setImageResource(R.drawable.ic_baseline_lock_24);
            }

            binding.txtCode.setText(roomObject.getNumber());
            binding.txtQuantity.setText("Số lượng: "+roomObject.getSl());

            binding.layoutMain.setOnClickListener(v -> {
                if(roomObject.getPass().equals(""))
                    mainListeners.onClickUnLock(roomObject);
                else
                    mainListeners.onClickLock(roomObject);
            });
        }
    }

    public interface MainListeners {
        void onClickLock(RoomObject roomObject);
        void onClickUnLock(RoomObject roomObject);
    }
}
