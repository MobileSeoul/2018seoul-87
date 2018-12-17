package com.hippo.jun.weandseoul;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class PlaceInfoActivity3 extends AppCompatActivity implements OnMapReadyCallback {

    TextView tv_Name, tv_Addr_Old, tv_Addr;
    String pName, pAddr_Old, pAddr;
    double pXcode, pYcode;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info3);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        tv_Name = findViewById(R.id.name);
        tv_Addr_Old = findViewById(R.id.addr_old);
        tv_Addr = findViewById(R.id.addr);

        Intent mIntent = getIntent();
        String parkNameEng = mIntent.getStringExtra("parkNameEng");
        int position = mIntent.getIntExtra("position", 101);

        try{
            //json파일 로드
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            //json파일에서 해당하는 공원 이름의 데이터 배열을 받아온다.
            JSONArray arr = obj.getJSONArray(parkNameEng.toUpperCase());

            //인텐트로 받은 position에 해당하는 데이터(이름/전화번호/이용정도/X,Y좌표)를 받아온다.
            pName = arr.getJSONObject(position).getString("NAME");
            pAddr_Old = arr.getJSONObject(position).getString("ADDR_OLD");
            pAddr = arr.getJSONObject(position).getString("ADDR");
            pXcode = arr.getJSONObject(position).getDouble("XCODE");
            pYcode = arr.getJSONObject(position).getDouble("YCODE");

            tv_Name.setText(pName);
            tv_Addr_Old.setText(pAddr_Old);
            tv_Addr.setText(pAddr);

            Log.d("pXcode", "onCreate: "+pXcode);
            Log.d("pYcode", "onCreate: "+pYcode);


        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        // 서울에 대한 위치 설정
        LatLng place = new LatLng(pXcode, pYcode);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(place)
                .title(pName);

        //마커 서브 제목
        //makerOptions.snippet("한국의 수도");

        // 마커를 생성한다.
        mMap.addMarker(makerOptions);

        //카메라를 서울 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        //줌 설정
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("food_truck.json");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 뒤로가기 버튼 눌렀을 때
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
