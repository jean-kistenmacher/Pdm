package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {


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

    public void aula4Click(View view) {
        startActivity(new Intent(this,Aula4Activity.class));
    }

    public void aula5Click(View view) { startActivity(new Intent(this,Aula5Activity.class)); }

    public void aula6Click(View view) {
        startActivity(new Intent(this,Aula6Activity.class));
    }

    public void aula7Click(View view) {
        startActivity(new Intent(this,Aula7Activity.class));
    }

    public void aula8Click(View view) {
        startActivity(new Intent(this,Aula8Activity.class));
    }

}
