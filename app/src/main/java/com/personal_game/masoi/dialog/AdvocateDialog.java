package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.personal_game.masoi.R;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.databinding.LayoutAdvocateBinding;
import com.personal_game.masoi.databinding.LayoutVoteBinding;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.PlayerObject2;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdvocateDialog extends Dialog {
    public Activity c;
    private LayoutAdvocateBinding layoutAdvocateBinding;
    private PlayerAdapter1 playerAdapter;
    private AdvocateListeners advocateListeners;
    private List<PlayerObject1> playerList;
    private PlayerObject2 mine;

    public AdvocateDialog(Activity a, List<PlayerObject1> playerList, AdvocateListeners advocateListeners, PlayerObject2 mine) {
        super(a);
        this.c = a;
        this.advocateListeners = advocateListeners;
        this.playerList = playerList;
        this.mine = mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutAdvocateBinding = LayoutAdvocateBinding.inflate(getLayoutInflater());
        View view = layoutAdvocateBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setPlayer();
        setListeners();

        if(!mine.isDie()) {
            if (playerList.get(0) != null) {
                if(playerList.get(0).getTk().equals(mine.getTk())){
                    layoutAdvocateBinding.layoutUserAdvocate.setVisibility(View.INVISIBLE);
                    layoutAdvocateBinding.txtWrite.setVisibility(View.VISIBLE);
                }

                Picasso.Builder builder = new Picasso.Builder(c);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        layoutAdvocateBinding.imgMain.setImageResource(R.drawable.logo);
                    }
                });
                Picasso pic = builder.build();
                pic.load(playerList.get(0).getImg()).into(layoutAdvocateBinding.imgMain);

                layoutAdvocateBinding.txtName.setText(playerList.get(0).getName());
            }
        }else{
            layoutAdvocateBinding.layoutUserAdvocate.setVisibility(View.INVISIBLE);
        }
    }

    private void setPlayer(){
        playerAdapter = new PlayerAdapter1(playerList, c, 1, new PlayerAdapter1.PlayerListeners() {
            @Override
            public void onClick(PlayerObject1 player) {

            }
        });
        layoutAdvocateBinding.rclPlayer.setAdapter(playerAdapter);
    }

    private void setListeners(){
        layoutAdvocateBinding.imgYes.setOnClickListener(v -> {
            advocateListeners.onClickYes(playerList.get(0));
            dismiss();
        });
        layoutAdvocateBinding.imgNo.setOnClickListener(v -> {
            advocateListeners.onClickNo(playerList.get(0));
            dismiss();
        });
    }

    public interface AdvocateListeners{
        void onClickYes(PlayerObject1 player);
        void onClickNo(PlayerObject1 player);
    }
}
