package com.chops.android_chatting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;


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

    //et의 키보드를 내리자.
    View.OnFocusChangeListener mliKeyboardDown;
    //키보드 컨트롤
    InputMethodManager mIMM;

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

        //키보드 내리자.
        mIMM = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mliKeyboardDown = new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean bFocus) {
                if(!bFocus && !(getCurrentFocus() instanceof EditText))
                {
                    mIMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                else
                {
                    mIMM.showSoftInput(view,0);
                }
            }
        };
        metAddFriend.setOnFocusChangeListener(mliKeyboardDown);
    }

    private void mfnAddFriend()
    {
        if (mstrEmail.equals(metAddFriend.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"자신의 아이디는 추가할 수 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        //파이어베이스 데이터베이스에 넣기.
        //DatabaseReference dr = mDatabase.getReference("profile/" + mstrEmail.replace(".","!,!") + "/friends");
        final DatabaseReference dr;

        dr = mDatabase.getReference("profile");//.child(metAddFriend.getText().toString().replace(".","!,!"));

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String strFriendModifiedEmail = metAddFriend.getText().toString().replace(".","!,!");
                String strMyModifiedEmail = mstrEmail.replace(".","!,!");

                //예외처리
                for (DataSnapshot ds : dataSnapshot.child(strMyModifiedEmail).child("friends").getChildren())
                {
                    if( strFriendModifiedEmail.equals(ds.getKey()))
                    {
                        Toast.makeText(getApplicationContext(),"이미 등록되어있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if( strFriendModifiedEmail.equals(ds.getKey()))
                    {
                        dr.child(strMyModifiedEmail)
                                .child("friends")
                                .child(strFriendModifiedEmail)
                                .setValue(strMyModifiedEmail+ "|" +strFriendModifiedEmail);

                        dr.child(strFriendModifiedEmail)
                                .child("friends")
                                .child(strMyModifiedEmail)
                                .setValue(strMyModifiedEmail+ "|" +strFriendModifiedEmail);

                        finish();
                        return;
                    }
                }

                Toast.makeText(getApplicationContext(), "친구 아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*
        Hashtable<String, String> htChat = new Hashtable<String, String>();
        htChat.put(metAddFriend.getText().toString().replace(".","!,!"), "id");
         */

    }
}
