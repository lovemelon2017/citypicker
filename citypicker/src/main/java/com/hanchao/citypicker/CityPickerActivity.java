package com.hanchao.citypicker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.hanchao.citypicker.adapter.CityAdapter;
import com.hanchao.citypicker.bean.CityModel;
import com.hanchao.citypicker.helper.GaoDeCityIIndexBarDataHelper;
import com.hanchao.citypicker.helper.SuspensionDecoration;
import com.hanchao.citypicker.util.AMapLocationHelper;
import com.hanchao.citypicker.util.CityUtil;
import com.hanchao.citypicker.view.IndexBar;

import java.util.ArrayList;

/**
 * 城市选择页面
 */
public class CityPickerActivity extends AppCompatActivity {

    EditText etCityInput;
    TextView tvLocation;
    TextView tvCancel;
    RecyclerView mRvCity;
    IndexBar mIndexBar;
    TextView tvSlidHide;
    ImageView ivClearInput;

    LinearLayoutManager manager;
    CityAdapter mAdapter;
    ArrayList<CityModel> groupedCityList = new ArrayList<>();//总城市列表
    ArrayList<CityModel> searchCityList = new ArrayList<>();

    SuspensionDecoration suspensionDecoration;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        initView();
        initRv();
        initData();
        initLocation();
        initListener();
    }

    private void initLocation() {

        AMapLocationHelper helper = new AMapLocationHelper(this);

        helper.startSingleLocate(new AMapLocationHelper.OnLocationGetListeneAdapter() {
            @Override
            public void onLocationGetSuccess(AMapLocation loc) {
                Log.e("han", loc.toString());
                cityName= loc.getCity();
                tvLocation.setText("定位城市:" + cityName);
            }

            @Override
            public void onLocationGetFail(AMapLocation loc) {
                Log.e("han", loc.toString());
                Toast.makeText(CityPickerActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cityName)){
                    Intent intent = new Intent();
                    intent.putExtra("city", cityName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        etCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                searchCity(input);
            }
        });


        ivClearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCity("");
                etCityInput.setText("");
            }
        });
    }

    private void searchCity(String input) {
        if (TextUtils.isEmpty(input)) {
            ivClearInput.setVisibility(View.GONE);
        } else {
            ivClearInput.setVisibility(View.VISIBLE);
        }

        searchCityList.clear();
        for (int i = 0; i < groupedCityList.size(); i++) {
            CityModel model = groupedCityList.get(i);
            if (model.getCity().contains(input)) {
                searchCityList.add(model);
            }
        }
        mIndexBar.setmSourceDatas(searchCityList).invalidate();
        suspensionDecoration.setmDatas(searchCityList);

        mAdapter.setData(searchCityList);

    }

    private void initData() {
        suspensionDecoration = new SuspensionDecoration(this, groupedCityList);
        mRvCity.addItemDecoration(suspensionDecoration);

        mIndexBar.setmPressedShowTextView(tvSlidHide)
                .setDataHelper(new GaoDeCityIIndexBarDataHelper())
                .setNeedRealIndex(true)//设置需要真实的索引
                .setSourceDatasAlreadySorted(true)
                .setmLayoutManager(manager);
        CityUtil.setHotCities(null);

        ArrayList<CityModel> cityList = CityUtil.getGroupCityList(this);
        groupedCityList.addAll(cityList);
        mIndexBar.setmSourceDatas(groupedCityList).invalidate();
    }

    private void initRv() {
        manager = new LinearLayoutManager(this);
        mRvCity.setLayoutManager(manager);
        mAdapter = new CityAdapter(this, groupedCityList);
        mRvCity.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = getResources().getDrawable(R.drawable.shape_divider);
        dividerItemDecoration.setDrawable(drawable);
        mRvCity.addItemDecoration(dividerItemDecoration);

        mAdapter.setClick(new CityAdapter.ItemCityClick() {
            @Override
            public void cityName(String name) {
                Intent intent = new Intent();
                intent.putExtra("city", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void initView() {
        etCityInput = findViewById(R.id.et_city);
        tvLocation = findViewById(R.id.tv_choose_city);
        tvCancel = findViewById(R.id.tv_cancel);
        mRvCity = findViewById(R.id.city_rv);
        mIndexBar = findViewById(R.id.indexBar);
        tvSlidHide = findViewById(R.id.tvSideBarHint);
        ivClearInput = findViewById(R.id.iv_delete);
    }
}