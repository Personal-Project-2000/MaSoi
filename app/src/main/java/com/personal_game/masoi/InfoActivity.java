package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityInfoBinding;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.databinding.LayoutChangpassBinding;
import com.personal_game.masoi.dialog.PassDialog;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.UserObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding activityInfoBinding;
    private UserObject user;
    private String path, pathBack;
    private boolean checkImg = false;
    private boolean checkImgBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInfoBinding = ActivityInfoBinding.inflate(getLayoutInflater());
        View view = activityInfoBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        user = (UserObject) getIntent().getSerializableExtra("user");

        info();
        setListeners();
    }

    private void setListeners(){
        activityInfoBinding.btnBack.setOnClickListener(v -> {
            finish();
        });

        activityInfoBinding.btnChangePass.setOnClickListener(v -> {
            LayoutChangpassBinding layoutChangpassBinding;
            layoutChangpassBinding = LayoutChangpassBinding.inflate(getLayoutInflater());

            PassDialog dialog = new PassDialog(this, user.getTk());

            dialog.show();
            dialog.getWindow().setLayout(700, 550);
        });

        activityInfoBinding.btnUpImgMain.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            startActivityForResult(result, 10);
        });

        activityInfoBinding.btnUpImgBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            startActivityForResult(result, 11);
        });

        activityInfoBinding.btnSave.setOnClickListener(v -> {
            if(checkImg)
                upImg();
            if(checkImgBack)
                upImgBack();
            if(!user.getFullName().equals(activityInfoBinding.editName.getText()+""))
                upInfo();
        });
    }

    private void info(){
        if(user.getImg() != null){
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    activityInfoBinding.imgMain.setImageResource(R.drawable.logo);
                }
            });
            Picasso pic = builder.build();
            pic.load(user.getImg()).into(activityInfoBinding.imgMain);
        }

        if(user.getImgBack() != null){
            Picasso.Builder builder1 = new Picasso.Builder(getApplicationContext());
            builder1.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    activityInfoBinding.imgBack.setImageResource(R.drawable.background);
                }
            });
            Picasso pic1 = builder1.build();
            pic1.load(user.getImgBack()).into(activityInfoBinding.imgBack);
        }

        activityInfoBinding.editName.setText(user.getFullName());
        activityInfoBinding.editAcc.setText(user.getTk());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 || requestCode == 11){
            if(resultCode == RESULT_OK){
                ActivityResult result = new ActivityResult(resultCode, data);

                if (result.getResultCode() == RESULT_OK) {
                    for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                        Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                        if(requestCode == 10) {
                            path = getRealPathFromURI(imageUri);
                            activityInfoBinding.imgMain.setImageURI(imageUri);
                            checkImg = true;
                        }else{
                            pathBack = getRealPathFromURI(imageUri);
                            activityInfoBinding.imgBack.setImageURI(imageUri);
                            checkImgBack = true;
                        }
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void upImg(){
        String domain = "https://game.covid21tsp.space/";

        List<MultipartBody.Part> pho = new ArrayList<>();

        File f = new File(path);
        RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", f.getName(), photoContext);

        pho.add(photo);

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> upImg = serviceAPI_lib.UpImgMain(user.getTk(), domain, photo);
        upImg.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Thay đổi ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upImgBack(){
        String domain = "https://game.covid21tsp.space/";

        List<MultipartBody.Part> pho = new ArrayList<>();

        File f = new File(pathBack);
        RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", f.getName(), photoContext);

        pho.add(photo);

        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> upImg = serviceAPI_lib.UpImgBack(photo, user.getTk(), domain);
        upImg.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Thay đổi ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upInfo(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> upImg = serviceAPI_lib.UpInfo(user.getTk(), activityInfoBinding.editName.getText()+"");
        upImg.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Thay đổi ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}