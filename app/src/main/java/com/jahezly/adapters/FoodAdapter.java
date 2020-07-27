package com.jahezly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jahezly.R;
import com.jahezly.databinding.FoodRowBinding;
import com.jahezly.databinding.SnaksRowBinding;
import com.jahezly.models.FoodsModel;
import com.jahezly.models.SnaksModel;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FoodsModel> list;

    public FoodAdapter(Context context, List<FoodsModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FoodRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.food_row, parent, false);
        return new Holder1(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FoodsModel foodsModel = list.get(position);
        Holder1 holder1 = (Holder1) holder;
        holder1.binding.setModel(foodsModel);

        holder1.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        SnakAdapter snakAdapter = new SnakAdapter(context,foodsModel.getSnaks());
        holder1.binding.recView.setAdapter(snakAdapter);

        holder1.itemView.setOnClickListener(v -> {
            FoodsModel foodsModel2 = list.get(holder1.getAdapterPosition());

            if (foodsModel2.getSnaks()!=null&&foodsModel.getSnaks().size()>0){
                if (holder1.binding.expandLayout.isExpanded()){
                    holder1.binding.expandLayout.collapse(true);
                    holder1.binding.arrow.animate().rotation(0).setDuration(500).start();
                }else {
                    holder1.binding.expandLayout.expand(true);
                    holder1.binding.arrow.animate().rotation(180).setDuration(500).start();

                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        private FoodRowBinding binding;

        public Holder1(@NonNull FoodRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
