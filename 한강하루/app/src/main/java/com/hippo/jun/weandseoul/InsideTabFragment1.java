package com.hippo.jun.weandseoul;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InsideTabFragment1 extends Fragment {

    public InsideTabFragment1(){
        // 빈 생성자 필수
    }

    String domain = "http://tkstka0023.cafe24.com/park/";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inside_tab_fragment1, container, false);

        String parkNameEng;
        ArrayList<String> pName;
        int openNum;

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mGridLayoutManager;

        pName = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);

        //park액티비티에서 공원의 영문명을 가져온다.
        parkNameEng = ((ParkActivity)getActivity()).parkNameEng;
        //park액티비티에서 공원의 번호를 가져온다.
        openNum = ((ParkActivity)getActivity()).openNum;

        Log.d("parkNameEng", "onCreateView: "+parkNameEng);

        ArrayList<RecyclerItem> items = new ArrayList<>();

        try{
            //json파일 로드
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            //json파일에서 해당하는 공원 이름의 데이터 배열을 받아온다.
            JSONArray arr = obj.getJSONArray(parkNameEng.toUpperCase());

            //배열의 크기에 맞게 for문 실행하고 NAME 데이터를 받아온다.
            for(int i=0 ; i<arr.length(); i++){
                pName.add(arr.getJSONObject(i).getString("NAME"));
            }
            //배열의 크기만큼 리사이클러뷰를 생성. 각각의 제목은 json 에서 받아온 이름.
            for(int i=0 ; i<arr.length(); i++){
                items.add(new RecyclerItem(domain+parkNameEng+String.valueOf(i+1)+".jpg", pName.get(i)));
                Log.d("전송이미지", "onCreateView: "+domain+parkNameEng+String.valueOf(i+1)+".jpg");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(getActivity(), PlaceInfoActivity1.class);
        intent.putExtra("parkNameEng", parkNameEng);
        intent.putExtra("number", openNum);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerAdapter3 recyclerAdapter = new RecyclerAdapter3(getActivity(), items, intent);
        mRecyclerView.setAdapter(recyclerAdapter);


        return view;
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("festival.json");
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
