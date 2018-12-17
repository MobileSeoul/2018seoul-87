package com.hippo.jun.weandseoul;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

public class ShowTodayActivity extends AppCompatActivity {


    String deleteServerDomain = "http://tkstka0023.cafe24.com/delete.php";

    String value; //사용자가 게시물을 삭제하기 위해 입력한 비밀번호
    String filename;
    ContentValues params; //전달할 이미지와 비밀번호 값

    String result; //통신결과

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_show_today);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        TextView todayText = findViewById(R.id.todayText);
        ImageView todayImage = findViewById(R.id.todayImage);
        TextView filename_tv = findViewById(R.id.filename);

        Intent mIntent = getIntent();

        String userId = mIntent.getStringExtra("id");
        String userText = mIntent.getStringExtra("text");
        String userImage = mIntent.getStringExtra("image");
        filename = mIntent.getStringExtra("filename");
//        user_num = mIntent.getStringExtra("user_num");

        todayText.setText(userText);
        filename_tv.setText(filename);
//        user_num_tv.setText(user_num);
        Glide.with(this).load(userImage).fitCenter().into(todayImage);

        Log.d("filename", "onCreate: "+filename);
        Log.d("userImage", "onCreate: "+userImage);


    }
    public void showDialog(){
        AlertDialog.Builder ad = new AlertDialog.Builder(ShowTodayActivity.this);

        ad.setTitle("게시물 삭제");       // 제목 설정
        ad.setMessage("게시물 비밀번호를 입력하세요");   // 내용 설정

        // EditText 삽입하기
        final EditText et = new EditText(ShowTodayActivity.this);
        ad.setView(et);

        // 확인 버튼 설정
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받기
                value = et.getText().toString();
                //삭제 진행
                params = new ContentValues();
                params.put("user_image", filename);
                params.put("user_num", value);
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
