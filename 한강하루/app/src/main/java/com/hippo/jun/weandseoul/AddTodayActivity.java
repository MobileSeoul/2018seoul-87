package com.hippo.jun.weandseoul;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTodayActivity extends AppCompatActivity {


    ImageView uploadBtn; //업로드한 사진이 출력되는 영역
    ImageView gallery;
    TextView photoTxt, submitBtn;

    PermissionListener permissionListener; //권한 설정 TED 활용

    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수
    Uri photoUri;
    File tempFile; //임시 파일 삭제하기 위함.

    public final static int ADD_TODAY_REQUEST = 1112;

    byte[] imageBytes = null;

    String uploadFileUri = null; //전송할 이미지 Uri
    public ProgressDialog dialog = null;

    int deviceWidth;
    LinedEditText linedEditText;
    EditText user_num_et;
    String getTodayText;
    String user_image;
    String user_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_today);

        //디바이스 크기 측정
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;

        user_num_et = findViewById(R.id.user_num);

        linedEditText = findViewById(R.id.l1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back2); //뒤로가기 버튼

        submitBtn = (TextView) findViewById(R.id.submitBtn);
        uploadBtn  = (ImageView) findViewById(R.id.uploadBtn);
        gallery = (ImageView) findViewById(R.id.gallery);
        photoTxt = (TextView) findViewById(R.id.photoTxt);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTodayText = linedEditText.getText().toString();
                user_num = user_num_et.getText().toString();

                if (!getTodayText.replace(" ", "").equals("") && gallery.getVisibility() == View.GONE){

                    dialog = ProgressDialog.show(AddTodayActivity.this, "", "Uploading file...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            GeoPictureUploader gpu = new GeoPictureUploader("hippo", getTodayText, user_image, user_num);
                            gpu.uploadPicture(uploadFileUri);
                            finish();
                            dialog.dismiss();
                        }
                    }).start();
                }
                //텍스트 입력 안했을 때
                else if(getTodayText.replace(" ", "").equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                //이미지 선택 안했을 때
                else if(gallery.getVisibility() == View.VISIBLE){
                    Toast.makeText(getApplicationContext(), "사진을 추가해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        final DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();
            }
        };
        final DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToAlbum();
            }
        };
        final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                uploadDialog(cameraListener, albumListener, cancelListener);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(AddDreamActivity.this, "거부된 권한\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //업로드 버튼 클릭 시
                TedPermission.with(AddTodayActivity.this)
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setDeniedMessage("[설정] > [권한]에서 카메라 및 저장공간 권한을 허용해주세요.")
                        .check();
            }
        });

        linedEditText.setSelection(linedEditText.getText().length()); //커서 위치 맨뒤로

//        linedEditText.getLeft();
//        int paddingTop = linedEditText.getPaddingTop();
//        int paddingBottom = linedEditText.getPaddingBottom();
//        int height = linedEditText.getHeight();
//        int lineHeight = linedEditText.getLineHeight();
//        int count = (height-paddingTop-paddingBottom) / lineHeight;

    }

    public void uploadDialog(DialogInterface.OnClickListener cameraListener, DialogInterface.OnClickListener albumListener, DialogInterface.OnClickListener cancelListener){
        if(ContextCompat.checkSelfPermission(AddTodayActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(AddTodayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(AddTodayActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            new AlertDialog.Builder(AddTodayActivity.this)
                    .setTitle("업로드할 이미지 선택").setPositiveButton("사진촬영",cameraListener)
                    .setNegativeButton("앨범선택", albumListener)
                    .setNeutralButton("취소", cancelListener).show();
        }
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(AddTodayActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                finish();
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    // Uri.fromFile 함수를 사용하면 7.0부터는 FileUriExposedException 이 발생하므로 아래 방식으로 처리
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "user_img_" + timeStamp;
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_img"); //temp_img 경로에 이미지를 저장하기 위함

        Log.d("imageFileName", "imageFileName: "+ imageFileName);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = new File(storageDir, imageFileName+".jpg");
        image.createNewFile();
//        File image = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
        user_image = imageFileName+".jpg";
        Log.d("image", "image: "+ image);
        return image;
    }
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
//            Toast.makeText(AddDreamActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if(data==null){
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            MediaScannerConnection.scanFile(AddTodayActivity.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            try { //bitmap 형태의 이미지로 가져오기 위해 아래와 같이 Thumbnail을 추출

                Log.d("device", "onActivityResult: "+deviceWidth);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, deviceWidth, deviceWidth); //1:1 비율로 추출(가로 = 디바이스 가로크기)
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 30, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                imageBytes = bs.toByteArray();

                //ImageView에 setImageBitmap을 활용하여 해당 이미지를 띄움.
                photoTxt.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);

                uploadBtn.setImageBitmap(thumbImage);

                uploadFileUri = tempFile.toString();

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
    }
    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("outputX", deviceWidth);
            intent.putExtra("outputY", deviceWidth);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_img/");
            tempFile = new File(folder.getPath(), croppedFileName.getName());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
                photoUri = FileProvider.getUriForFile(AddTodayActivity.this, getPackageName(), tempFile);
            } else {//sdk 23 이하, 7.0 미만
                photoUri = Uri.fromFile(tempFile);
            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));


            startActivityForResult(i, CROP_FROM_CAMERA);
        }

    }
    //해당 디렉토리 전부 비우기
    public void setDirEmpty(String dirName){
        String path = Environment.getExternalStorageDirectory().toString() + dirName;

        File dir = new File(path);
        File[] childFileList = dir.listFiles();

        if (dir.exists()) {
            if(childFileList != null){
                for (File childFile : childFileList) {
                    if (childFile.isDirectory()) {
                        setDirEmpty(childFile.getAbsolutePath());
                        //하위 디렉토리
                    } else {
                        childFile.delete(); //하위 파일
                    }
                } //dir.delete();
            }
        }
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
    

    @Override
    protected void onDestroy() {
        setDirEmpty("/temp_img");
        super.onDestroy();
    }
}
