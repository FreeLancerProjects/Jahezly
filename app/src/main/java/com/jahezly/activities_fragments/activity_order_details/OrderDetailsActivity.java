package com.jahezly.activities_fragments.activity_order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jahezly.R;
import com.jahezly.adapters.DrinkAdapter;
import com.jahezly.adapters.FoodAdapter;
import com.jahezly.databinding.ActivityOrderDetailsBinding;
import com.jahezly.databinding.ActivityReserveOrderBinding;
import com.jahezly.interfaces.Listeners;
import com.jahezly.language.Language;
import com.jahezly.models.AddOrderModel;
import com.jahezly.models.DrinkModel;
import com.jahezly.models.FoodsModel;
import com.jahezly.models.OrderDataModel;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderDetailsBinding binding;
    private String lang;
    private OrderModel orderModel;
    private FoodAdapter foodAdapter;
    private DrinkAdapter drinkAdapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        Paper.init(this);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.setOrderModel(orderModel);
        binding.setBackListener(this);
        binding.recViewFoods.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewDrink.setLayoutManager(new LinearLayoutManager(this));

        getOrder();

    }

    private void updateUi() {
        if (orderModel.getFoods()!=null&&orderModel.getFoods().size()>0){
            foodAdapter = new FoodAdapter(this,orderModel.getFoods());
            binding.recViewFoods.setAdapter(foodAdapter);
        }


        if (orderModel.getDrinks()!=null&&orderModel.getDrinks().size()>0){
            drinkAdapter = new DrinkAdapter(this,orderModel.getDrinks());
            binding.recViewDrink.setAdapter(drinkAdapter);
            binding.tvNoDrink.setVisibility(View.GONE);

        }else {
            binding.tvNoDrink.setVisibility(View.VISIBLE);
        }


    }

    private void getOrder(){
        try {

            Api.getService(Tags.base_url)
                    .getOrdersById(userModel.getRestaurant().getToken(),orderModel.getId())
                    .enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {

                            if (response.isSuccessful() && response.body() != null ) {
                               orderModel = response.body();
                               binding.setOrderModel(orderModel);
                               updateUi();
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(OrderDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(OrderDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderModel> call, Throwable t) {
                            try {


                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(OrderDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    @Override
    public void back() {
        finish();
    }
}
