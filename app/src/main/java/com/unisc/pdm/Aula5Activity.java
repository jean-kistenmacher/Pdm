package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Aula5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula5);
    }

    public void aulaSlidesClick(View view)  {
        startActivity(new Intent(this,Aula5SlidesActivity.class));
    }
}
