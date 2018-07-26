package com.example.kushtimusprime.maptrialsix;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TrialFragment extends AppCompatActivity {
    private BottomNavigationView mapMenu;
    private FrameLayout mainContainer;
    private HopefulMapFragment hopefulMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_fragment);
        mapMenu=findViewById(R.id.mapMenu);
        mainContainer=findViewById(R.id.mainContainer);
        hopefulMapFragment=new HopefulMapFragment();
        mapMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.mapItem:
                        replaceFragment(hopefulMapFragment);
                    default:
                        return false;
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,fragment);
        fragmentTransaction.commit();
    }

}
