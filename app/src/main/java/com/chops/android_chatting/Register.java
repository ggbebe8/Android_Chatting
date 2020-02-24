package com.chops.android_chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Register extends Activity {

    EditText metEmail;
    EditText metPassword1;
    EditText metPassword2;
    Button mbtnRegi;
    Button mbtnClose;
    //파이어베이스 인증
    FirebaseAuth mAuth;
    //파이어베이스 데이터베이스
    FirebaseDatabase mDatabase;
    //et의 키보드를 내리자.
    View.OnFocusChangeListener mliKeyboardDown;
    //키보드 컨트롤
    InputMethodManager mIMM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_register);

        mfnInitVariable();
        mfnListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void mfnInitVariable()
    {
        metEmail = (EditText)findViewById(R.id.etRegisterEmail);
        metPassword1 = (EditText)findViewById(R.id.etRegisterPassword1);
        metPassword2 = (EditText)findViewById(R.id.etRegisterPassword2);
        mbtnRegi = (Button) findViewById(R.id.btnRegisterAdd);
        mbtnClose = (Button) findViewById(R.id.btnRegisterClose);
        mAuth = FirebaseAuth.getInstance();
        //파이어베이스 데이터베이스
        mDatabase = FirebaseDatabase.getInstance();
    }

    private void mfnListener()
    {
        mbtnRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfnRegisterUser(metEmail.getText().toString(), metPassword1.getText().toString(), metPassword2.getText().toString());
            }
        });

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
        metEmail.setOnFocusChangeListener(mliKeyboardDown);
        metPassword1.setOnFocusChangeListener(mliKeyboardDown);
        metPassword2.setOnFocusChangeListener(mliKeyboardDown);
    }


    //유저등록
    private void mfnRegisterUser(final String p_strEmail, String p_strPassword, String p_strPassword2)
    {
        //예외처리
        if(metEmail.getText().toString().equals("") || metEmail.getText() == null) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (metPassword1.getText().toString().equals("") || metPassword1.getText() == null) {
            Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!metPassword1.getText().toString().equals(metPassword2.getText().toString())) {
            Toast.makeText(this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        //등록
        mAuth.createUserWithEmailAndPassword(p_strEmail, p_strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( Register.this, "등록 완료",
                                    Toast.LENGTH_SHORT).show();
                            //파이어베이스 데이터베이스에 넣기.
                            DatabaseReference dr = mDatabase.getReference("profile").child(p_strEmail.replace(".","!,!")).child("add_date");
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dr.setValue(sdf.format(c.getTime()));

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            finish();
                        } else {
                            Toast.makeText( Register.this, "등록 실패",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser p_currentUser)
    {
        return;
    }
}
