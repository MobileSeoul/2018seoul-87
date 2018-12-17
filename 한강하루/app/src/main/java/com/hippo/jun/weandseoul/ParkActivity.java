package com.hippo.jun.weandseoul;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class ParkActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TextView parkName;
    ImageView parkImage;

    String parkNameEng;
    PermissionListener permissionListener;

    int openNum; //한강 공원 번호 0~10번까지
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼


//        permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
////                    OpenRiverActivity(new Intent(getActivity(), ParkActivity.class));
//                }
//            }
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            }
//        };
//        TedPermission.with(this)
//                .setPermissionListener(permissionListener)
//                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setDeniedMessage("[설정] > [권한]에서 카메라 및 저장공간 권한을 허용해주세요.")
//                .check();


        parkImage = (ImageView) findViewById(R.id.parkImage);
        parkName = (TextView) findViewById(R.id.parkName);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        String englishName []= {
                "yeouido", "banpo", "jamsil","nanji",
                "gangseo", "yanghwa", "mangwon", "ichon",
                "jamwon", "ttukseom", "gwangnaroo"
        };
        Intent mIntent = getIntent();
        openNum = mIntent.getIntExtra("number", 0);
        String subName = " 한강공원";
        //인텐트로 전달받은 공원이름에 " 한강공원"이라는 텍스트를 추가
        String parkFullName = mIntent.getStringExtra("parkNameKor")+subName;
        //합쳐진 텍스트를 툴바의 제목으로 지정
        parkName.setText(parkFullName);

        //탭에게 전달할 count 값과 공원이름 영문명을 변수 parkStringText에 저장
//        count = Integer.parseInt(mIntent.getStringExtra("count"));

        //인텐트로 받은 공원의 영문명을 저장
        parkNameEng = mIntent.getStringExtra("parkNameEng");

        //CDN 서버의 url
        String cdn3Url = "http://tkstka0023.cdn3.cafe24.com/app_image/";
        //글라이드로 서버에 있는 이미지를 로딩하여 툴바에 설정
        Glide.with(getApplicationContext()).load(cdn3Url+parkNameEng+".jpg").into(parkImage);

        final String innerTabText[]={
                "몽땅축제정보",
                "운동시설",
                "푸드트럭"
        };
        for (String text:innerTabText) {
            tabLayout.addTab(tabLayout.newTab().setText(text));
        }

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        InsideTabPagerAdapter insideTabPagerAdapter = new InsideTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(insideTabPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
