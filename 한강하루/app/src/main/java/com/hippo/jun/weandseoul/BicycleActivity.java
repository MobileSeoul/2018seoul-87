package com.hippo.jun.weandseoul;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BicycleActivity extends AppCompatActivity implements OnMapReadyCallback {

    String bName, bFee, bInfo, bTel, bBicycle, bAddr;
    TextView tName, tFee, tInfo, tTel, tBicycle, tAddr;
    double bXcode, bYcode;
    GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        Intent mIntent = getIntent();
        bName=mIntent.getStringExtra("name");
        bFee=mIntent.getStringExtra("fee");
        bInfo=mIntent.getStringExtra("info");
        bTel=mIntent.getStringExtra("tel");
        bBicycle=mIntent.getStringExtra("bicycle");
        bAddr=mIntent.getStringExtra("addr");
        bXcode=mIntent.getDoubleExtra("xcode", 36);
        bYcode=mIntent.getDoubleExtra("ycode", 127);

        tName=findViewById(R.id.name);
        tFee=findViewById(R.id.fee);
        tInfo=findViewById(R.id.info);
        tTel=findViewById(R.id.tel);
        tBicycle=findViewById(R.id.bicycle);
        tAddr=findViewById(R.id.addr);

        tName.setText(bName);
        tFee.setText(bFee);
        tInfo.setText(bInfo);
        tTel.setText(bTel);
        tBicycle.setText(bBicycle);
        tAddr.setText(bAddr);

        //fragment의 ID를 참조하고 구글맵을 호출한다.
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        // getMapAsync 는 반드시 메인스레드에서 실행되어야한다.
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        // 좌표에 대한 위치 설정
        LatLng place = new LatLng(bXcode, bYcode);

        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions
                .position(place)
                .title(bName);

        //마커 서브 제목
        //makerOptions.snippet("한국의 수도");

        // 마커를 생성한다.
        mMap.addMarker(makerOptions);

        //카메라를 서울 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        //줌 설정
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
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
