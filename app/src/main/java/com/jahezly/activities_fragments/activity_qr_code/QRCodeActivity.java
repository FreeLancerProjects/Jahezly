package com.jahezly.activities_fragments.activity_qr_code;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.google.zxing.Result;
import com.jahezly.R;
import com.jahezly.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.jahezly.databinding.ActivityQrcodeBinding;
import com.jahezly.language.Language;
import com.jahezly.models.OrderModel;
import com.jahezly.models.UserModel;
import com.jahezly.preferences.Preferences;
import com.jahezly.remote.Api;
import com.jahezly.share.Common;
import com.jahezly.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private final int CAMERA_REQ = 1022;
    private final String camera_perm = Manifest.permission.CAMERA;
    private ActivityQrcodeBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode);
        binding.scannerView.setFormats(ZXingScannerView.ALL_FORMATS);
        binding.scannerView.setResultHandler(this);
        binding.scannerView.setAutoFocus(false);
        checkCameraPermission();
    }
    private void checkCameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this, camera_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_perm}, CAMERA_REQ);

        } else {
            binding.scannerView.startCamera();


        }
    }
    @Override
    public void handleResult(Result result) {
        scanCode(result.getText());
    }

    private void scanCode(String code)
    {
        getOrder(code);
    }

    private void getOrder(String code){
        try {
            ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
            dialog.show();

            Api.getService(Tags.base_url)
                    .getOrderByCode(userModel.getRestaurant().getToken(),code)
                    .enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null ) {
                                OrderModel orderModel = response.body();
                                Intent intent = new Intent(QRCodeActivity.this, OrderDetailsActivity.class);
                                intent.putExtra("data",orderModel);
                                startActivity(intent);
                                finish();

                            } else {
                                dialog.dismiss();

                                if (response.code() == 500) {
                                    Toast.makeText(QRCodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else if (response.code()==404){
                                    Toast.makeText(QRCodeActivity.this, R.string.code_not_found, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(QRCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                dialog.dismiss();


                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(QRCodeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(QRCodeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (binding.scannerView != null) {
                    binding.scannerView.startCamera();
                }
            } else {
                Toast.makeText(this, "Access camera denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkCameraPermission();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if (binding.scannerView != null) {
            binding.scannerView.stopCamera();
        }
    }


}
