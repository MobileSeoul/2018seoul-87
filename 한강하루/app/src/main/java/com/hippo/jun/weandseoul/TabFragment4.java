package com.hippo.jun.weandseoul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hippo.jun.weandseoul.R;

import kr.go.seoul.airquality.AirQualityButtonTypeB;
import kr.go.seoul.airquality.AirQualityTypeMini;

/**
 * Created by JUN on 2018-07-16.
 */


public class TabFragment4 extends Fragment {

    public TabFragment4(){
        // 빈 생성자 필수
    }

    String key = "517968486e746b733937724a54714b";
    AirQualityButtonTypeB typeB;
    AirQualityTypeMini typeMini;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_4, container, false);

        typeB = (AirQualityButtonTypeB) view.findViewById(R.id.type_b);
        typeMini = (AirQualityTypeMini) view.findViewById(R.id.type_mini);

        typeMini.setOpenAPIKey(key);
        typeB.setOpenAPIKey(key);
        typeB.setButtonImage(R.drawable.button_grd_layout);

        return view;
    }
}