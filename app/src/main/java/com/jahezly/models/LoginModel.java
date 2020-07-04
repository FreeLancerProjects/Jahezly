package com.jahezly.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.jahezly.BR;
import com.jahezly.R;

public class LoginModel extends BaseObservable {

    private String userName;
    private String password;
    public ObservableField<String> error_userName = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();

    public boolean isDataValid(Context context){
        if (!userName.isEmpty()&&
                !password.isEmpty()&&
                password.length()>=6
        ){
            error_userName.set(null);
            error_password.set(null);
            return true;
        }else {

            if (userName.isEmpty()){
                error_userName.set(context.getString(R.string.field_req));
            }else {
                error_userName.set(null);

            }


            if (password.isEmpty()){
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6){
                error_password.set(context.getString(R.string.pas_short));


            }else {
                error_password.set(null);

            }

            return false;
        }
    }
    public LoginModel() {
        userName="";
        password="";
    }



    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
