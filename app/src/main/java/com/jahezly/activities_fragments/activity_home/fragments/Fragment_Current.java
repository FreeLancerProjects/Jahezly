package com.jahezly.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.HomeActivity;
import com.jahezly.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.jahezly.adapters.OrderAdapter;
import com.jahezly.databinding.FragmentCurrentEndedLateBinding;
import com.jahezly.models.OrderDataModel;
import com.jahezly.models.OrderModel;
import com.jahezly.models.UserModel;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Current extends Fragment {
    private HomeActivity activity;
    private FragmentCurrentEndedLateBinding binding;
    private OrderAdapter adapter;
    private List<OrderModel> orderModelList;
    private boolean isLoading = false;
    private int current_page = 1;
    private Preferences preferences;
    private UserModel userModel;


    public static Fragment_Current newInstance() {
        return new Fragment_Current();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_ended_late, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        orderModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new OrderAdapter(activity, orderModelList, this);
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total = binding.recView.getAdapter().getItemCount();

                    int lastVisibleItem = ((LinearLayoutManager) binding.recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();


                    if (total >= 20 && (total - lastVisibleItem) == 2 && !isLoading) {
                        isLoading = true;
                        int page = current_page + 1;
                        orderModelList.add(null);
                        adapter.notifyDataSetChanged();
                        loadMore(page);
                    }
                }
            }
        });

        getOrders();
    }


    public void getOrders() {
        try {

            Api.getService(Tags.base_url)
                    .getOrders(userModel.getRestaurant().getToken(), String.valueOf(userModel.getRestaurant().getId()), "on", "current", 1, 20)
                    .enqueue(new Callback<OrderDataModel>() {
                        @Override
                        public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                orderModelList.clear();
                                orderModelList.addAll(response.body().getData());
                                if (orderModelList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoOrders.setVisibility(View.GONE);
                                } else {
                                    binding.tvNoOrders.setVisibility(View.VISIBLE);

                                }
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }

    }

    private void loadMore(int page) {

        try {



            Api.getService(Tags.base_url)
                    .getOrders(userModel.getRestaurant().getToken(), String.valueOf(userModel.getRestaurant().getId()), "on", "current", page, 20)
                    .enqueue(new Callback<OrderDataModel>() {
                        @Override
                        public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                            isLoading = false;
                            orderModelList.remove(orderModelList.size()-1);
                            adapter.notifyItemRemoved(orderModelList.size()-1);

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                orderModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                if (response.body().getData().size()>0)
                                {
                                    current_page = response.body().getCurrent_page();

                                }
                            } else {
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                if (orderModelList.get(orderModelList.size()-1)==null)
                                {
                                    isLoading = false;
                                    orderModelList.remove(orderModelList.size()-1);
                                    adapter.notifyItemRemoved(orderModelList.size()-1);

                                }
                                binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra("data",orderModel2);
        startActivity(intent);
    }
}
