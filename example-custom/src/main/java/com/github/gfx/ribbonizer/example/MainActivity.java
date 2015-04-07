package com.github.gfx.ribbonizer.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @InjectView(R.id.text)
    TextView textView;

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
