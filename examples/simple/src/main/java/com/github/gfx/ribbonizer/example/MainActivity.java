package com.github.gfx.ribbonizer.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {
        new AlertDialog.Builder(this)
                .setTitle("Hello, Android!")
                .setMessage("This is an example app.")
                .setPositiveButton("OK", null)
                .show();
    }
}
