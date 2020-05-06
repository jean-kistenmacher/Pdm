package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aula8SelActivity extends AppCompatActivity {

    private ListView listView;
    private EditText etAno;
    List<Map<String, Object>> lista;
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula8_sel);

        listView = findViewById(R.id.listView);
        etAno= findViewById(R.id.etAno);
        helper = new DatabaseHelper(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvId = view.findViewById(R.id.tvId);
                TextView tvModelo= view.findViewById(R.id.tvModelo);
                TextView tvAno= view.findViewById(R.id.tvAno);
                TextView tvValor= view.findViewById(R.id.tvValor);
                Intent intent = new Intent(getApplicationContext(), Aula8UDActivity.class);
                intent.putExtra("id", tvId.getText().toString());
                intent.putExtra("modelo", tvModelo.getText().toString());
                intent.putExtra("ano", tvAno.getText().toString());
                intent.putExtra("valor", tvValor.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void searchClick(View view) {
        String query = "";
        if (etAno.getText().toString().isEmpty()) {
            query = "SELECT * FROM carro";
        }
        else {
            query = "SELECT * FROM carro WHERE ano = " +  etAno.getText().toString();
        }
        lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++){
            Map<String, Object> mapa = new HashMap<>();
            String id = c.getString(0);
            String modelo = c.getString(1);
            String ano = c.getString(2);
            String valor = c.getString(3);
            mapa.put("id", id);
            mapa.put("modelo", modelo);
            mapa.put("ano", ano);
            mapa.put("valor", valor);
            lista.add(mapa);
            c.moveToNext();
        }
        c.close();
        SimpleAdapter adapter = new SimpleAdapter(this, lista,R.layout.listagem,
                new String [] {"id","modelo","ano","valor"},
                new int[] {R.id.tvId, R.id.tvModelo,R.id.tvAno,R.id.tvValor,});
        listView.setAdapter(adapter);

    }

    public void addClick(View view) {
        startActivity(new Intent(this,Aula8AddActivity.class));
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();

    }

}
