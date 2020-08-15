package com.hanchao.cityselect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hanchao.citypicker.CityPickerActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSelect = findViewById(R.id.select_tv);

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }else {
            Intent intent = new Intent(this, CityPickerActivity.class);
            startActivityForResult(intent, 100);
        }
    }

    public void onClick(View view) {

        requestPermission();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("city");
                if (!TextUtils.isEmpty(city)) {
                    tvSelect.setText("当前城市:" + city);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200://刚才的识别码
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                    Intent intent = new Intent(this, CityPickerActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    Toast.makeText(MainActivity.this, "未开启定位权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}