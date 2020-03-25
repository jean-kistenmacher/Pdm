package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void aula1Click(View view) {
        startActivity(new Intent(this,Aula1Activity.class));
    }

    public void aula2Click(View view) { startActivity(new Intent(this,Aula2Activity.class));  }

    public void aula3Click(View view) {
        startActivity(new Intent(this,Aula3Activity.class));
    }


}
