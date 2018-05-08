package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        //Set on click to buttons
        findViewById(R.id.buttonSendMessage).setOnClickListener(this);
        findViewById(R.id.buttonFinduser).setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonSendMessage:

                Intent tomessage = new Intent(this, SelectMessageUserActivity.class);
                startActivity(tomessage);

                break;

            case R.id.buttonFinduser:

                Intent tomap = new Intent(this, MapActivity.class);
                startActivity(tomap);

                break;

        }
    }
}
