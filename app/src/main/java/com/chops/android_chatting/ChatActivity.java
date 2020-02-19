package com.chops.android_chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;


public class ChatActivity extends AppCompatActivity {



    //로그인 이메일아이디
    private String mstrEmail;

    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;

    //리사이클러뷰 어댑터
    private RecyclerAdapter mrcAdapter;

    //리사이클러뷰 메인 채팅 뷰
    RecyclerView mRecyclerView;

    //입력
    EditText metInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mfnInitVariable();
        mfnListener();
    }


    //객체 및 변수 초기화
    private void mfnInitVariable()
    {
        //파이어베이스 인스턴스 초기화.
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mstrEmail = user.getEmail();
        }

        //리사이클러뷰 초기화
        mRecyclerView = findViewById(R.id.recyclerView);
        mrcAdapter = new RecyclerAdapter(mstrEmail);
        mRecyclerView.setAdapter(mrcAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        metInput = (EditText)findViewById(R.id.etInput);
    }

    private void mfnListener()
    {
        //Finish 버튼 클릭
        Button btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //보내기 버튼
        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strText = metInput.getText().toString();

                if(strText.equals(""))
                {
                    Toast.makeText(ChatActivity.this, "내용을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strCurrentDate = sdf.format(c.getTime());

                    //파이어베이스 데이터베이스에 넣기.
                    DatabaseReference dr = mDatabase.getReference("chat").child(strCurrentDate);
                    Hashtable<String, String> htChat = new Hashtable<String, String>();
                    htChat.put("email", mstrEmail);
                    htChat.put("text", strText);
                    dr.setValue(htChat);

                    metInput.setText("");
                }
            }
        });


        //데이터베이스 리스너
        DatabaseReference dr = mDatabase.getReference("chat");
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*
                // A new comment has been added, add it to the displayed list
                Data data = dataSnapshot.getValue(Data.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mChat.add(data);
                adapter.notifyItemInserted(mChat.size() - 1);

                 */
                Data chat = dataSnapshot.getValue(Data.class);
                mrcAdapter.addItem(chat);
                mrcAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mrcAdapter.getItemCount()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



/*
    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setEmail(listTitle.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
*/

}
