package com.jahezly.activities_fragments.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Current;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Finished;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Late;
import com.jahezly.activities_fragments.activity_login.LoginActivity;
import com.jahezly.activities_fragments.activity_qr_code.QRCodeActivity;
import com.jahezly.activities_fragments.activity_reserve_order.ReserveOrderActivity;
import com.jahezly.adapters.MyPagerFragmentsAdapter;
import com.jahezly.databinding.ActivityHomeBinding;
import com.jahezly.databinding.DialogLanguageBinding;
import com.jahezly.language.Language;
import com.jahezly.models.UserModel;
import com.jahezly.notifications.FireBaseMessaging;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.share.Common;
import com.jahezly.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private MyPagerFragmentsAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();



    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.tab.setupWithViewPager(binding.pager);
        adapter = new MyPagerFragmentsAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.createFragments_titles(getTitles(),getFragments());
        binding.pager.setAdapter(adapter);

        binding.tvAddOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReserveOrderActivity.class);
            startActivityForResult(intent,100);
        });

        binding.imageQrCode.setOnClickListener(v -> {
            Intent intent = new Intent(this, QRCodeActivity.class);
            startActivity(intent);
        });


        binding.imageLogout.setOnClickListener(v -> {
            if (userModel!=null){
                logout();
            }else {
                navigateToSignInActivity();
            }
        });


        binding.imageLanguage.setOnClickListener(v -> {
           createLangDialog();
        });

        if (userModel!=null){
            updateTokenFireBase();
        }

    }


    private List<Fragment> getFragments(){
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(Fragment_Current.newInstance());
        fragmentList.add(Fragment_Late.newInstance());
        fragmentList.add(Fragment_Finished.newInstance());

        return fragmentList;

    }


    private List<String> getTitles(){
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.current));
        titles.add(getString(R.string.late));
        titles.add(getString(R.string.finished));

        return titles;

    }



    private void createLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", "ar");
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> {
                        if (!lang.equals("ar")) {
                            refreshActivity("ar");
                        }
                    }, 200);
        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> {
                        if (!lang.equals("en")) {
                            refreshActivity("en");
                        }
                    }, 200);
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 1050);


    }


    private void updateTokenFireBase() {


        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String token = task.getResult().getToken();
                try {
                    UserModel userModel = preferences.getUserData(this);
                    UserModel.User user = userModel.getRestaurant();

                    Api.getService(Tags.base_url)
                            .updateFirebaseToken(user.getToken(), token,"android","restaurant", user.getId())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                    } else {
                                        try {
                                            user.setFirebase_token(token);
                                            userModel.setRestaurant(user);
                                            preferences.create_update_userdata(HomeActivity.this,userModel);
                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    try {

                                        if (t.getMessage() != null) {
                                            Log.e("error", t.getMessage());
                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            });
                } catch (Exception e) {


                }

            }
        });
    }

    public void logout() {
        if (userModel != null) {


            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.show();


            FirebaseInstanceId.getInstance()
                    .getInstanceId().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();

                    Api.getService(Tags.base_url)
                            .logout(userModel.getRestaurant().getToken(), token,"android",userModel.getRestaurant().getId())
                            .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    dialog.dismiss();
                                    if (response.isSuccessful()) {
                                        preferences.clear(HomeActivity.this);
                                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        if (manager != null) {
                                            manager.cancel(Tags.not_tag, Tags.not_id);
                                        }
                                        navigateToSignInActivity();


                                    } else {
                                        dialog.dismiss();
                                        try {
                                            Log.e("error", response.code() + "__" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (response.code() == 500) {
                                            Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    try {
                                        dialog.dismiss();
                                        if (t.getMessage() != null) {
                                            Log.e("error", t.getMessage() + "__");

                                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e("Error", e.getMessage() + "__");
                                    }
                                }
                            });

                }
            });


        } else {
            navigateToSignInActivity();
        }

    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList){
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode==100&&resultCode==RESULT_OK){
            Fragment_Current fragment_current = (Fragment_Current) adapter.getItem(0);
            fragment_current.getOrders();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragmentList){
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (userModel!=null){
            finish();
        }else {
            navigateToSignInActivity();
        }
    }
}
