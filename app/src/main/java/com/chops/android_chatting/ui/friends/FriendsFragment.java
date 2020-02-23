package com.chops.android_chatting.ui.friends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chops.android_chatting.ChatActivity;
import com.chops.android_chatting.Data;
import com.chops.android_chatting.Data_Friends;
import com.chops.android_chatting.MainActivity;
import com.chops.android_chatting.R;
import com.chops.android_chatting.RecyclerAdapter;
import com.chops.android_chatting.RecyclerAdapter_Friends;
import com.chops.android_chatting.tabChatMain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FriendsFragment extends Fragment {

    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;
    //파이어베이스 스토리지
    StorageReference mStorageRef;
    //로그인 이메일
    String mstrEmail = "";
    //뷰
    View v;
    //리사이클러뷰 어댑터
    RecyclerAdapter_Friends mrcAdapter;
    //리사이클러뷰 메인 친구 뷰
    RecyclerView mRecyclerView;
    //친구추가
    ImageView mivAddFriend;

    public FriendsFragment(String p_strEmail)
    {
        mstrEmail = p_strEmail;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_friends, container, false);

        mfnInitVariable();
        mfnListener();

        return v;
    }

    //객체 및 변수 초기화
    private void mfnInitVariable()
    {
        //파이어베이스 인스턴스 초기화.
        mDatabase = FirebaseDatabase.getInstance();
        //파이어베이스 스토리지 초기화
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //리사이클러뷰 초기화
        mRecyclerView = v.findViewById(R.id.rcFriends);
        mrcAdapter = new RecyclerAdapter_Friends();
        mRecyclerView.setAdapter(mrcAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //친구추가
        mivAddFriend = (ImageView)v.findViewById(R.id.ivAddFriend);
    }

    private void mfnListener()
    {
        //리사이클러뷰의 대화 버튼 클릭이벤트
        mrcAdapter.setOnItemClickListener(new RecyclerAdapter_Friends.OnItemClickListener() {
            @Override
            public void onItemClick(final View v, final int pos) {

                DatabaseReference drGetFriendChatKey = mDatabase.getReference("profile").child( mstrEmail.replace(".","!,!")).child("friends").child( mrcAdapter.listData.get(pos).mstrFriendEmail.replace(".","!,!"));

                drGetFriendChatKey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String strChatKey = dataSnapshot.getValue().toString();
                        //성공 시, 뷰 전환.
                        Intent in = new Intent(v.getContext(), ChatActivity.class);
                        in.putExtra("ChatKey", strChatKey);
                        startActivity(in);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        //친구추가 클릭
        mivAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), com.chops.android_chatting.add_friend.class);
                in.putExtra("Email", mstrEmail);
                startActivity(in);
            }
        });

        //데이터베이스 리스너
        DatabaseReference drFriendList = mDatabase.getReference("profile/" + mstrEmail.replace(".","!,!") + "/friends");
        drFriendList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String strKey = dataSnapshot.getKey() == null ? "" : dataSnapshot.getKey().replace("!,!",".");
                try {
                    mfnDownImage(strKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //mrcAdapter.addItem(dfFriends);
                //mrcAdapter.notifyDataSetChanged();
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


    //프로필 이미지를 서버에서 가져옴
    private void mfnDownImage(final String p_strEmail) throws IOException {
        final File localFile = File.createTempFile(p_strEmail, "jpg");

        mStorageRef.child("chat_image/" + p_strEmail + "/profile.jpg").getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Data_Friends dfFriends = new Data_Friends();
                        dfFriends.mstrFriendEmail = p_strEmail;
                        dfFriends.mbitFriendProfile = BitmapFactory.decodeFile(localFile.getPath());
                        mrcAdapter.addItem(dfFriends);
                        mrcAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Data_Friends dfFriends = new Data_Friends();
                dfFriends.mstrFriendEmail = p_strEmail;
                dfFriends.mbitFriendProfile = null;
                mrcAdapter.addItem(dfFriends);
                mrcAdapter.notifyDataSetChanged();
            }
        });
    }
}