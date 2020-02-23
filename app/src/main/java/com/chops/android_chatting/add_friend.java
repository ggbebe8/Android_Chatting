package com.chops.android_chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class add_friend extends Activity {

    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;

    //추가 버튼
    Button mbtnAddFriend;

    //닫기 버튼
    Button mbtnClose;

    //로그인 이메일
    String mstrEmail = "";

    EditText metAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        Intent it = getIntent();
        mstrEmail = it.getExtras().getString("Email");

        mfnInit();
        mfnListener();
    }

    private void mfnInit()
    {
        //파이어베이스 인스턴스 초기화.
        mDatabase = FirebaseDatabase.getInstance();
        mbtnAddFriend = (Button)findViewById(R.id.btnAddFriend);
        mbtnClose = (Button)findViewById(R.id.btnClose);
        metAddFriend = (EditText)findViewById(R.id.etAddFriend);
    }

    private void mfnListener()
    {
        //추가버튼
        mbtnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfnAddFriend();
            }
        });
        //닫기버튼
        mbtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mfnAddFriend()
    {
        //파이어베이스 데이터베이스에 넣기.
        //DatabaseReference dr = mDatabase.getReference("profile/" + mstrEmail.replace(".","!,!") + "/friends");
        DatabaseReference dr;

        dr = mDatabase.getReference("profile").child(mstrEmail.replace(".","!,!")).child("friends").child(metAddFriend.getText().toString().replace(".","!,!"));
        String val = "id";
        /*
        Hashtable<String, String> htChat = new Hashtable<String, String>();
        htChat.put(metAddFriend.getText().toString().replace(".","!,!"), "id");
         */
        dr.setValue(val);
    }
}
