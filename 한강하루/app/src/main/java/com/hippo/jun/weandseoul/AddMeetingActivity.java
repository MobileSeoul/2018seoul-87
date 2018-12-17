package com.hippo.jun.weandseoul;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddMeetingActivity extends AppCompatActivity {


    Calendar c = Calendar.getInstance();
    public final int cDate[] = {c.get(Calendar.YEAR), c.get(Calendar.MONTH) , c.get(Calendar.DAY_OF_MONTH)}; //현재 년, 월, 일 설정
    int[] uDate; //사용자가 입력한 년, 월, 일

    TextView dateSet, dateTxt, submitBtn;

    public ProgressDialog dialog = null;

    String mName, mContent, mTel, mNumber, mPlace, mDelete_key;
    EditText eName, eContent, eTel, eNumber, eDelete_key;

    Spinner spinner;
    String[] items =
            {"여의도 한강공원", "반포 한강공원", "잠실 한강공원", "난지 한강공원", "강서 한강공원", "양화 한강공원",
            "망원 한강공원", "이촌 한강공원", "잠원 한강공원", "뚝섬 한강공원", "광나루 한강공원"};

    int codePosition;

    String result;
    String uploadServerDomain = "http://tkstka0023.cafe24.com/UploadToServer2.php";
    ContentValues params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        eName = findViewById(R.id.meeting_name);
        eContent = findViewById(R.id.meeting_content);
        eTel = findViewById(R.id.meeting_tel);
        eNumber = findViewById(R.id.meeting_number);
        eDelete_key = findViewById(R.id.delete_key);

        final ImageView calenderImg = findViewById(R.id.i6);

        dateTxt = (TextView) findViewById(R.id.dateTxt);
        dateSet = (TextView) findViewById(R.id.dateSet);
        submitBtn = (TextView) findViewById(R.id.submitBtn);

        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(year < cDate[0]) {//년도가 작은 경우
                    Toast.makeText(AddMeetingActivity.this, "오늘 이후의 날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                }
                else if(year > cDate[0]){ //년도가 큰 경우
                    doDateSet(year, monthOfYear, dayOfMonth);
                    UpdateNow();
                    ifSucceed(calenderImg);
                }
                else { //년도 같은 경우
                    if(monthOfYear < cDate[1]) { //월이 더 작은 경우
                        Toast.makeText(AddMeetingActivity.this, "오늘 이후의 날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(monthOfYear > cDate[1]) { //월이 큰 경우
                        doDateSet(year, monthOfYear, dayOfMonth);
                        UpdateNow();
                        ifSucceed(calenderImg);
                    }
                    else { //월이 같은 경우
                        if(dayOfMonth >= cDate[2]){ //일수가 같거나 큰 경우
                            doDateSet(year, monthOfYear, dayOfMonth);
                            UpdateNow();
                            ifSucceed(calenderImg);
                        }
                        else {
                            Toast.makeText(AddMeetingActivity.this, "오늘 이후의 날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

        dateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMeetingActivity.this, mDateSetListener, cDate[0], cDate[1], cDate[2]);
                datePickerDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = eName.getText().toString();
                mContent = eContent.getText().toString();
                mNumber = eNumber.getText().toString();
                mTel = eTel.getText().toString();
                mDelete_key = eDelete_key.getText().toString();


                if (!mName.replace(" ", "").equals("") && !mContent.replace(" ", "").equals("")
                        && !mTel.replace(" ", "").equals("") && !mNumber.replace(" ", "").equals("")
                        && !Arrays.equals(uDate, null)) {

                    dialog = ProgressDialog.show(AddMeetingActivity.this, "", "Uploading file...", true);
                    new Thread(new Runnable() {
                        public void run() {

                            // 날짜를 yyyymmdd 형태로 전달하기 위함.
                            String year, month, day, date;
                            year = String.valueOf(uDate[0]);
                            month = String.valueOf(uDate[1]+1);
                            day = String.valueOf(uDate[2]);

                            if(month.length() == 1){
                                month = "0"+month;
                            }
                            if (day.length() == 1){
                                day = "0"+day;
                            }

                            date = year+month+day;

                            Log.d("final date", "onClick: "+date);

                            //전달할 내용들..
                            params = new ContentValues();
                            params.put("name", mName);
                            params.put("content", mContent);
                            params.put("tel", mTel);
                            params.put("date", date);
                            params.put("place", mPlace);
                            params.put("number", mNumber);
                            params.put("delete_key", mDelete_key);
                            params.put("position", String.valueOf(codePosition));
                            Log.d("내용", "run: "+mName+" "+mContent+" "+mTel+" "+date+" "+mPlace+" "+mNumber+" "+mDelete_key+" "+codePosition);
                            NetworkTask networkTask = new NetworkTask(params);
                            networkTask.execute();
                            finish();
                            dialog.dismiss();
                        }
                    }).start();

//                    finish();
                }
                //모임 이름 입력 안했을 때
                else if (mName.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "모임 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //모임 소개 입력 안했을 때
                else if (mContent.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "모임 소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //모임장 번호 입력 안했을 때
                else if (mTel.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "모임장 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //모임 인원 입력 안했을 때
                else if (mNumber.replace(" ", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "모임 인원을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //날짜 선택 안했을 때
                else if (Arrays.equals(uDate, null)) {
                    Toast.makeText(getApplicationContext(), "목표 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codePosition = position;
                mPlace = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }
    public void ifSucceed(ImageView img){
        dateTxt.setTextColor(0xFF5cd6f8);
        img.setImageResource(R.drawable.ic_calendar2);
    }
    public void doDateSet(int year, int month, int day){
        uDate = new int[]{year, month, day};
    }
    public void UpdateNow() {
        dateTxt.setText(String.format(Locale.US, "%d년  %d월  %d일", uDate[0], uDate[1] + 1, uDate[2]));
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

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        ContentValues params;

        public NetworkTask(ContentValues getParams) {
            params = getParams;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                RequestServer2 rs = new RequestServer2(uploadServerDomain);
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

}
