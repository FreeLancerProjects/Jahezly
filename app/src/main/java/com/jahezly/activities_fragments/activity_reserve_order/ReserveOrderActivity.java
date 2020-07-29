package com.jahezly.activities_fragments.activity_reserve_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.jahezly.databinding.ActivityLoginBinding;
import com.jahezly.databinding.ActivityReserveOrderBinding;
import com.jahezly.interfaces.Listeners;
import com.jahezly.language.Language;
import com.jahezly.models.AddOrderModel;
import com.jahezly.models.CreateOrderModel;
import com.jahezly.models.LoginModel;
import com.jahezly.models.OrderModel;
import com.jahezly.models.UserModel;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.share.Common;
import com.jahezly.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Listeners.BackListener {
    private ActivityReserveOrderBinding binding;
    private AddOrderModel model;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;



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
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        model = new AddOrderModel();
        binding.setModel(model);
        binding.setBackListener(this);
        binding.rbFamily.setOnClickListener(v -> {
            model.setType("family");
        });

        binding.rbPeople.setOnClickListener(v -> {
            model.setType("member");
        });


        binding.rbInside.setOnClickListener(v -> {
            model.setInside_outside("in");
        });

        binding.rbOutside.setOnClickListener(v -> {
            model.setType("out");
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
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        CreateOrderModel createOrderModel = new CreateOrderModel(String.valueOf(userModel.getRestaurant().getId()),model.getName(),model.getPhone(),String.valueOf(model.getPeopleNumber()),String.valueOf(model.getChildrenNumber()),"",model.getDate(),model.getFormatTime(),model.getInside_outside(),model.getType());
        try {


            Api.getService(Tags.base_url)
                    .createOrder(preferences.getUserData(this).getRestaurant().getToken(),createOrderModel)
                    .enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                Intent intent = new Intent(ReserveOrderActivity.this,OrderDetailsActivity.class);
                                intent.putExtra("data",response.body());
                                startActivity(intent);
                                finish();

                            } else {
                                dialog.dismiss();

                                if (response.code()==500){
                                    Toast.makeText(ReserveOrderActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ReserveOrderActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                }

                                try {
                                    Log.e("error",response.code()+"__"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderModel> call, Throwable t) {
                            try {
                                dialog.dismiss();

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(ReserveOrderActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ReserveOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                                dialog.dismiss();

                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();


        }


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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String t = dateFormat.format(new Date(calendar.getTimeInMillis()));

        model.setTime(calendar.getTimeInMillis()*1000);
        model.setFormatTime(t);
        binding.setModel(model);


    }

    @Override
    public void back() {
        finish();
    }
}
