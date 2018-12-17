package com.hippo.jun.weandseoul;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hippo.jun.weandseoul.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by JUN on 2018-07-16.
 */


public class TabFragment5 extends Fragment {

    public TabFragment5(){
        // 빈 생성자 필수
    }

    ArrayList<String> bName = new ArrayList<>();
    ArrayList<String> bFee = new ArrayList<>();
    ArrayList<String> bInfo = new ArrayList<>();
    ArrayList<String> bTel = new ArrayList<>();
    ArrayList<String> bBicycle = new ArrayList<>();
    ArrayList<String> bAddr = new ArrayList<>();
    ArrayList<Double> bXcode = new ArrayList<>();
    ArrayList<Double> bYcode = new ArrayList<>();

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mGridLayoutManager;
    RecyclerView.Adapter mAdapter;
    RecyclerAdapter4 recyclerAdapter;

    ArrayList<RecyclerItem> items = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_5, container, false);

        //한강공원 순서
        //여의 반포 잠실 난지 강서 양화 망원 이촌 잠원 뚝섬 광나루

        try{
            //json파일 로드
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            //json파일안에 BICYCLE 배열을 받아서 변수 a에 넣음
            JSONArray a = obj.getJSONArray("PARK");
            for(int i=0 ; i<a.length(); i++){
                bName.add(a.getJSONObject(i).getString("NAME"));
                bFee.add(a.getJSONObject(i).getString("FEE"));
                bInfo.add(a.getJSONObject(i).getString("INFO"));
                bTel.add(a.getJSONObject(i).getString("TEL"));
                bBicycle.add(a.getJSONObject(i).getString("BICYCLE"));
                bAddr.add(a.getJSONObject(i).getString("ADDR"));
                bXcode.add(Double.parseDouble(a.getJSONObject(i).getString("XCODE")));
                bYcode.add(Double.parseDouble(a.getJSONObject(i).getString("YCODE")));

            }
            for (int i=0; i<a.length(); i++){
                items.add(new RecyclerItem(bName.get(i), bFee.get(i), bInfo.get(i), bTel.get(i), bBicycle.get(i), bAddr.get(i), bXcode.get(i), bYcode.get(i)));
            }
            Log.d("onCreateView", "onCreateView: "+a.getJSONObject(0).getString("NAME"));
            Log.d("onCreateView", "onCreateView: "+a.getJSONObject(1).getString("FEE"));

        }catch (Exception e){
            e.printStackTrace();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter4(getActivity(), items, new Intent(getActivity(), BicycleActivity.class));
        mRecyclerView.setAdapter(recyclerAdapter);

        return view;
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("bicycle.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}