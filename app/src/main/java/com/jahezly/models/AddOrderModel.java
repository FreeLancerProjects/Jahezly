package com.jahezly.models;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.jahezly.BR;
import com.jahezly.R;

import java.io.Serializable;

public class AddOrderModel extends BaseObservable implements Serializable {
    private String name;
    private String phone;
    private String type;
    private long time;
    private String formatTime;
    private String date;
    private int peopleNumber;
    private int childrenNumber;
    private String inside_outside;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() && 
                !phone.trim().isEmpty()&&
                !type.isEmpty()&&
                !date.isEmpty()&&
                time!=0&&
                !inside_outside.isEmpty()
        ){
            
            error_name.set(null);
            error_phone.set(null);
            return true;
        }else {
            
            if (name.trim().isEmpty()){
                error_name.set(context.getString(R.string.field_req));
            }else {
                error_name.set(null);

            }


            if (phone.trim().isEmpty()){
                error_phone.set(context.getString(R.string.field_req));
            }else {
                error_phone.set(null);

            }
            if (type.isEmpty()){
                Toast.makeText(context, R.string.ch_reserv_type, Toast.LENGTH_SHORT).show();
            }

            if (time==0){
                Toast.makeText(context, R.string.ch_reserve_time, Toast.LENGTH_SHORT).show();
            }

            if (date.isEmpty()){
                Toast.makeText(context, R.string.ch_reserve_date, Toast.LENGTH_SHORT).show();
            }


            
            return false;
        }
    }

    public AddOrderModel() {
        setName("");
        setPhone("");
        setType("");
        setTime(0);
        setDate("");
        setFormatTime("");
        setChildrenNumber(0);
        setInside_outside("inside");
        setPeopleNumber(1);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Bindable
    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
        notifyPropertyChanged(BR.peopleNumber);
    }

    @Bindable
    public int getChildrenNumber() {
        return childrenNumber;
    }

    public void setChildrenNumber(int childrenNumber) {
        this.childrenNumber = childrenNumber;
        notifyPropertyChanged(BR.childrenNumber);

    }


    public String getInside_outside() {
        return inside_outside;
    }

    public void setInside_outside(String inside_outside) {
        this.inside_outside = inside_outside;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
