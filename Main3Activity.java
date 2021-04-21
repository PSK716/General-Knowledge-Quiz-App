package com.example.quizzy.Results_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzy.Attempt_Quiz_Section.Tests;
import com.example.quizzy.R;
import com.google.firebase.auth.FirebaseAuth;

public class Main3Activity extends AppCompatActivity {
TextView t1;
FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        t1 = findViewById(R.id.textView15);
        auth= FirebaseAuth.getInstance();
        final String uid=auth.getUid();
        Intent intent=getIntent();
        String score=intent.getStringExtra("Scoore");
        t1.setText(score+" /10");
    }

    public void playAgain(View view) {
        Intent intent=new Intent(Main3Activity.this, Tests.class);
        startActivity(intent);
    }
}