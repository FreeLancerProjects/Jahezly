package com.jahezly.activities_fragments.activity_search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.jahezly.adapters.DrinkAdapter;
import com.jahezly.adapters.FoodAdapter;
import com.jahezly.adapters.OrderAdapter;
import com.jahezly.databinding.ActivityOrderDetailsBinding;
import com.jahezly.databinding.ActivitySearchBinding;
import com.jahezly.interfaces.Listeners;
import com.jahezly.language.Language;
import com.jahezly.models.OrderDataModel;
import com.jahezly.models.OrderModel;
import com.jahezly.models.UserModel;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivitySearchBinding binding;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private String query = "";
    private List<OrderModel> orderModelList;
    private OrderAdapter adapter;
    private Call<OrderDataModel> searchCall;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        initView();


    }


    private void initView() {
        orderModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, orderModelList, null);
        binding.recView.setAdapter(adapter);

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString().trim();
                if (query.isEmpty()) {
                    binding.progBar.setVisibility(View.GONE);
                    orderModelList.clear();
                    adapter.notifyDataSetChanged();
                    binding.tvNoSearchResults.setVisibility(View.VISIBLE);
                    if (searchCall!=null){
                        searchCall.cancel();

                    }

                } else {
                    search();
                }
            }
        });

    }


    private void search() {
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoSearchResults.setVisibility(View.GONE);
        orderModelList.clear();
        adapter.notifyDataSetChanged();
        try {

            searchCall = Api.getService(Tags.base_url).search(userModel.getRestaurant().getToken(), query);
            searchCall.enqueue(new Callback<OrderDataModel>() {
                @Override
                public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                    binding.progBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        orderModelList.addAll(response.body().getData());
                        if (orderModelList.size() > 0) {
                            adapter.notifyDataSetChanged();
                            binding.tvNoSearchResults.setVisibility(View.GONE);

                        } else {
                            binding.tvNoSearchResults.setVisibility(View.VISIBLE);

                        }
                    } else {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.code() == 500) {
                            Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderDataModel> call, Throwable t) {
                    try {
                        binding.progBar.setVisibility(View.GONE);


                        if (t.getMessage() != null) {
                            Log.e("error", t.getMessage());
                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                Toast.makeText(SearchActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                            }else if (t.getMessage().contains("socket")){

                            }else {
                                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public void setItemData(OrderModel orderModel2) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("data",orderModel2);
        startActivity(intent);
    }

    @Override
    public void back() {
        finish();
    }


}
