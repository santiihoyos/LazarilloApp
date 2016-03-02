package com.lazarilloapp.lazarilloapp.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lazarilloapp.lazarilloapp.R;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.bt_selectCiegos:

                break;
            case R.id.bt_selectNoCiegos:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
    }
}
