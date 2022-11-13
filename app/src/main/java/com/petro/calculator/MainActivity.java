package com.petro.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout calcButtons = findViewById(R.id.calcButtons);
        for (int i = 0; i < calcButtons.getChildCount(); i++) {
            TextView textView = (TextView) calcButtons.getChildAt(i);
            textView.setOnClickListener(this);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        TextView result = findViewById(R.id.result);
//        Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.buttonAC:
                result.setText(null);
                return;
            case R.id.buttonInverse:
                int r = Integer.parseInt(result.getText().toString());
                r *= -1;
                result.setText("" + r);
                return;
            case R.id.buttonEvaluate:
                result.setText(Calculator.calculate(result.getText().toString()));
                return;
        }

        result.setText(result.getText().toString() + ((TextView) view).getText());
    }
}