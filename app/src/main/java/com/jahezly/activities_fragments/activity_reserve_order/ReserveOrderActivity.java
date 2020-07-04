package com.jahezly.activities_fragments.activity_reserve_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.jahezly.databinding.ActivityLoginBinding;
import com.jahezly.databinding.ActivityReserveOrderBinding;
import com.jahezly.language.Language;
import com.jahezly.models.AddOrderModel;
import com.jahezly.models.LoginModel;
import com.jahezly.share.Common;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class ReserveOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private ActivityReserveOrderBinding binding;
    private AddOrderModel model;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String lang;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reserve_order);
        Paper.init(this);
        initView();


    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        model = new AddOrderModel();
        binding.setModel(model);

        binding.rbFamily.setOnClickListener(v -> {
            model.setType("family");
        });

        binding.rbPeople.setOnClickListener(v -> {
            model.setType("people");
        });


        binding.rbInside.setOnClickListener(v -> {
            model.setInside_outside("inside");
        });

        binding.rbOutside.setOnClickListener(v -> {
            model.setType("outside");
        });

        binding.imgIncreasePeople.setOnClickListener(v -> {
            model.setPeopleNumber(model.getPeopleNumber()+1);
            binding.setModel(model);

        });

        binding.imgDecreasePeople.setOnClickListener(v -> {
            if (model.getPeopleNumber()>1){
                model.setPeopleNumber(model.getPeopleNumber()-1);
                binding.setModel(model);


            }
        });

        binding.imgIncreaseChildren.setOnClickListener(v -> {
            model.setChildrenNumber(model.getChildrenNumber()+1);
            binding.setModel(model);

        });

        binding.imgDecreaseChildren.setOnClickListener(v -> {
            if (model.getChildrenNumber()>0){
                model.setChildrenNumber(model.getChildrenNumber()-1);
                binding.setModel(model);
            }
        });

        binding.tvChange.setOnClickListener(v -> {
            try {
                datePickerDialog.show(getFragmentManager(), "");
            } catch (Exception e) {
            }
        });


        binding.btnSend.setOnClickListener(v -> {
            if (model.isDataValid(this)){
                Common.CloseKeyBoard(this,binding.edtName);
                sendOrder();
            }
        });
        createDatePickerDialog();
        createTimePickerDialog();
    }

    private void sendOrder() {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        startActivity(intent);

    }


    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.change));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale("en"));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);

    }

    private void createTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setOkText(getString(R.string.change));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setLocale(new Locale("en"));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);


    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String d = dateFormat.format(new Date(calendar.getTimeInMillis()));
        model.setDate(d);
        binding.setModel(model);
        try {
            timePickerDialog.show(getFragmentManager(), "");

        } catch (Exception e) {
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String t = dateFormat.format(new Date(calendar.getTimeInMillis()));
        model.setTime(calendar.getTimeInMillis()*1000);
        model.setFormatTime(t);
        binding.setModel(model);
    }
}
