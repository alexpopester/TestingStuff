package com.usu.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        AppCompatTextView textView = new AppCompatTextView(this);
        textView.setText("ROUGE LIKE");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(42);
        linearLayout.addView(textView);

        MaterialButton play = new MaterialButton(this);
        play.setText("Play");
        play.setOnClickListener((view) -> {
            startActivity(new Intent(this, GameActivity.class));
        });
        linearLayout.addView(play);

        setContentView(linearLayout);
    }
}