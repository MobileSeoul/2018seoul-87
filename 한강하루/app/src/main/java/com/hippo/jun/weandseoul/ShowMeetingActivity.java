package com.hippo.jun.weandseoul;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class ShowMeetingActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    TextView tName, tTel, tContent, tDate, tNum, tPlace;
    String pName, pContent, pTel, pDate, pPlace, pNum, pDelete_key;

    double[] xCode = {37.526538, 37.510500, 37.518206, 37.566460, 37.586119, 37.538332, 37.556007,
            37.516255, 37.519745, 37.529233, 37.550035};
    double[] yCode = {126.933636, 126.995555, 127.081976, 126.876368, 126.817154, 126.902269,
            126.894613, 126.975918, 127.009863, 127.069977, 127.121628};

    int codePosition;

    String deleteServerDomain = "http://tkstka0023.cafe24.com/delete2.php";

    String value; //사용자가 게시물을 삭제하기 위해 입력한 비밀번호
    ContentValues params; //전달할 이미지와 비밀번호 값
    String result; //통신결과

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_meeting);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        tName = findViewById(R.id.name);
        tTel = findViewById(R.id.tel);
        tContent = findViewById(R.id.content);
        tNum = findViewById(R.id.number);
        tPlace = findViewById(R.id.place);
        tDate = findViewById(R.id.date);

        Intent mIntent = getIntent();
        pName=mIntent.getStringExtra("name");
        pContent=mIntent.getStringExtra("content");
        pTel=mIntent.getStringExtra("tel");
        pDate=mIntent.getStringExtra("date");
        pPlace=mIntent.getStringExtra("place");
        pNum=mIntent.getStringExtra("number");
        pDelete_key=mIntent.getStringExtra("delete_key");
        codePosition=Integer.parseInt(mIntent.getStringExtra("position"));

        tName.setText(pName);
        tContent.setText(pContent);
        tTel.setText(pTel);
        tNum.setText(pNum);
        tPlace.setText(pPlace);
        tDate.setText(pDate);

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

        // 서울에 대한 위치 설정
        LatLng place = new LatLng(xCode[codePosition], yCode[codePosition]);

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
    public void showDialog(){
        AlertDialog.Builder ad = new AlertDialog.Builder(ShowMeetingActivity.this);

        ad.setTitle("게시물 삭제");       // 제목 설정
        ad.setMessage("게시물 비밀번호를 입력하세요");   // 내용 설정

        // EditText 삽입하기
        final EditText et = new EditText(ShowMeetingActivity.this);

        ad.setView(et);

        // 확인 버튼 설정
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받기
                value = et.getText().toString();
                //삭제 진행
                params = new ContentValues();
                params.put("name", pName);
                params.put("content", pContent);
                params.put("delete_key", value);
                NetworkTask networkTask = new NetworkTask(params);
                networkTask.execute();
                dialog.dismiss();
                finish();
            }
        });

        // 취소 버튼 설정
        ad.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        // 창 띄우기
        ad.show();
    }
    public class NetworkTask extends AsyncTask<Void, Void, String> {

        ContentValues params;

        public NetworkTask(ContentValues getParams) {
            params = getParams;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                RequestServer2 rs = new RequestServer2(deleteServerDomain);
                result = rs.request(params);
                Log.d("doInBackground show", "doInBackground: "+result);
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                return "FAIL";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    /**오른쪽 위에 옵션 메뉴 필요할 때 구현 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    /**오른쪽 옵션메뉴 아이템 클릭 시 동작 구현*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_file) {
            showDialog();
            return true;
        } else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
