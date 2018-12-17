package com.hippo.jun.weandseoul;

/**
 * Created by JUN on 2018-07-16.
 */

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TabFragment2 extends Fragment {

    public TabFragment2(){
        // 빈 생성자 필수
    }

    String selectServerDomain = "http://tkstka0023.cafe24.com/select2.php";
    String result;

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mGridLayoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<String> mName = new ArrayList<>(), mContent = new ArrayList<>(), mTel = new ArrayList<>(),
                    mDate = new ArrayList<>(), mPlace = new ArrayList<>(), mNumber = new ArrayList<>(), mPosition = new ArrayList<>();
    ArrayList<RecyclerItem> items = new ArrayList<>();

    JSONArray arr;

    View view;
    RecyclerAdapter2 recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter2(getActivity(), items, new Intent(getActivity(), ShowMeetingActivity.class));
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
                mName.clear();
                mContent.clear();
                mTel.clear();
                mDate.clear();
                mPlace.clear();
                mNumber.clear();
                mPosition.clear();
                try {
                    Log.d("result", "doInBackground: "+result);
                    arr = new JSONObject(result).getJSONArray("info");
                    Log.d("arr.length", "doInBackground: "+arr.length());
                    for (int i = arr.length()-1; i >=0; i--) {
                        mName.add(arr.getJSONObject(i).getString("name"));
                        mContent.add(arr.getJSONObject(i).getString("content"));
                        mTel.add(arr.getJSONObject(i).getString("tel"));
                        mDate.add(arr.getJSONObject(i).getString("date"));
                        mPlace.add(arr.getJSONObject(i).getString("place"));
                        mNumber.add(arr.getJSONObject(i).getString("number"));
                        mPosition.add(arr.getJSONObject(i).getString("position"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //서버에서 추출한 값을 리사이클러 아이템에 초기화
                items.clear();
                for (int i=0 ; i<arr.length(); i++){
                    items.add(new RecyclerItem(mName.get(i), mContent.get(i), mTel.get(i), mDate.get(i), mPlace.get(i), mNumber.get(i), mPosition.get(i)));
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