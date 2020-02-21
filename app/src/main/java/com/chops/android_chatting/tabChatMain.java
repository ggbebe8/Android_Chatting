package com.chops.android_chatting;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.chops.android_chatting.ui.friends.FriendsFragment;
import com.chops.android_chatting.ui.home.HomeFragment;
import com.chops.android_chatting.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class tabChatMain extends AppCompatActivity {

    long mlPressed = 0;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ProfileFragment pf;
    private HomeFragment hf;
    private FriendsFragment ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_chat_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        Intent it = getIntent();
        String strEmail = it.getExtras().getString("Email");

        pf = new ProfileFragment(strEmail);
        hf = new HomeFragment(strEmail);
        ff = new FriendsFragment(strEmail);

        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, hf).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, ff).commit();
        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, pf).commit();

        setTitle("Home");
        fragmentManager.beginTransaction().show(hf).commit();
        fragmentManager.beginTransaction().hide(pf).commit();
        fragmentManager.beginTransaction().hide(ff).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        setTitle("Home");
                        fragmentManager.beginTransaction().show(hf).commit();
                        fragmentManager.beginTransaction().hide(pf).commit();
                        fragmentManager.beginTransaction().hide(ff).commit();

                        break;
                    }
                    case R.id.navigation_friends: {
                        setTitle("Friends");
                        fragmentManager.beginTransaction().hide(hf).commit();
                        fragmentManager.beginTransaction().hide(pf).commit();
                        fragmentManager.beginTransaction().show(ff).commit();
                        break;
                    }
                    case R.id.navigation_profile: {
                        setTitle("Profile");
                        fragmentManager.beginTransaction().hide(hf).commit();
                        fragmentManager.beginTransaction().show(pf).commit();
                        fragmentManager.beginTransaction().hide(ff).commit();

                        break;
                    }
                }

                return true;
            }
        });
        /*
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_friends, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

         */
    }

    @Override
    public  void onBackPressed()
    {
        if (System.currentTimeMillis() - mlPressed < 1500)
        {
            finish();;
        }
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
        mlPressed = System.currentTimeMillis();
    }

}
