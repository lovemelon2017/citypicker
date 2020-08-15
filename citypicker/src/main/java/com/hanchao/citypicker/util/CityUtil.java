package com.hanchao.citypicker.util;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.hanchao.citypicker.bean.CityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * @author :小豆豆打飞机
 * @date: 2020/8/14
 * @motto: A good beginning is half the battle
 */
public class CityUtil {

    public static ArrayList<CityModel> getCityList(Context context) {
        return getCityList(context, null);
    }

    public static ArrayList<CityModel> getCityList(Context context, String filterStr) {
        ArrayList<CityModel> cities;
        cities = getCitiesFromOfflineCities(context);

        ArrayList<CityModel> res = new ArrayList<>();
        for (CityModel item : cities) {
            if (TextUtils.isEmpty(filterStr) || item.getPinyin().contains(filterStr) || item.getCity().contains(
                    filterStr)) {
                res.add(item);
            }
        }

        return res;
    }

    private static volatile HashSet<String> mHotCities = new HashSet<>();

    public static void setHotCities(HashSet<String> hotCities) {
        if (hotCities == null || hotCities.size() == 0) {
            mHotCities.add("北京市");
            mHotCities.add("广州市");
            mHotCities.add("成都市");
            mHotCities.add("上海市");
            mHotCities.add("杭州市");
            mHotCities.add("武汉市");
            return;
        }

        mHotCities.clear();
        mHotCities.addAll(hotCities);
    }

    public static char HOT_CITY_CHAR = '#';


    public static CityModel getLocationCityModel(Context context, String cityCode) {
        ArrayList<CityModel> oriCityList = getCitiesFromOfflineCities(context);
        CityModel cityModel = null;
        for (CityModel item : oriCityList) {
            if (cityCode.equals(item.getCode())) {
                cityModel = item;
                break;
            }
        }
        return cityModel;
    }

    public static ArrayList<CityModel> getGroupCityList(Context context) {
        return getGroupCityList(context, null);
    }

    public static ArrayList<CityModel> getGroupCityList(Context context, String filterStr) {
        ArrayList<CityModel> res = new ArrayList<CityModel>();

        ArrayList<CityModel> oriCityList = getCityList(context, filterStr);
        Collections.sort(oriCityList, new Comparator<CityModel>() {
            @Override
            public int compare(CityModel o1, CityModel o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });

        // 先录入热门城市
        if (mHotCities != null && mHotCities.size() > 0) {
            for (CityModel item : oriCityList) {
                if (mHotCities.contains(item.getCity())) {
                    res.add(CityModel.createHotCityModel(item));
                    continue;
                }
            }
        }

        char currChar = ' ';
        for (CityModel item : oriCityList) {
            try {
                res.add(item);
            } catch (Exception e) {
                continue;
            }
        }

        return res;
    }

    private static ArrayList<CityModel> offlieCityCache = new ArrayList<>();

    private static ArrayList<CityModel> getCitiesFromOfflineCities(Context context) {

        if (offlieCityCache != null && offlieCityCache.size() > 0) {
            return offlieCityCache;
        }

        ArrayList<CityModel> res = new ArrayList<>();
        OfflineMapManager mapManager = new OfflineMapManager(context, null);
        ArrayList<OfflineMapCity> cities = mapManager.getOfflineMapCityList();

        for (OfflineMapCity offlineMapCity : cities) {
            res.add(new CityModel(offlineMapCity));
        }

        offlieCityCache = res;

        return res;
    }
}
