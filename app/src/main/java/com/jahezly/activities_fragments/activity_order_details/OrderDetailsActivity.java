package com.jahezly.activities_fragments.activity_order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.jahezly.R;
import com.jahezly.databinding.ActivityOrderDetailsBinding;
import com.jahezly.databinding.ActivityReserveOrderBinding;
import com.jahezly.language.Language;
import com.jahezly.models.AddOrderModel;
import com.jahezly.share.Common;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity {
    private ActivityOrderDetailsBinding binding;
    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        Paper.init(this);
        initView();


    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

    }

}
