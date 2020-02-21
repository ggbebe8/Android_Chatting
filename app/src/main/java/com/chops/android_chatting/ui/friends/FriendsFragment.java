package com.chops.android_chatting.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chops.android_chatting.R;
import com.chops.android_chatting.RecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FriendsFragment extends Fragment {

    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;
    //로그인 이메일
    String mstrEmail = "";
    //뷰
    View v;

    public FriendsFragment(String p_strEmail)
    {
        mstrEmail = p_strEmail;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_friends, container, false);

        return v;
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
    }
}