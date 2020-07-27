package com.jahezly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.jahezly.R;
import com.jahezly.databinding.DrinkRowBinding;
import com.jahezly.databinding.SnaksRowBinding;
import com.jahezly.models.DrinkModel;
import com.jahezly.models.SnaksModel;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DrinkModel> list;

    public DrinkAdapter(Context context, List<DrinkModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DrinkRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.drink_row, parent, false);
        return new Holder1(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DrinkModel drinkModel = list.get(position);


        Holder1 holder1 = (Holder1) holder;
        holder1.binding.setModel(drinkModel);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        private DrinkRowBinding binding;

        public Holder1(@NonNull DrinkRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
