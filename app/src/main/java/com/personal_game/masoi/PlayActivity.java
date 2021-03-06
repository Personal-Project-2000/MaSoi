package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PLayerWithPlayAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityPlayBinding;
import com.personal_game.masoi.dialog.AdvocateDialog;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.ResultDialog;
import com.personal_game.masoi.dialog.StoryDialog;
import com.personal_game.masoi.dialog.VoteDialog;
import com.personal_game.masoi.object.BaiObject;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_BaiInfo;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.PlayerObject2;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.socket.SetupSocket;
import com.personal_game.masoi.socket.model.PlayerDie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends AppCompatActivity {
    private ActivityPlayBinding activityPlayBinding;
    private PLayerWithPlayAdapter pLayerWithPlayAdapter;
    private RoomObject room;
    private List<PlayerObject2> playerList = new ArrayList<>();
    private PlayerObject2 mine;
    private String Tk;
    private BaiObject bai;
    private VoteDialog voteDialog ;
    private AdvocateDialog advocateDialog;
    private Timer timer = new Timer();
    private int code1 = 0;
    private long timeNext = new Timestamp(System.currentTimeMillis()).getTime()/1000;
    private boolean isAllow = false;
    //d??nh cho ng?????i c?? ch???c n??ng ph?? th???y
    private int typePotion = 1;
    //d??nh cho ng?????i c?? ch???c n??ng cupid, th???i s??o
    private int amount = 0;
    //d??nh cho ph?? th???y
    private boolean isPositionDie = false, isPositionHelp = false;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Timestamp ts = new Timestamp(System.currentTimeMillis());

            if(ts.getTime()/1000 == timeNext) {
                switch (code1) {
                    case 0:
                        activityPlayBinding.txtCall.setText("S??i");
                        break;
                    case 1:
                        activityPlayBinding.txtCall.setText("Ti??n Tri");
                        break;
                    case 2:
                        activityPlayBinding.txtCall.setText("B???o V???");
                        break;
                }

                if(code1 == 4)
                    code1 = 0;
                else
                    code1++;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayBinding = ActivityPlayBinding.inflate(getLayoutInflater());
        View view = activityPlayBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();

//        new CountDownTimer(1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//                // Used for formatting digit to be in 2 digits only
////                NumberFormat f = new DecimalFormat("00");
////                long hour = (millisUntilFinished / 3600000) % 24;
////                long min = (millisUntilFinished / 60000) % 60;
////                long sec = (millisUntilFinished / 1000) % 60;
//
//                if(range == 10) {
//                    switch (code1) {
//                        case 1:
//                            activityPlayBinding.txtCall.setText("S??i");
//                            break;
//                        case 2:
//                            activityPlayBinding.txtCall.setText("Ti??n Tri");
//                            break;
//                        case 3:
//                            activityPlayBinding.txtCall.setText("B???o V???");
//                            break;
//                    }
//
//                    if(code1 == 4)
//                        code1 = 0;
//                    else
//                        code1++;
//                }
//                if(range == 10)
//                    range = 0;
//                else
//                    range ++;
//
//                Toast.makeText(getApplication(), range, Toast.LENGTH_SHORT).show();
//
//            }
//            // When the task is over it will print 00:00:00 there
//            public void onFinish() {
//
//            }
//        }.start();
    }

    private void init(){
        Intent intent = getIntent();
        room = (RoomObject) intent.getSerializableExtra("room");
        List<PlayerObject1> playerListTemp = (List<PlayerObject1>) intent.getSerializableExtra("playerList");
        PlayerObject1 player1 = (PlayerObject1) intent.getSerializableExtra("mine");
        Tk = intent.getStringExtra("Tk");
        bai = (BaiObject) intent.getSerializableExtra("bai");

        mine = new PlayerObject2(player1.getTk(), player1.getName(), player1.getImg(), player1.isBoss());

        for(PlayerObject1 player : playerListTemp){
            playerList.add(new PlayerObject2(player.getTk(), player.getName(), player.getImg(), player.isBoss()));
        }

        if(playerList.size() >= 12) {
//            VoteDialog dialog = new VoteDialog(PlayActivity.this, 1, "B???n mu???n ch???n A l??m t???c tr?????ng?", playerListTemp);
//
//            dialog.show();
//            dialog.getWindow().setLayout(700, 1200);
//            dialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
        }

        if(bai != null) {
            if (bai.getImg() != null) {
                Picasso.Builder builder = new Picasso.Builder(this);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        activityPlayBinding.imgBai.setImageResource(R.drawable.logo);
                    }
                });
                Picasso pic = builder.build();
                pic.load(bai.getImg()).into(activityPlayBinding.imgBai);
            }
            activityPlayBinding.txtName.setText(bai.getName());
            activityPlayBinding.txtDes.setText(bai.getDescription());

            //Ph?? Th???y
            if(bai.getId().equals("61ea0f9dc550781bbe59b365")){
                activityPlayBinding.imgHelp.setImageResource(R.drawable.ic_potion_help);
                activityPlayBinding.imgDie.setImageResource(R.drawable.ic_potion_die);
                isPositionHelp = true;
                isPositionDie = true;
            }

