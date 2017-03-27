package com.example.mylocation.overly;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.mylocation.R;

import java.util.List;

public class PoiLocation extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView;
    private Button poiSearch;
    private PoiSearch mPoiSearch;

    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poilocation);
        poiSearch = (Button) findViewById(R.id.poiClick);
        poiSearch.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mapView.getMap();
        mPoiSearch = PoiSearch.newInstance();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);




        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
            public void onGetPoiResult(PoiResult result){
                //获取POI检索结果
                List<PoiInfo> allPoi = result.getAllPoi();
                for (PoiInfo p:allPoi) {
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
                    //创建一个图层选项
                    OverlayOptions options = new MarkerOptions().position(p.location).icon(bitmapDescriptor);
                    mBaiduMap.addOverlay(options);
                    Log.d("PoiLocation", p.name);
                }
            }
            public void onGetPoiDetailResult(PoiDetailResult result){
                //获取Place详情页检索结果

            }
        };

        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

       

    }

    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mPoiSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onClick(View v) {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("美食")
                .pageNum(10));
    }
}
