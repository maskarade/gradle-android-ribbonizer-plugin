package com.github.gfx.ribbonizer.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.button)
    void onButtonClick() {
        new AlertDialog.Builder(this)
                .setTitle("Hello, Android!")
                .setMessage("This is an example app.")
                .setPositiveButton("OK", null)
                .show();
    }
}
