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
                AddFieldFragment AddFragment = AddFieldFragment.newInstance();

                FragmentTransaction tran = getFragmentManager().beginTransaction();
                tran.add(R.id.activity_field, AddFragment);
                tran.commit();
                break;
            case MapsActivity.DETAILS_FRAGMENT:
                String id = getIntent().getExtras().getString("id");

                FieldDetailsFragment detailsFragment = FieldDetailsFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("id", id);
                detailsFragment.setArguments(args);

                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.add(R.id.activity_field, detailsFragment);
                trans.commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int intentCode = getIntent().getExtras().getInt("frgToLoad");

        switch (intentCode) {
            case MapsActivity.ADD_FRAGMENT:
                getMenuInflater().inflate(R.menu.activity_main_menu,menu);
                break;
            case MapsActivity.DETAILS_FRAGMENT:
                getMenuInflater().inflate(R.menu.activity_field_details_menu,menu);
                break;
        }
        //getMenuInflater().inflate(R.menu.activity_main_menu,menu);
        return true;
    }


}
