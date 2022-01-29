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
    private PlayerObject1 mine;
    private String Tk;
    private BaiObject bai;
    private VoteDialog voteDialog ;
    private AdvocateDialog advocateDialog;
    private Timer timer = new Timer();
    private int code1 = 0;
    private long timeNext = new Timestamp(System.currentTimeMillis()).getTime()/1000;
    private boolean isAllow = false;
    //dành cho người có chức năng phù thủy
    private int typePotion = 1;
    //dành cho người có chức năng cupid, thổi sáo
    private int amount = 0;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Timestamp ts = new Timestamp(System.currentTimeMillis());

            if(ts.getTime()/1000 == timeNext) {
                switch (code1) {
                    case 0:
                        activityPlayBinding.txtCall.setText("Sói");
                        break;
                    case 1:
                        activityPlayBinding.txtCall.setText("Tiên Tri");
                        break;
                    case 2:
                        activityPlayBinding.txtCall.setText("Bảo Vệ");
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
//                            activityPlayBinding.txtCall.setText("Sói");
//                            break;
//                        case 2:
//                            activityPlayBinding.txtCall.setText("Tiên Tri");
//                            break;
//                        case 3:
//                            activityPlayBinding.txtCall.setText("Bảo Vệ");
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
        mine = (PlayerObject1) intent.getSerializableExtra("mine");
        Tk = intent.getStringExtra("Tk");
        bai = (BaiObject) intent.getSerializableExtra("bai");

        for(PlayerObject1 player : playerListTemp){
            playerList.add(new PlayerObject2(player.getTk(), player.getName(), player.getImg()));
        }

        if(playerList.size() >= 12) {
            VoteDialog dialog = new VoteDialog(PlayActivity.this, 1, "Bạn muốn chọn A làm tộc trưởng?", playerListTemp);

            dialog.show();
            dialog.getWindow().setLayout(700, 1200);
            dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
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

            //Phù Thủy
            if(bai.getId().equals("61ea0f9dc550781bbe59b365")){
                activityPlayBinding.imgHelp.setImageResource(R.drawable.ic_potion_help);
                activityPlayBinding.imgDie.setImageResource(R.drawable.ic_potion_die);
            }
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
            }, "Bạn muốn rời khỏi phòng?");

            dialog.show();
            dialog.getWindow().setLayout(600, 250);
        });

        activityPlayBinding.imgGia.setOnClickListener(v -> {
            AdvocateDialog dialog = new AdvocateDialog(this, new AdvocateDialog.AdvocateListeners() {
                @Override
                public void onClickYes() {

                }

                @Override
                public void onClickNo() {

                }
            });

            dialog.show();
            dialog.getWindow().setLayout(700, 1200);
            dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
        });

        activityPlayBinding.imgLove.setOnClickListener(v -> {

            ResultDialog dialog = new ResultDialog(this, new ResultDialog.PlayerWinListeners() {
                @Override
                public void onClickContinue() {
                    StoryDialog dialog1 = new StoryDialog(PlayActivity.this, new StoryDialog.StoryListeners() {
                        @Override
                        public void onClickContinue() {
                            Intent intent = new Intent(PlayActivity.this, WaitActivity.class);
                            startActivity(intent);
                        }
                    });

                    dialog1.show();
                    dialog1.getWindow().setLayout(700, 1150);
                    dialog1.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
                }
            });

            dialog.show();
            dialog.getWindow().setLayout(650, 1100);
            dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
        });
    }

    private void setPlayer(){
        pLayerWithPlayAdapter = new PLayerWithPlayAdapter(playerList, getApplication(), new PLayerWithPlayAdapter.PlayerListeners() {
            @Override
            public void onClick(PlayerObject2 player) {
                if(isAllow) {
                    switch (bai.getId()) {
                        //Sói
                        case "61e675a18f96beab7215afee": {
                            SetupSocket.wolf(mine.getTk(), player.getTk(), room.getId());

                            isAllow = false;
                        }
                        break;
                        //Tiên tri
                        case "61ea0f7ec550781bbe59b363": {
                            SetupSocket.prophesy(player.getTk(), room.getId());

                            isAllow = false;
                        }
                        break;
                        //Bảo vệ
                        case "61ea0f8ec550781bbe59b364": {
                            SetupSocket.guard(player.getTk(), room.getId());

                            isAllow = false;
                        }
                        break;
                        //Phù Thủy
                        case "61ea0f9dc550781bbe59b365": {
                            SetupSocket.witch(mine.getTk(), player.getTk(), typePotion, room.getId());
                            if (typePotion == 1) {
                                typePotion = 2;
                            } else {
                                typePotion = 1;

                                isAllow = false;
                            }
                        }
                        break;
                        //Thợ săn
                        case "61ea0fabc550781bbe59b366": {
                            SetupSocket.hunter(player.getTk(), room.getId());

                            isAllow = false;
                        }
                        break;
                        //cupid
                        case "61ea0fbec550781bbe59b367": {
                            SetupSocket.cupid(player.getTk(), room.getId());
                            amount++;
                            if (amount == 1) {
                                Toast.makeText(PlayActivity.this, "Chọn thêm 1 người nữa", Toast.LENGTH_SHORT).show();
                            }else{
                                isAllow = false;
                            }
                        }
                        break;
                        //Thổi sáo
                        case "61ea100ec550781bbe59b36a": {
                            SetupSocket.flute(player.getTk(), room.getId());
                            amount++;
                            if (amount == 1) {
                                Toast.makeText(PlayActivity.this, "Chọn thêm 1 người nữa", Toast.LENGTH_SHORT).show();
                            }else{
                                isAllow = false;
                                amount = 0;
                            }
                        }
                        break;
                    }
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
                Toast.makeText(getApplication(), "Đuổi thất bại", Toast.LENGTH_SHORT).show();
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
                    List<PlayerDie> playerDies = new ArrayList<>();
                    try {
                        JSONArray playerDie = data.optJSONArray("playerDie");

                        if(playerDie.length() > 0) {
                            for (int i = 0; i < playerDie.length(); i++) {
                                JSONObject player = playerDie.getJSONObject(i);

                                String user = player.optString("user");
                                int position = takePosition(user);
                                playerDies.add(new PlayerDie(user, room.getId(), playerList.get(position).getName(), playerList.get(position).getImg()));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(baiId == bai.getId()){
                        isAllow = true;
                    }else{
                        isAllow = false;
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
                    String user = data.optString("user");
                    String message = data.optString("message");

                    int position = takePosition(user);

                    Toast.makeText(getApplication(), playerList.get(position).getTk()+" "+message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PlayActivity.this, "Bạn đã trở thành sói", Toast.LENGTH_SHORT).show();
                        bai.setId("61e675a18f96beab7215afee");
                        bai.setName("Sói");
                        GetBai("61e675a18f96beab7215afee");
                    }
                }
            });
        }
    };

    private void GetBai(String baiId){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_BaiInfo> call = serviceAPI_lib.BaiInfo(baiId);
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
                    bai.setDescription("Lỗi load dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Message_BaiInfo> call, Throwable t) {

            }
        });
    }
}