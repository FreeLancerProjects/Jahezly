package com.jahezly.activities_fragments.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_splash.SplashActivity;
import com.jahezly.databinding.ActivityLoginBinding;
import com.jahezly.interfaces.Listeners;
import com.jahezly.language.Language;
import com.jahezly.models.LoginModel;
import com.jahezly.models.UserModel;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.share.Common;
import com.jahezly.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Listeners.LoginListener {
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
    private Preferences preferences;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        Paper.init(this);
        initView();



    }

    private void initView() {
        preferences = Preferences.getInstance();
        loginModel = new LoginModel();
        binding.setModel(loginModel);
        binding.setListener(this);

    }

    @Override
    public void checkDataLogin() {
        if (loginModel.isDataValid(this)){
            Common.CloseKeyBoard(this,binding.edtUserName);
            login();
        }
    }

    private void login() {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(loginModel.getUserName(),loginModel.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            preferences.create_update_userdata(LoginActivity.this,response.body());
                            navigateToHomeActivity();

                        }else
                        {

                            try {
                                Log.e("code",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code()==500)
                            {
                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if (response.code()==404)
                            {
                                Toast.makeText(LoginActivity.this, R.string.user_pas_not_correct, Toast.LENGTH_SHORT).show();
                            }else
                            {

                                Toast.makeText(LoginActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e)
                        {
                            Log.e("Error",e.getMessage()+"__");
                        }
                    }
                });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
