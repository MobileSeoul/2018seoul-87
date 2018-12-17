package com.hippo.jun.weandseoul;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    DrawerLayout drawer;
//    ActionBarDrawerToggle toggle; //왼쪽에 햄버거메뉴
    TabLayout tabLayout;
    TabPagerAdapter pagerAdapter;
    public final static int ADD_DREAM_REQUEST = 1111;
    int setVisible = View.GONE;
    Bitmap decodedBitmap = null;
    byte[] decodeByteArray;
    String dreamName;
    int [] uDate;
    boolean isFabOpen;
    FloatingActionButton mainFab, addMeetingFab, addTodayFab;
    View fabView;
    TextView dreamText, dailyText;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        appBarLayout = findViewById(R.id.appbar);

        dailyText = findViewById(R.id.dailyText);
        dreamText = findViewById(R.id.dreamText);
        addMeetingFab = findViewById(R.id.addMeetingFab);
        addTodayFab = findViewById(R.id.addTodayFab);
        fabView = findViewById(R.id.fabView);
        mainFab = findViewById(R.id.mainFab);

        //fab 클릭 시 텍스트 위치
        dailyText.setTranslationX(-280);
        dailyText.setTranslationY(-200);

        dreamText.setTranslationX(-280);
        dreamText.setTranslationY(-400);




        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFabOpen){
                    ShowFabMenu();
                }
                else{
                    CloseFabMenu();
                }
            }
        });
        addTodayFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseFabMenu();
                setDelay(new Intent(getApplicationContext(), AddTodayActivity.class), 300);
            }
        });

        addMeetingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseFabMenu();
                setDelay(new Intent(getApplicationContext(), AddMeetingActivity.class), 300);
            }
        });
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseFabMenu();
            }
        });

        final int changeableIcon[]={
                R.drawable.selector_moon,
                R.drawable.selector_people,
                R.drawable.selector_river,
                R.drawable.selector_cloud,
                R.drawable.selector_bicycle
        };

        final String selectedTabText[]={
                "일상",
                "모임",
                "한강공원",
                "대기정보",
                "자전거대여"
        };

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //선택된 탭 변수
        final View[] tabView = new View[5];
        TextView[] tabText = new TextView[5];
        ImageView[] tabImage = new ImageView[5];

        //선택된 탭
        for(int i=0; i<5; i++){
            tabView[i] = (getLayoutInflater().inflate(R.layout.custom_tablayout, null));
            tabText[i] = (TextView) tabView[i].findViewById(R.id.tabText);
            tabText[i].setText(selectedTabText[i]);
            tabImage[i] = (ImageView) tabView[i].findViewById(R.id.tabIcon);
            tabImage[i].setImageResource(changeableIcon[i]);
        }

        //탭 추가
        for (int i=0; i<5; i++){
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabView[i]));
        }

        TextView tv = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tabText);
        ImageView iv = tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tabIcon);

        tv.setTextColor(0xff000000);
//        tv.setVisibility(View.VISIBLE);
//        iv.setPadding(a/4,a,a/4,a/4);


//        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(pagerAdapter);
//        //양 옆 2페이지까지 로딩해놓음.
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //첫 번째 탭을 처음에 나오게 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, pagerAdapter.getItem(0)).commit();

        //디바이스 크기 측정
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int deviceWidthPx = size.x;

        //인덱스 1, 2번 탭 사이에 margin 만들기
//        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
//
//        View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1);
//        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//        p.setMargins(0, 0, deviceWidthPx/10, 0);
//        tab.requestLayout();
//
//        tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(2);
//        p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//        p.setMargins(deviceWidthPx/10, 0, 0, 0);
//        tab.requestLayout();

        final int str[] = {
                R.string.tab1text,
                R.string.tab2text,
                R.string.tab3text,
                R.string.tab4text,
                R.string.tab5text
        };

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                viewPager.setCurrentItem(position);
                TextView subText = findViewById(R.id.subtext);
                TextView tv = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tabText);
                ImageView iv = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tabIcon);
                subText.setText(str[position]);
                tv.setVisibility(View.VISIBLE);
                tv.setTextColor(0xff000000);
                if(position == 0 || position == 1){
                    mainFab.show();
                }else{
                    mainFab.hide();
                }
//                iv.setPadding(a/4,a,a/4,a/4);

                getSupportFragmentManager().beginTransaction().replace(R.id.container, pagerAdapter.getItem(position)).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                TextView tv = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tabText);
                ImageView iv = tabLayout.getTabAt(position).getCustomView().findViewById(R.id.tabIcon);
//                tv.setVisibility(View.GONE);
                tv.setTextColor(0xffcccccc);
//                iv.setPadding(b,b,b,b);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setDelay(final Intent intent, int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, delay);
    }

    private void setDelay(final Intent intent, final int requestCode, int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(intent, requestCode);
            }
        }, delay);
    }
    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

//    public static int convertPixelsToDp(float px, Context context){
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        float dp = px / (metrics.densityDpi / 160f);
//        return Math.round(dp);
//    }

    private void ShowFabMenu() {
        isFabOpen = true;
        addMeetingFab.setVisibility(View.VISIBLE);
        addTodayFab.setVisibility(View.VISIBLE);
        fabView.setVisibility(View.VISIBLE);

        dreamText.setVisibility(View.VISIBLE);
        dailyText.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.GONE);

        dreamText.setAlpha(1f);
        dailyText.setAlpha(1f);
        fabView.setAlpha(1f);
        mainFab.setImageResource(R.drawable.ic_plus);
        mainFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        mainFab.animate().rotation(135f);
        addTodayFab.animate().translationY(-200).rotation(0f);
        addMeetingFab.animate().translationY(-400).rotation(0f);
    }

    private void CloseFabMenu() {
        isFabOpen = false;
        View[] v = {fabView, addTodayFab, addMeetingFab, dreamText, dailyText};

        dreamText.setAlpha(0f);
        dailyText.setAlpha(0f);
        fabView.setAlpha(0f);
        appBarLayout.setVisibility(View.VISIBLE);
        mainFab.animate().rotation(0f);
        mainFab.setImageResource(R.drawable.ic_plus2);
        mainFab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5cd6f8")));
        addTodayFab.animate().translationY(0).rotation(90f);
        addMeetingFab.animate().translationY(0).rotation(90f).setListener(
                new FabAnimatorListener(v));
    }
    private class FabAnimatorListener implements Animator.AnimatorListener{
        View[] viewsToHide;

        public FabAnimatorListener(View[] viewsToHide){
            this.viewsToHide = viewsToHide;
        }
        @Override
        public void onAnimationCancel(Animator animation) {
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            if(!isFabOpen){
                for (View view: viewsToHide){
                    view.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void onAnimationRepeat(Animator animation) {
        }
        @Override
        public void onAnimationStart(Animator animation) {
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (isFabOpen){
            CloseFabMenu();
        }
        else {
            super.onBackPressed();
        }
    }
    public void gotoSetting(){
        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse("package:" + getPackageName())));
    }

    /**오른쪽 위에 옵션 메뉴 필요할 때 구현 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_settings) {
            gotoSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            Log.d("RESULT ERROR", "onActivityResult: ERROR");
            return;
        }
        if(requestCode == ADD_DREAM_REQUEST){
            dreamName = data.getStringExtra("dream");
            setVisible = data.getIntExtra("result", View.GONE);
            decodeByteArray = data.getByteArrayExtra("image");
            uDate = data.getIntArrayExtra("date");

            if (decodeByteArray != null) { //이미지 받은경우
                decodedBitmap = BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pagerAdapter.notifyDataSetChanged();
    }


}
