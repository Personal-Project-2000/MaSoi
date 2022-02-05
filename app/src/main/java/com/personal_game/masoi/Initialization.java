package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.object.Message_Adress;
import com.personal_game.masoi.socket.SetupSocket;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Initialization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Address();
    }

    private void Address(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_Adress> address = serviceAPI_lib.Address();
        address.enqueue(new Callback<Message_Adress>() {
            @Override
            public void onResponse(Call<Message_Adress> call, Response<Message_Adress> response) {
                if(response.body().getStatus1() == 1){
                    SetupSocket.uriLocal = response.body().getIp1();

                    Toast.makeText(getApplication(), "Đăng nhập để trải nghệm nào", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Initialization.this, SignInActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message_Adress> call, Throwable t) {
                Toast.makeText(getApplication(), "Kiểm tra lại kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}