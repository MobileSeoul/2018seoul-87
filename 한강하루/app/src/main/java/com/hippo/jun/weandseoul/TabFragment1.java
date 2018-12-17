package com.hippo.jun.weandseoul;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TabFragment1 extends Fragment {


    public TabFragment1(){
        // 빈 생성자 필수
    }

    String selectServerDomain = "http://tkstka0023.cafe24.com/select.php";
    String uploadServerDomain = "http://tkstka0023.cafe24.com/uploads/";
    String result;

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mGridLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> userId = new ArrayList<>(), userText = new ArrayList<>(), userImage = new ArrayList<>();
    ArrayList<RecyclerItem> items = new ArrayList<>();

    JSONArray arr;

    View view;
    RecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter(getActivity(), items, new Intent(getActivity(), ShowTodayActivity.class));
        mRecyclerView.setAdapter(recyclerAdapter);

        //페이지 당겨서 새로고침
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //새로고침 후 실행할 동작
                NetworkTask networkTask = new NetworkTask();
                networkTask.execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        public NetworkTask() {
        }
        @Override
        protected String doInBackground(Void... voids) {
            try{
                RequestServer rs = new RequestServer(selectServerDomain);
                result = rs.request();
                if (result.equals("ResponseError")){
                    return result;
                }
                else if(result.equals("MalformedURL")){
                    return result;
                }
                else if (result.equals("IOException")){
                    return result;
                }
                userImage.clear();
                userId.clear();
                userText.clear();
                try {
                    Log.d("result", "doInBackground: "+result);
                    arr = new JSONObject(result).getJSONArray("info");
                    Log.d("arr.length", "doInBackground: "+arr.length());
                    for (int i = arr.length()-1; i >=0; i--) {
                        userId.add(arr.getJSONObject(i).getString("id"));
                        userText.add(arr.getJSONObject(i).getString("text"));
                        userImage.add(arr.getJSONObject(i).getString("image"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //서버에서 추출한 값을 리사이클러 아이템에 초기화
                items.clear();
                for (int i=0 ; i<arr.length(); i++){
                    items.add(new RecyclerItem(uploadServerDomain+userImage.get(i), userImage.get(i), userId.get(i), userText.get(i)));
//                    Log.d("userImage.get(i)", "doInBackground: "+userImage.get(i));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return "FAIL";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("ResponseError")){
                Log.d("ResponseError", "onPostExecute: "+s);
            }
            else if(s.equals("MalformedURL")){
                Log.d("MalformedURL", "onPostExecute: "+s);
            }
            else if (s.equals("IOException")){
                Log.d("IOException", "onPostExecute: "+s);
            }
            recyclerAdapter.refreshAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "onResume: 0000");
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();
    }
}