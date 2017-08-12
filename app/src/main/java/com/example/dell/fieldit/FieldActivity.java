package com.example.dell.fieldit;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class FieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        int intentCode = getIntent().getExtras().getInt("frgToLoad");

        switch (intentCode) {
            case MapsActivity.ADD_FRAGMENT:
                AddFieldFragment addFragment = AddFieldFragment.newInstance();
                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.add(R.id.activity_field, addFragment);
                tran.commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }


}
