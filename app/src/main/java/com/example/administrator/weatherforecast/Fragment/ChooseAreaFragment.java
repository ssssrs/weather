package com.example.administrator.weatherforecast.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.weatherforecast.Database.City;
import com.example.administrator.weatherforecast.Database.County;
import com.example.administrator.weatherforecast.Database.Province;
import com.example.administrator.weatherforecast.R;
import com.example.administrator.weatherforecast.util.HttpUtil;
import com.example.administrator.weatherforecast.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wu873191083 on 2017/5/11.
 */

public class ChooseAreaFragment extends Fragment {

    private static final String TAG = "ChooseAreaFragment";
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;

    private ProgressDialog progressDialog;//进度条
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();

    private List<Province> provincesList;//省

    private List<City> cityList;//市

    private List<County> countyList;//县

    private Province selectedProvince;

    private City selectedCity;

    private County countyProvince;

    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=(TextView)view.findViewById(R.id.title_text);
        backButton=(Button) view.findViewById(R.id.back_button);
        listView=(ListView) view.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        Log.d(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){

                    selectedProvince=provincesList.get(position);
                    Log.d(TAG, "点击省: "+currentLevel);

                    queryCities();

                }
                else if(currentLevel==LEVEL_CITY){
//                    Log.d(TAG, "点击: "+currentLevel);
                    selectedCity=cityList.get(position);
                    queryCounties();
                }

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCities();
                }
                else if(currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void showProgressDialog(){//显示进度条
        if(progressDialog==null){

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }

    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    private void queryFromServer(String address, final String type){

        showProgressDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);

                }else if("city".equals(type)) {

                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());

                }else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

                if(result){

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){

                                queryCities();

                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });


    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);//隐藏backButton
        provincesList= DataSupport.findAll(Province.class);//获取provinceList中的所有数据
        if(provincesList.size()>0){
            dataList.clear();
            for(Province province:provincesList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);//插数据的时候从list头开始插，只在通常app上的话不加也无所谓
            currentLevel=LEVEL_PROVINCE;
        }else {

            String address="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }

    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);//ackButton可见
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        Log.d(TAG, "数组长度: "+cityList.size());
//        int count= cityList.size();
//        String num[] = new String[count];
        if(cityList.size()>0){

            dataList.clear();
//            City city;
//
//            for (int i = 0; i < count; i++) {
//
//                city=cityList.get(i);
//                num[i]=city.getCityName();
//                Log.d(TAG, "数组"+city.getCityName());
//
//            }
//            for(int i=0;i<num.length;i++){
//                dataList.add(num[i]);
//            }
            for(City city:cityList){
                Log.d(TAG, "城市："+city.getCityName());
                dataList.add(city.getCityName());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);//插数据的时候从list头开始插，只在通常app上的话不加也无所谓
            currentLevel=LEVEL_CITY;
        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }

    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);//ackButton可见
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);//插数据的时候从list头开始插，只在通常app上的话不加也无所谓
            currentLevel=LEVEL_COUNTY;
        }else {
            int provinceCode=selectedProvince.getProvinceCode();
            int cityCode=selectedCity.getCityCode();
            String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }

    }
}
