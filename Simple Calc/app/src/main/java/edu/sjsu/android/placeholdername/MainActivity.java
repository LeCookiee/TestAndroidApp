package edu.sjsu.android.placeholdername;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;

import org.matheclipse.core.basic.AndroidLoggerFix;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidLoggerFix.fix();
        Log.d("TEST", "fixing");
    }

}