package com.personal_game.masoi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.R;
import com.personal_game.masoi.databinding.ItemPlayerBinding;
import com.personal_game.masoi.databinding.ItemPlayerwithplayBinding;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.PlayerObject2;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PLayerWithPlayAdapter extends RecyclerView.Adapter<PLayerWithPlayAdapter.ViewHolder>{
    private final List<PlayerObject2> playerList;
    private final Context context;
    private final PlayerListeners playerListeners;

    public PLayerWithPlayAdapter(List<PlayerObject2> playerList, Context context, PlayerListeners playerListeners){
        this.playerList = playerList;
        this.context = context;
        this.playerListeners = playerListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PLayerWithPlayAdapter.ViewHolder(ItemPlayerwithplayBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(playerList.get(position));
    }

    @Override
    public int getItemCount() {
        return playerList != null ? playerList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemPlayerwithplayBinding binding;

        public ViewHolder(@NonNull ItemPlayerwithplayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(PlayerObject2 player) {
            if(player.getImg() != null){
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        binding.imgMain.setImageResource(R.drawable.logo);
                    }
                });
                Picasso pic = builder.build();
                pic.load(player.getImg()).into(binding.imgMain);
            }

            binding.txtName.setText(player.getName());

            if(player.isDie()){
                binding.layoutMain.setBackgroundResource(R.drawable.vien_gray);
            }else{
                binding.layoutMain.setBackgroundResource(R.drawable.vien1);
            }

            if(player.isDieNew()){
                binding.layoutMain.setBackgroundResource(R.drawable.vien_red);
            }else{
                binding.layoutMain.setBackgroundResource(R.drawable.vien1);
            }

            if(player.isHypnosis()){
                binding.imgSao.setImageResource(R.drawable.ic_music1);
            }else{
                binding.imgSao.setImageResource(R.drawable.ic_music);
            }

            if(player.isLove()){
                binding.imgLove.setImageResource(R.drawable.ic_love1);
            }else{
                binding.imgLove.setImageResource(R.drawable.ic_love);
            }

            if(player.isPatriarch()){
                binding.imgGia.setImageResource(R.drawable.ic_patriarch1);
            }else {
                binding.imgGia.setImageResource(R.drawable.ic_patriarch);
            }

            binding.layoutMain.setOnClickListener(v -> {
                playerListeners.onClick(player);
            });
        }
    }

    public interface PlayerListeners{
        void onClick(PlayerObject2 player);
    }
}
