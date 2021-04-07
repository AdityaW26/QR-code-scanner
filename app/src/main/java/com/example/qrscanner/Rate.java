package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.valueOf;


public class Rate extends AppCompatActivity {

    private static Button button_sbm;
    private static TextView textView;
    private static RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        listenerforRatingBar();
        onButtonClickListener();
    }

    public void listenerforRatingBar(){
        ratingBar = (RatingBar)findViewById(R.id.ratingbar);
        textView = (TextView)findViewById(R.id.text);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textView.setText(valueOf(rating));
            }
        });
    }

    public void onButtonClickListener(){
        button_sbm=(Button)findViewById(R.id.submitBtn);
        ratingBar = (RatingBar)findViewById(R.id.ratingbar);

        button_sbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Rate.this,"Submited",Toast.LENGTH_SHORT).show();
                button_sbm.setEnabled(false);
            }
        });
    }
}