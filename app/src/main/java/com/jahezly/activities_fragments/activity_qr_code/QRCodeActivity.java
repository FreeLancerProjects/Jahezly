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
import com.jahezly.databinding.ActivityQrcodeBinding;
import com.jahezly.language.Language;

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

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
