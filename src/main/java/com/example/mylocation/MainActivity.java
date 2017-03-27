package com.example.mylocation;

import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener {

    private MapView mapView;
    private Button poiClick, nextClick;

    private int nodeIndex = -2;// 节点索引,供浏览节点时使用
    private List<String> busLineIDList = null;
    private int busLineIndex = 0;

    //第一步，创建POI对象
    private PoiSearch mPoiSearch;


    private BusLineSearch mBusLineSearch;

    private String busLineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);

        poiClick = (Button) findViewById(R.id.poiClick);
        poiClick.setOnClickListener(this);

        //第二步，创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();


        mBusLineSearch = BusLineSearch.newInstance();

        mBusLineSearch.setOnGetBusLineSearchResultListener(this);

        mPoiSearch.setOnGetPoiSearchResultListener(this);

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
       /* mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("ktv")
                .pageNum(10));*/
        switch (v.getId()) {
            case R.id.poiClick:
                poiClick();

                break;
            case R.id.nextClick:
                break;
        }

    }


    private void poiClick() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("717"));

    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, result.getBusLineName(),
                Toast.LENGTH_SHORT).show();
        Log.d("MainActivity", result.toString());
        List<BusLineResult.BusStation> steps = result.getStations();
        StringBuffer sb = new StringBuffer();
        for (BusLineResult.BusStation b:steps) {
            sb.append("-->");
            sb.append(b.getTitle());
        }
        Log.d("MainActivity", sb.toString());
    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 遍历所有poi，找到类型为公交线路的poi
        for (PoiInfo poi : result.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE
                    || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                //如下代码为发起检索代码，定义监听者和设置监听器的方法与POI中的类似
                mBusLineSearch.searchBusLine((new BusLineSearchOption()
                        //我这里的城市写死了，和我要查的是一样的
                        .city("北京")
                        .uid(poi.uid)));
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }
}
