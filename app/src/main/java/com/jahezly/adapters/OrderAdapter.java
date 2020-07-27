package com.jahezly.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jahezly.R;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Current;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Finished;
import com.jahezly.activities_fragments.activity_home.fragments.Fragment_Late;
import com.jahezly.activities_fragments.activity_search.SearchActivity;
import com.jahezly.databinding.LoadMoreRowBinding;
import com.jahezly.databinding.OrderRowBinding;
import com.jahezly.models.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int LOAD = 2;
    private Context context;
    private List<OrderModel>  list;
    private Fragment fragment;


    public OrderAdapter(Context context, List<OrderModel>  list, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==DATA) {
            OrderRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_row, parent, false);
            return new Holder1(binding);


        }else
            {
                LoadMoreRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.load_more_row,parent,false);
                return new LoadHolder(binding);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderModel orderModel = list.get(position);


        if (holder instanceof Holder1)
        {
            Holder1 holder1 = (Holder1) holder;
            holder1.binding.setModel(orderModel);


            holder1.binding.btnDetails.setOnClickListener(view -> {
                {
                    OrderModel  orderModel2 = list.get(holder1.getAdapterPosition());

                    if (fragment instanceof Fragment_Current)
                    {
                        Fragment_Current fragment_current = (Fragment_Current) fragment;
                        fragment_current.setItemData(orderModel2);
                    }else if (fragment instanceof Fragment_Late)
                    {
                        Fragment_Late fragment_late = (Fragment_Late) fragment;
                        fragment_late.setItemData(orderModel2);
                    }
                    else if (fragment instanceof Fragment_Finished)
                    {
                        Fragment_Finished fragment_finished = (Fragment_Finished) fragment;
                        fragment_finished.setItemData(orderModel2);
                    }else if (fragment==null){
                        SearchActivity activity = (SearchActivity) context;
                        activity.setItemData(orderModel2);
                    }
                }
            });

        }else if (holder instanceof LoadHolder)
        {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadHolder.binding.progBar.setIndeterminate(true);

        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        private OrderRowBinding binding;

        public Holder1(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreRowBinding binding;

        public LoadHolder(@NonNull LoadMoreRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (list.get(position)==null)
        {
            return LOAD;
        }else
        {
            return DATA;
        }
    }
}
