package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityRegistrationBinding;
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.UserObject;

import java.io.IOException;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding activityRegistrationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegistrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = activityRegistrationBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        setListeners();
    }

    private void setListeners(){
        activityRegistrationBinding.btnSignIn.setOnClickListener(v ->{
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        });

        activityRegistrationBinding.btnRegistration.setOnClickListener(v -> {
            if(check()) {
                registration(activityRegistrationBinding.inputTK.getText() + "",
                        activityRegistrationBinding.inputFullName.getText() + "",
                        activityRegistrationBinding.inputPass.getText() + "");
            }
        });
    }

    private boolean check(){
        if(activityRegistrationBinding.inputPass.getText().length() < 8){
            Toast.makeText(getApplication(), "Mật khẩu ít hơn 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(activityRegistrationBinding.inputPass.getText()+"").equals(activityRegistrationBinding.inputPass1.getText()+"")){
            Toast.makeText(getApplication(), "Nhập lại mật khẩu đã bị sai", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void loading(boolean Loading) {
        if (Loading) {
            activityRegistrationBinding.progressBar.setVisibility(View.VISIBLE);
            activityRegistrationBinding.btnRegistration.setVisibility(View.GONE);
        } else {
            activityRegistrationBinding.progressBar.setVisibility(View.GONE);
            activityRegistrationBinding.btnRegistration.setVisibility(View.VISIBLE);
        }
    }

    private void registration(String Tk, String FullName, String Pass){
        loading(true);

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> regis = serviceAPI_lib.Registration(new UserObject(Tk, FullName, Pass));
        regis.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();

                if(response.body().getStatus1() == 1){
                    Intent intent = new Intent(getApplication(), SignInActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                loading(false);
                Toast.makeText(getApplication(), "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}