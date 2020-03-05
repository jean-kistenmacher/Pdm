package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity  {

    private EditText editText;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        Log.d("CICLO", "onCreate");
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CICLO", "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CICLO", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("CICLO", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CICLO", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("CICLO", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CICLO", "onDestroy");
    }

    int i = 0;


    public void click1(View view) {

        String valorDigitado = editText.getText().toString();

        if (!valorDigitado.isEmpty()) {

            Intent intent = new Intent(this, OutraActivity.class);
            intent.putExtra("celcius", valorDigitado);
            startActivity(intent);
        }
    }
}
