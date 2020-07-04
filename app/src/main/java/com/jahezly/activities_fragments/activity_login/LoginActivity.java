package com.jahezly.activities_fragments.activity_login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_splash.SplashActivity;
import com.jahezly.databinding.ActivityLoginBinding;
import com.jahezly.interfaces.Listeners;
import com.jahezly.language.Language;
import com.jahezly.models.LoginModel;
import com.jahezly.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements Listeners.LoginListener {
    private ActivityLoginBinding binding;
    private LoginModel loginModel;
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
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
