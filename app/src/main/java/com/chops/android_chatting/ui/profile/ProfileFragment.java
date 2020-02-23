package com.chops.android_chatting.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chops.android_chatting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    View v;
    //프로필사진
    ImageView imgProfile;
    //파이어베이스 스토리지
    StorageReference mStorageRef;
    //로그인 이메일
    String mstrEmail = "";
    //프로필사진_bitmap
    Bitmap mbitmap;

    //생성자
    public ProfileFragment(String p_strEmail)
    {
        mstrEmail = p_strEmail;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mfnInitVariable();

        //서버에서 미리 정해진 이미지 받기
        try {
            mfnDownImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mfnListener();


        return v;
    }

    //사진 받아오기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        //권한요청
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        } else {
        }
*/
        Uri Image;
        try {
            Image = data.getData();
            mbitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Image);
            imgProfile.setImageBitmap(mbitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byData = baos.toByteArray();

            //사진을 정상적으로 찾았다면 서버로 전송
            UploadTask uploadTask = mStorageRef.child("chat_image/" + mstrEmail +"/profile.jpg").putBytes(byData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });

        } catch (Exception e) {}
    }

    //객체 및 변수 초기화
    private void mfnInitVariable()
    {
        //파이어베이스 스토리지 초기화
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //프로필 이미지 초기화
        imgProfile = (ImageView)v.findViewById(R.id.imProfile);
    }

    //리스너
    private void mfnListener()
    {
        //프로필 사진 클릭
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(it, 1);
            }
        });
    }

    //프로필 이미지를 서버에서 가져옴
    private void mfnDownImage() throws IOException {
        final File localFile = File.createTempFile("images", "jpg");
        mStorageRef.child("chat_image/" + mstrEmail +"/profile.jpg").getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        mbitmap = BitmapFactory.decodeFile(localFile.getPath());
                        imgProfile.setImageBitmap(mbitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }


/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
*/
}