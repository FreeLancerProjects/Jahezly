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
import com.jahezly.databinding.LoadMoreRowBinding;
import com.jahezly.databinding.OrderRowBinding;
import com.jahezly.databinding.SnaksRowBinding;
import com.jahezly.models.OrderModel;
import com.jahezly.models.SnaksModel;

import java.util.List;

public class SnakAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SnaksModel> list;

    public SnakAdapter(Context context, List<SnaksModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SnaksRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.snaks_row, parent, false);
        return new Holder1(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SnaksModel snaksModel = list.get(position);


        Holder1 holder1 = (Holder1) holder;
        holder1.binding.setModel(snaksModel);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        private SnaksRowBinding binding;

        public Holder1(@NonNull SnaksRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
