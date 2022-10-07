package com.coolweather.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.coolweather.colorweather.db.*;
import com.coolweather.colorweather.util.savePC;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

public class chooseAreaFragment2 extends Fragment {

    private static final int PROVINCE_LEVEL = 0;
    private static final int CITY_LEVEL = 1;
    private static final int COUNTY_LEVEL = 2;
    private static int current_level;
    private static int key = 0;

    private Button back;
    private TextView title;

    private RecyclerView recyclerView;

    private List<ProvinceName> provinceList;
    private List<CityProvince> cityProvinceList;
    private List<CityLocationId> countyList;
    private List<String> dataList = new ArrayList<>();

    private RecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        List<ProvinceName> provinceNameList = LitePal.findAll(ProvinceName.class);
        List<CityLocationId> cityLocationIdList = LitePal.findAll(CityLocationId.class);
        List<CityProvince> cityProvinceList = LitePal.findAll(CityProvince.class);
        if (provinceNameList.size() == 0 && cityLocationIdList.size() == 0 && cityProvinceList.size() == 0) {
            savePC.saveFile(getContext());
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.choose_area, container, false);
        back = (Button) view.findViewById(R.id.back);
        title = (TextView) view.findViewById(R.id.title);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewAdapter(dataList, getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        queryProvince();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_level == COUNTY_LEVEL) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String provinceName = pref.getString("provinceName", "");
                    queryCity(provinceName);
                } else if (current_level == CITY_LEVEL) {
                    queryProvince();
                }
            }
        });
    }

    private void queryProvince() {
        Log.d("123", "queryProvince begin");
        current_level = PROVINCE_LEVEL;
        title.setText("中国");
        back.setVisibility(View.GONE);
        // 先判断provinList是否为空，不为空直接用，如果provinList为空，则从数据库中找
        if (provinceList != null && provinceList.size() > 0) {
            dataList.clear();
            for (ProvinceName province : provinceList) {
                dataList.add(province.getProvinceName());
            }
        } else {
            provinceList = LitePal.findAll(ProvinceName.class);
            dataList.clear();
            for (ProvinceName province : provinceList) {
                dataList.add(province.getProvinceName());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void queryCity(String provinceName) {
        Log.d("123", "queryCity begin");
        current_level = CITY_LEVEL;
        title.setText(provinceName);
        back.setVisibility(View.VISIBLE);
        cityProvinceList = LitePal
                .where("provinceName = ?", provinceName)
                .find(CityProvince.class);
        dataList.clear();
        for (CityProvince city : cityProvinceList) {
            dataList.add(city.getCityName());
        }
        adapter.notifyDataSetChanged();
    }

    private void queryCounty(String cityName) {
        Log.d("123", "queryCounty begin");
        current_level = COUNTY_LEVEL;
        title.setText(cityName);
        back.setVisibility(View.VISIBLE);
        countyList = LitePal
                .where("cityName = ?", cityName)
                .find(CityLocationId.class);
        dataList.clear();
        for (CityLocationId city : countyList) {
            dataList.add(city.getCountyName());
        }
        adapter.notifyDataSetChanged();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private List<String> dataList;
        private Activity activity;

        public RecyclerViewAdapter(List<String> dataList, Activity activity) {
            this.dataList = dataList;
            this.activity = activity;
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.china_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.contentText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current_level == PROVINCE_LEVEL) {
                        SharedPreferences.Editor pref = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        pref.putString("provinceName", (String)holder.contentText.getText());
                        pref.apply();
                        queryCity((String)holder.contentText.getText());
                    } else if (current_level == CITY_LEVEL) {
                        queryCounty((String)holder.contentText.getText());
                    } else if (current_level == COUNTY_LEVEL) {
                        List<CityLocationId> county = LitePal
                                .limit(1)
                                .where("countyName = ?", (String)holder.contentText.getText())
                                .find(CityLocationId.class);
                        if (activity instanceof weatherActivity) {
                        } else if (activity instanceof AccessAreaActivity){
                            // 更新为false
                            positionCity city = new positionCity();
                            city.setSecond(false);
                            city.updateAll();
                            // 查重
                            positionCity city2 = null;// 定位
                            List<positionCity> positionCityList = LitePal.findAll(positionCity.class);
                            for (int i = 0; i < positionCityList.size(); i++) {
                                if (positionCityList.get(i).getCountyName().equals(String.valueOf(holder.contentText.getText()))) {
                                    key=1;
                                    city = positionCityList.get(i);
                                    break;
                                }
                            }
                            // 不重复
                            if (key == 0) {
                                // 新添加的设置为true
                                positionCity positionCity = new positionCity();
                                positionCity.setCountyName((String) holder.contentText.getText());
                                positionCity.setLocationId(county.get(0).getLocationId());
                                positionCity.setFirst(false);
                                positionCity.setSecond(true);
                                positionCity.save();
                                Log.d("123", "save");
                            } else {
                                // 新添加的更新为true
                                positionCity positionCity = new positionCity();
                                if (city2 != null && city2.getFirst() == true) {
                                } else {
                                    positionCity.setFirst(false);
                                }
                                positionCity.setSecond(true);
                                positionCity.updateAll("countyName = ?", (String) holder.contentText.getText());
                                Log.d("123", "notSave");
                            }
                        }
                        Intent intent = new Intent(getContext(), weatherActivity.class);
                        intent.putExtra("countyName", "adapter");
                        startActivity(intent);
                        Activity activity = getActivity();
                        activity.finish();
                        ActivityCollector.finishAll();
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            String content = dataList.get(position);
            holder.contentText.setText(content);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView contentText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                contentText = (TextView) itemView.findViewById(R.id.value_text);
            }
        }
    }

}
