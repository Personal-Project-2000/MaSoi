package com.personal_game.masoi.dialog;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.personal_game.masoi.R;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Request_ChangPass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassDialog extends Dialog implements android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button exit, change;
    public ProgressBar progressBar;
    public EditText inputPass;
    public EditText inputPassNew;
    public EditText inputPassNew1;
    private String Tk;

    public PassDialog(Activity a, String Tk) {
        super(a);
        this.c = a;
        this.Tk = Tk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_changpass);

        exit = (Button) findViewById(R.id.btnExit);
        change = (Button) findViewById(R.id.btnChangePass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputPass = (EditText) findViewById(R.id.inputPassOld);
        inputPassNew = (EditText) findViewById(R.id.inputPassNew);
        inputPassNew1 = (EditText) findViewById(R.id.inputPass);
        exit.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExit:
                dismiss();
                break;
            case R.id.btnChangePass:
                if(check()){
                    ChangePass(Tk, inputPass.getText()+"", inputPassNew.getText()+"");
                }
                break;
            default:
                break;
        }
    }

    private void loading(boolean Loading) {
        if (Loading) {
            progressBar.setVisibility(View.VISIBLE);
            change.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
        }
    }

    private boolean check(){
        if(inputPassNew.getText().length() < 8){
            Toast.makeText(c, "Mật khẩu ít hơn 8 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(inputPassNew.getText()+"").equals(inputPassNew1.getText()+"")){
            Toast.makeText(c, "Nhập lại mật khẩu đã bị sai", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void ChangePass(String Tk, String PassOld, String PassNew){
        loading(true);

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> changPass = serviceAPI_lib.ChangePass(new Request_ChangPass(Tk, PassOld, PassNew));
        changPass.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                Toast.makeText(c, response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                if(response.body().getStatus1() == 1){
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                loading(false);
            }
        });
    }
}
