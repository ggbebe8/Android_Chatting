package com.chops.android_chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    EditText metEmail;
    EditText metPassword;

    ProgressBar pbLogin;

    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;
    //파이어베이스 인증 관련
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfnInitVariable();
        mfnListener();
    }

    //변수 및 객체 초기화
    private void mfnInitVariable()
    {
        //파이어베이스 데이터베이스
        mDatabase = FirebaseDatabase.getInstance();
        //파이어베이스 인증 초기화
        mAuth = FirebaseAuth.getInstance();
        //textview 이메일
        metEmail = (EditText)findViewById(R.id.etEmail);
        //textview 패스워드
        metPassword = (EditText)findViewById(R.id.etPassword);
        //로딩 중.. 표시
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
    }

    //리스너
    private void mfnListener()
    {
        //등록 버튼
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfnRegisterUser(metEmail.getText().toString(), metPassword.getText().toString());
            }
        });

        //로그인 버튼
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mfnUserLogin(metEmail.getText().toString(), metPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }



    private void updateUI(FirebaseUser p_currentUser)
    {
        return;
    }


    //유저등록
    private void mfnRegisterUser(final String p_strEmail, String p_strPassword)
    {
        //예외처리
        if(metEmail.getText().toString().equals("") || metEmail.getText() == null) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }
        else if (metPassword.getText().toString().equals("") || metPassword.getText() == null) {
            Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        //등록
        mAuth.createUserWithEmailAndPassword(p_strEmail, p_strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText( MainActivity.this, "등록 완료",
                                    Toast.LENGTH_SHORT).show();
                            //파이어베이스 데이터베이스에 넣기.
                            DatabaseReference dr = mDatabase.getReference("profile").child(p_strEmail.replace(".","!,!"));
                            String val = "id";
                            dr.setValue(val);
                            /*
                            Hashtable<String, String> htChat = new Hashtable<String, String>();
                            htChat.put(p_strEmail.replace(".","!,!"), "id");
                            dr.setValue(htChat);

                             */

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText( MainActivity.this, "등록 실패",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    //로그인
    private void mfnUserLogin(String p_strEmail, String p_strPassword)
    {
        //예외처리
        if(metEmail.getText().toString().equals("") || metEmail.getText() == null) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }
        else if (metPassword.getText().toString().equals("") || metPassword.getText() == null) {
            Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }
        pbLogin.setVisibility(View.VISIBLE);

        //로그인 인증
        mAuth.signInWithEmailAndPassword(p_strEmail, p_strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pbLogin.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            //Toast.makeText( MainActivity.this, "로그인 성공",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            //성공 시, 뷰 전환.
                            Intent in = new Intent(MainActivity.this, tabChatMain.class);
                            in.putExtra("Email", metEmail.getText().toString());
                            startActivity(in);
                            finish();

                        } else {
                            pbLogin.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}
