package com.jahezly.activities_fragments.activity_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_login.LoginActivity;
import com.jahezly.databinding.ActivitySplashBinding;
import com.jahezly.language.Language;
import com.jahezly.preferences.Preferences;
import com.jahezly.tags.Tags;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        preferences = Preferences.getInstance();

        String path = "android.resource://"+getPackageName()+"/"+R.raw.splash_vid;
        binding.videoView.setVideoPath(path);
        binding.videoView.setOnPreparedListener(mediaPlayer -> binding.videoView.start());

        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                String session = preferences.getSession(SplashActivity.this);

                if (session.equals(Tags.session_login))
                {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
