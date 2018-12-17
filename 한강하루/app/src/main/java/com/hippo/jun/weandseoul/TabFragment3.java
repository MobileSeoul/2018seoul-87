package com.hippo.jun.weandseoul;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * Created by JUN on 2018-07-16.
 */

public class TabFragment3 extends Fragment {

    public TabFragment3(){
        // 빈 생성자 필수
    }

    int openNum;

    final String englishName []= {
            "yeouido", "banpo", "jamsil","nanji",
            "gangseo", "yanghwa", "mangwon", "ichon",
            "jamwon", "ttukseom", "gwangnaroo"
    };
    final String[] riverPark = {
            "여의도", "반포", "잠실", "난지",
            "강서", "양화", "망원", "이촌",
            "잠원", "뚝섬" , "광나루"
    };
//    PermissionListener permissionListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);


//        permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                    OpenRiverActivity(openNum);
//                }
//            }
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            }
//        };

//        TedPermission.with(getActivity())
//                .setPermissionListener(permissionListener)
//                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .setDeniedMessage("[설정] > [권한]에서 위치 권한을 허용해주세요.")
//                .check();


        String serverUrl = "http://tkstka0023.cafe24.com/app_image/";

        Button yeouidoBtn = view.findViewById(R.id.yeouidoBtn);
        Button banpoBtn = view.findViewById(R.id.banpoBtn);
        Button jamsilBtn = view.findViewById(R.id.jamsilBtn);
        Button nanjiBtn = view.findViewById(R.id.nanjiBtn);
        Button gangseoBtn = view.findViewById(R.id.gangseoBtn);
        Button yanghwaBtn = view.findViewById(R.id.yanghwaBtn);
        Button mangwonBtn = view.findViewById(R.id.mangwonBtn);
        Button ichonBtn = view.findViewById(R.id.ichonBtn);
        Button jamwonBtn = view.findViewById(R.id.jamwonBtn);
        Button ttukseomBtn = view.findViewById(R.id.ttukseomBtn);
        Button gwangnarooBtn = view.findViewById(R.id.gwangnarooBtn);

        yeouidoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 0;
                OpenRiverActivity(openNum);
            }
        });
        banpoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 1;
                OpenRiverActivity(openNum);
            }
        });
        jamsilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 2;
                OpenRiverActivity(openNum);
            }
        });
        nanjiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 3;
                OpenRiverActivity(openNum);
            }
        });
        gangseoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 4;
                OpenRiverActivity(openNum);
            }
        });
        yanghwaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 5;
                OpenRiverActivity(openNum);
            }
        });
        mangwonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 6;
                OpenRiverActivity(openNum);
            }
        });
        ichonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 7;
                OpenRiverActivity(openNum);
            }
        });
        jamwonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 8;
                OpenRiverActivity(openNum);
            }
        });
        ttukseomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 9;
                OpenRiverActivity(openNum);
            }
        });
        gwangnarooBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNum = 10;
                OpenRiverActivity(openNum);
            }
        });

        return view;
    }
    public void OpenRiverActivity(int num){
        Intent intent = new Intent(getActivity(), ParkActivity.class);
        intent.putExtra("parkNameEng", englishName[num]);
        intent.putExtra("parkNameKor", riverPark[num]);
        intent.putExtra("number", num);
        startActivity(intent);
    }
//    public void checkMyPermission(Context context){
//        TedPermission.with(context)
//                .setPermissionListener(permissionListener)
//                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
//                .setDeniedMessage("[설정] > [권한]에서 위치 권한을 허용해주세요.")
//                .check();
//    }
}