//            if(bai.getName().equals("T??nh Y??u") || bai.getName().equals("Th??? S??n")){
//                activityPlayBinding.btnContinue.setVisibility(View.INVISIBLE);
//            }
        }

        setListeners();
        setPlayer();
        socket();
        timer.schedule(timerTask, 1000);
    }

    private void setListeners(){
        activityPlayBinding.btnBack.setOnClickListener(v -> {
            ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                @Override
                public void onClickYes() {
                    exitRoom();
                }
            }, "B???n mu???n r???i kh???i ph??ng?");

            dialog.show();
            //dialog.getWindow().setLayout(600, 250);
        });

        activityPlayBinding.imgGia.setOnClickListener(v -> {
//            AdvocateDialog dialog = new AdvocateDialog(this, new AdvocateDialog.AdvocateListeners() {
//                @Override
//                public void onClickYes() {
//
//                }
//
//                @Override
//                public void onClickNo() {
//
//                }
//            });
//
//            dialog.show();
//            dialog.getWindow().setLayout(700, 1200);
//            dialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
        });

        activityPlayBinding.imgLove.setOnClickListener(v -> {

//            ResultDialog dialog = new ResultDialog(this, new ResultDialog.PlayerWinListeners() {
//                @Override
//                public void onClickContinue() {
//                    StoryDialog dialog1 = new StoryDialog(PlayActivity.this, new StoryDialog.StoryListeners() {
//                        @Override
//                        public void onClickContinue() {
//                            Intent intent = new Intent(PlayActivity.this, WaitActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                    dialog1.show();
//                    dialog1.getWindow().setLayout(700, 1150);
//                    dialog1.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
//                }
//            });
//
//            dialog.show();
//            dialog.getWindow().setLayout(650, 1100);
//            dialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
        });

        activityPlayBinding.btnContinue.setOnClickListener(v -> {
            if(isAllow) {
                switch (bai.getId()) {
                    //S??i
                    case "61e675a18f96beab7215afee": {
                        ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                            @Override
                            public void onClickYes() {
                                SetupSocket.wolf(mine.getTk(), "", room.getId());
                                isAllow = false;
                            }
                        }, "B???n kh??ng mu???n gi???t ai?");

                        dialog.show();
                        //dialog.getWindow().setLayout(600, 300);
                    }
                    break;
                    //Ti??n tri
                    case "61ea0f7ec550781bbe59b363": {
                        SetupSocket.prophesy("", room.getId(), mine.getTk());
                        isAllow = false;
                    }
                    break;
                    //B???o v???
                    case "61ea0f8ec550781bbe59b364": {
                        ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                            @Override
                            public void onClickYes() {
                                SetupSocket.guard("", room.getId(), mine.getTk());
                                isAllow = false;
                            }
                        }, "B???n kh??ng mu???n b???o v??? ai?");

                        dialog.show();
                        //dialog.getWindow().setLayout(600, 300);
                    }
                    break;
                    //Ph?? Th???y
                    case "61ea0f9dc550781bbe59b365": {
                        if (typePotion == 1) {
                            ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                @Override
                                public void onClickYes() {
                                    SetupSocket.witch(mine.getTk(), "", typePotion, room.getId());
                                    typePotion = 2;
                                }
                            }, "B???n kh??ng mu???n c???u ai?");

                            dialog.show();
                            dialog.getWindow().setLayout(600, 300);

                            Toast.makeText(getApplication(), "B???n mu???n gi???t ai n??o?", Toast.LENGTH_SHORT).show();
                            //tr??? v??? gi?? tr??? b???n ?????u
                            for(int i = 0; i < playerList.size(); i++){
                                playerList.get(i).setDieNew(false);
                            }
                            pLayerWithPlayAdapter.notifyDataSetChanged();
                        } else {
                            ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                @Override
                                public void onClickYes() {
                                    SetupSocket.witch(mine.getTk(), "", typePotion, room.getId());
                                    typePotion = 1;
                                    isAllow = false;
                                }
                            }, "B???n kh??ng mu???n gi???t ai?");
                        }
                    }
                    break;
                    //Th???i s??o
                    case "61ea100ec550781bbe59b36a": {
                        SetupSocket.flute("", room.getId(), mine.getTk());
                        isAllow = false;
                    }
                    break;
                }
            }
        });
    }

    private void setPlayer(){
        pLayerWithPlayAdapter = new PLayerWithPlayAdapter(playerList, getApplication(), new PLayerWithPlayAdapter.PlayerListeners() {
            @Override
            public void onClick(PlayerObject2 player) {
                if(isAllow) {
                    if(!mine.isDie()) {
                        if(!player.isDie()) {
                            switch (bai.getId()) {
                                //S??i
                                case "61e675a18f96beab7215afee": {
                                    ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                        @Override
                                        public void onClickYes() {
                                            SetupSocket.wolf(mine.getTk(), player.getTk(), room.getId());
                                            isAllow = false;
                                        }
                                    }, "B???n mu???n gi???t " + player.getName() + "?");

                                    dialog.show();
                                    //dialog.getWindow().setLayout(600, 300);
                                }
                                break;
                                //Ti??n tri
                                case "61ea0f7ec550781bbe59b363": {
                                    ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                        @Override
                                        public void onClickYes() {
                                            SetupSocket.prophesy(player.getTk(), room.getId(), mine.getTk());
                                            isAllow = false;
                                        }
                                    }, "B???n mu???n xem " + player.getName() + " c?? ph???i s??i kh??ng?");

                                    dialog.show();
                                    //dialog.getWindow().setLayout(600, 300);
                                }
                                break;
                                //B???o v???
                                case "61ea0f8ec550781bbe59b364": {
                                    ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                        @Override
                                        public void onClickYes() {
                                            SetupSocket.guard(player.getTk(), room.getId(), mine.getTk());
                                            isAllow = false;
                                        }
                                    }, "B???n b???o v??? " + player.getName() + " ????m nay c?? ph???i s??i kh??ng?");

                                    dialog.show();
                                    //dialog.getWindow().setLayout(600, 300);
                                }
                                break;
                                //Ph?? Th???y
                                case "61ea0f9dc550781bbe59b365": {
                                    ConfirmDialog dialog;

                                    if (typePotion == 1) {
                                        if(player.isDieNew()) {
                                            if (isPositionHelp) {
                                                dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                                    @Override
                                                    public void onClickYes() {
                                                        SetupSocket.witch(mine.getTk(), player.getTk(), typePotion, room.getId());
                                                        activityPlayBinding.imgHelp.setImageResource(R.drawable.ic_potion);
                                                        isPositionHelp = false;
                                                        typePotion = 2;

                                                        //tr??? v??? gi?? tr??? b???n ?????u
                                                        for(int i = 0; i < playerList.size(); i++){
                                                            playerList.get(i).setDieNew(false);
                                                        }
                                                        pLayerWithPlayAdapter.notifyDataSetChanged();

                                                        Toast.makeText(getApplication(), "B???n mu???n gi???t ai", Toast.LENGTH_SHORT).show();
                                                    }
                                                }, "B???n mu???n c???u " + player.getName() + " c?? ph???i kh??ng?");

                                                dialog.show();
                                                //dialog.getWindow().setLayout(600, 300);
                                            } else {
                                                Toast.makeText(getApplication(), "B???n ???? s??? d???ng b??nh c???u, chi c?? th??? b???m b??? qua", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getApplication(), "Ng?????i n??y c??n s???ng, b???n h??y ch???n ng?????i ch???t ????? c???u ho???c b???m b??? qu??", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (isPositionDie) {
                                                dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                                    @Override
                                                    public void onClickYes() {
                                                        SetupSocket.witch(mine.getTk(), player.getTk(), typePotion, room.getId());
                                                        activityPlayBinding.imgDie.setImageResource(R.drawable.ic_potion);
                                                        isPositionDie = false;
                                                        typePotion = 1;
                                                        isAllow = false;
                                                    }
                                                }, "B???n mu???n gi???t " + player.getName() + " c?? ph???i kh??ng?");

                                                dialog.show();
                                                //dialog.getWindow().setLayout(600, 300);
                                        } else {
                                            Toast.makeText(getApplication(), "B???n ???? s??? d???ng b??nh gi???t, chi c?? th??? b???m b??? qua", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                break;
                                //Th??? s??n
                                case "61ea0fabc550781bbe59b366": {
                                    if(player.getTk().equals(mine.getTk())){
                                        Toast.makeText(getApplication(), "B???n kh??ng th??? b???n ch??nh m??nh ???????c, h??y ch???n ng?????i kh??c", Toast.LENGTH_SHORT).show();
                                    }else {
                                        ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                            @Override
                                            public void onClickYes() {
                                                SetupSocket.hunter(player.getTk(), room.getId(), mine.getTk());
                                                isAllow = false;
                                            }
                                        }, "B???n mu???n b???n " + player.getName() + " c?? ph???i kh??ng?");

                                        dialog.show();
                                        dialog.getWindow().setLayout(600, 300);
                                    }
                                }
                                break;
                                //cupid
                                case "61ea0fbec550781bbe59b367": {
                                    ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                        @Override
                                        public void onClickYes() {
                                            SetupSocket.cupid(player.getTk(), room.getId(), mine.getTk());
                                            amount++;
                                            if (amount == 1) {
                                                Toast.makeText(PlayActivity.this, "Ch???n th??m 1 ng?????i n???a", Toast.LENGTH_SHORT).show();
                                            } else {
                                                isAllow = false;
                                            }
                                        }
                                    }, "B???n mu???n k???t duy??n " + player.getName() + " c?? ph???i kh??ng?");

                                    dialog.show();
                                    dialog.getWindow().setLayout(600, 300);
                                }
                                break;
                                //Th???i s??o
                                case "61ea100ec550781bbe59b36a": {
                                    if(player.getTk().equals(mine.getTk())){
                                        Toast.makeText(getApplication(), "B???n kh??ng th??? th??i ni??m ch??nh m??nh ???????c, h??y ch???n ng?????i kh??c", Toast.LENGTH_SHORT).show();
                                    }else {
                                        ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                                            @Override
                                            public void onClickYes() {
                                                SetupSocket.flute(player.getTk(), room.getId(), mine.getTk());
                                                amount++;
                                                if (amount == 1) {
                                                    Toast.makeText(PlayActivity.this, "Ch???n th??m 1 ng?????i n???a", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    isAllow = false;
                                                    amount = 0;
                                                }
                                            }
                                        }, "B???n mu???n th??i ni??n " + player.getName() + " c?? ph???i kh??ng?");

                                        dialog.show();
                                        dialog.getWindow().setLayout(600, 300);
                                    }
                                }
                                break;
                            }
                        }else{
                            Toast.makeText(getApplication(), "Ng?????i n??y ???? ch???t", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplication(), "B???n ???? ch???t n??n ch??? c?? th??? b???m b??? qua", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplication(), "Ch??a ?????n l?????t c???a b???n", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activityPlayBinding.rclPlayer.setAdapter(pLayerWithPlayAdapter);
    }

    private void exitRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.ExitRoom(room.getId(), Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    if(mine.isBoss()){
                        SetupSocket.exitRoom(Tk, room.getId(), 3);
                    }else{
                        SetupSocket.exitRoom(Tk, room.getId(), 1);
                    }

                    Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "??u???i th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int takePosition(String name){
        for (int i = 0; i < playerList.size(); i ++) {
            if(playerList.get(i).getTk().equals(name) ){
                return i;
            }
        }
        return 0;
    }

    private void socket(){
        SetupSocket.mSocket.on("S_exitroom", exitRoom);
        SetupSocket.mSocket.on("S_call", call);
        SetupSocket.mSocket.on("S_cupid", cupid);
        SetupSocket.mSocket.on("S_flute", flute);
        SetupSocket.mSocket.on("S_playerVote", advocate);
        SetupSocket.mSocket.on("S_changeFunc", changeFunc);
        SetupSocket.mSocket.on("S_prophesy", prophesy);
    }

    private final Emitter.Listener exitRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String user = data.optString("user");

                    int position = takePosition(user);

                    if(playerList.size() > 0) {
                        playerList.remove(position);
                        pLayerWithPlayAdapter.notifyItemRemoved(position);
                    }
                }
            });
        }
    };

    private final Emitter.Listener call = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String baiName = data.optString("baiName");
                    String baiId = data.optString("baiId");

                    activityPlayBinding.txtCall.setText(baiName);

                    if(baiId.equals(bai.getId())){
                        isAllow = true;
                        if(!mine.isDie())
                            activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien_yellow);
                        else
                            activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien_yellowgray);
                    }else{
                        isAllow = false;
                        if(!mine.isDie())
                            activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien);
                        else
                            activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien_gray);
                    }

                    List<PlayerObject1> playerDies = new ArrayList<>();
                    List<PlayerObject1> playerAliveList = new ArrayList<>();
                    List<PlayerObject1> playerVotes = new ArrayList<>();
                    try {
                        JSONArray playerVote = data.optJSONArray("playerVote");

                        if (playerVote != null) {
                            for (int i = 0; i < playerVote.length(); i++) {
                                JSONObject player = playerVote.getJSONObject(i);

                                String user = player.optString("user");
                                int position = takePosition(user);
                                playerVotes.add(new PlayerObject1(user, playerList.get(position).getName(), playerList.get(position).getImg(), false, false));
                            }
                        }

                        JSONArray playerDie = data.optJSONArray("playerDie");

                        if (playerDie != null) {
                            for (int i = 0; i < playerDie.length(); i++) {
                                JSONObject player = playerDie.getJSONObject(i);

                                String user = player.optString("user");
                                int position = takePosition(user);
                                playerDies.add(new PlayerObject1(user, playerList.get(position).getName(), playerList.get(position).getImg(), false, false));
                                //check b??i ph?? th???y
                                if(bai.getId().equals("61ea0f9dc550781bbe59b365") && baiId.equals(bai.getId())){
                                    playerList.get(position).setDieNew(true);
                                }else {
                                    if(baiId.equals("1")) {
                                        if(user.equals(mine.getTk())){
                                            mine.setDie(true);
                                            activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien_gray);
                                        }
                                        playerList.get(position).setDie(true);
                                    }
                                }
                                pLayerWithPlayAdapter.notifyItemChanged(position);
                            }
                        }

                        JSONArray playerAlive = data.optJSONArray("playerAlive");

                        if(playerAlive != null){
                            for (int i = 0; i < playerAlive.length(); i++) {
                                JSONObject playerAlive1 = playerAlive.getJSONObject(i);

                                String userAlive = playerAlive1.optString("user");
                                int position = takePosition(userAlive);
                                playerAliveList.add(new PlayerObject1(userAlive, playerList.get(position).getName(), playerList.get(position).getImg(), false, false));

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(baiId.equals("1")){
                        if(!mine.isDie()) {
                            voteDialog = new VoteDialog(PlayActivity.this, 2, "", playerAliveList, new VoteDialog.VoteListeners() {
                                @Override
                                public void onClickYes(PlayerObject1 player) {
                                    SetupSocket.vote(mine.getTk(), player.getTk(), room.getId());
                                }
                            });

                            voteDialog.show();
                            voteDialog.getWindow().setLayout(700, 1200);
                            voteDialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
                        }
                    }else if(baiId.equals("2")){
                        if(voteDialog != null)
                            voteDialog.dismiss();
                        advocateDialog = new AdvocateDialog(PlayActivity.this, playerVotes, new AdvocateDialog.AdvocateListeners() {
                            @Override
                            public void onClickYes(PlayerObject1 player) {
                                SetupSocket.advocate(mine.getTk(), room.getId(), 1);
                            }

                            @Override
                            public void onClickNo(PlayerObject1 player) {
                                SetupSocket.advocate(mine.getTk(), room.getId(), 2);
                            }
                        }, mine);

                        advocateDialog.show();
                        advocateDialog.getWindow().setLayout(700, 1200);
                        advocateDialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
                    }else{
                        if(advocateDialog != null) {
                            advocateDialog.dismiss();
                        }
                    }

                    if(baiId.equals("3")){
                        String notification = data.optString("notification");

                        ResultDialog dialog = new ResultDialog(PlayActivity.this, new ResultDialog.PlayerWinListeners() {
                            @Override
                            public void onClickContinue() {
                                StoryDialog dialog1 = new StoryDialog(PlayActivity.this, new StoryDialog.StoryListeners() {
                                    @Override
                                    public void onClickContinue() {
                                        Toast.makeText(getApplication(), "H??y b???t ?????u tr???n m???i n??o", Toast.LENGTH_SHORT).show();

                                        exitRoom();
                                    }
                                });

                                dialog1.show();
                                dialog1.getWindow().setLayout(700, 1150);
                                dialog1.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
                            }
                        }, notification);

                        dialog.show();
                        dialog.getWindow().setLayout(650, 1100);
                        dialog.setCanceledOnTouchOutside(false); //B???m ngo??i h???p tho???i kh??ng dismiss
                    }
                }
            });
        }
    };

    private final Emitter.Listener cupid = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityPlayBinding.imgLove.setImageResource(R.drawable.ic_love1);

                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONArray players = data.optJSONArray("playerList");

                        for(int i = 0; i < players.length(); i ++){
                            JSONObject player = players.getJSONObject(i);
                            String user = player.optString("user");

                            int position = takePosition(user);
                            playerList.get(position).setLove(true);
                            pLayerWithPlayAdapter.notifyItemChanged(position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private final Emitter.Listener flute = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activityPlayBinding.imgSao.setImageResource(R.drawable.ic_music1);

                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONArray players = data.optJSONArray("playerList");

                        for(int i = 0; i < players.length(); i ++){
                            JSONObject player = players.getJSONObject(i);
                            String user = player.optString("user");

                            int position = takePosition(user);
                            playerList.get(position).setHypnosis(true);
                            pLayerWithPlayAdapter.notifyItemChanged(position);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private final Emitter.Listener advocate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String user = data.optString("userDie");
                    String message = data.optString("message");
                    boolean isDie = data.optBoolean("isDie");

                    int position = takePosition(user);

                    if (user.equals(mine.getTk()) && isDie) {
                        playerList.get(position).setDie(true);
                        pLayerWithPlayAdapter.notifyItemChanged(position);

                        mine.setDie(true);
                        activityPlayBinding.txtCall.setBackgroundResource(R.drawable.vien_gray);
                    }

                    Toast.makeText(getApplication(), playerList.get(position).getName()+" "+message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private final Emitter.Listener changeFunc = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(bai.getId().equals("61ea0fd0c550781bbe59b368")){
                        Toast.makeText(PlayActivity.this, "B???n ???? tr??? th??nh s??i", Toast.LENGTH_SHORT).show();
                        bai.setId("61e675a18f96beab7215afee");
                        bai.setName("S??i");
                        GetBai("61e675a18f96beab7215afee");
                    }
                }
            });
        }
    };

    private final Emitter.Listener prophesy = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String answer = data.optString("answer");

                    Toast.makeText(getApplication(), answer, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    //d??nh cho l?? b??i n???a ng?????i n???a s??i
    private void GetBai(String baiId){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_BaiInfo> call = serviceAPI_lib.BaiInfo(baiId, mine.getTk(), "", "");
        call.enqueue(new Callback<Message_BaiInfo>() {
            @Override
            public void onResponse(Call<Message_BaiInfo> call, Response<Message_BaiInfo> response) {
                if(response.body().getStatus1() == 1 && response.body().getBaiInfo1() != null){
                    bai.setImg(response.body().getBaiInfo1().getImg());
                    bai.setDescription(response.body().getBaiInfo1().getDescription());
                    if (response.body().getBaiInfo1().getImg() != null) {
                        Picasso.Builder builder = new Picasso.Builder(getApplication());
                        builder.listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                activityPlayBinding.imgBai.setImageResource(R.drawable.logo);
                            }
                        });
                        Picasso pic = builder.build();
                        pic.load(response.body().getBaiInfo1().getImg()).into(activityPlayBinding.imgBai);
                    }
                    activityPlayBinding.txtDes.setText(bai.getDescription());
                }else{
                    bai.setDescription("L???i load d??? li???u");
                }
            }

            @Override
            public void onFailure(Call<Message_BaiInfo> call, Throwable t) {

            }
        });
    }
}