package com.jahezly.activities_fragments.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Current;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Finished;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Late;
import com.jahezly.activities_fragments.activity_qr_code.QRCodeActivity;
import com.jahezly.activities_fragments.activity_reserve_order.ReserveOrderActivity;
import com.jahezly.adapters.MyPagerFragmentsAdapter;
import com.jahezly.databinding.ActivityHomeBinding;
import com.jahezly.databinding.DialogLanguageBinding;
import com.jahezly.language.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private MyPagerFragmentsAdapter adapter;
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


        binding.imageLanguage.setOnClickListener(v -> {
           createLangDialog();
        });

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
}
