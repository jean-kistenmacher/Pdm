package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAmostraTrabalho extends AppCompatActivity {

    String query = "SELECT * FROM amostra";
    List<Map<String, Object>> lista;
    private DatabaseTrabalho dbTrabalho;
    ListView listView;
    public ArrayList<String> rs;
    TextView tvR, tvG, tvB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amostra);
        rs = new ArrayList<>();
        loadLista();

    }

    protected  void onDestroy() {
        dbTrabalho.close();
        super.onDestroy();
    }

    public void loadLista(){
        listView = findViewById(R.id.lista);

        lista = new ArrayList<>();
        dbTrabalho = new DatabaseTrabalho(this);
        SQLiteDatabase db = dbTrabalho.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Map<String,Object> map = new HashMap<>();
            String id = c.getString(0);
            String R = "R: " + c.getString(1);
            String G = "G: " + c.getString(2);
            String B = "B: " + c.getString(3);
            String nome = c.getString(4);

            map.put("id",id);
            map.put("R",R);
            map.put("G",G);
            map.put("B",B);
            map.put("nome",nome);
            lista.add(map);
            c.moveToNext();
        }
        c.close();
        SimpleAdapter adapter = new AmostraAdapter(this,lista,R.layout.lista_amostra,new String[] {"nome","R","G","B"},
                new int[] {R.id.tvNome, R.id.tvR, R.id.tvG, R.id.tvB});
        listView.setAdapter(adapter);
    }

    private class AmostraAdapter extends SimpleAdapter {

        public AmostraAdapter(Context ctx, List<Map<String, Object>> lista, int listagem2, String[] strings, int[] ints) {
            super(ctx,lista,listagem2,strings,ints);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            SQLiteDatabase db = dbTrabalho.getReadableDatabase();
            Cursor c = db.rawQuery(query,null);
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                if(i == position){
                    int r = c.getInt(1);
                    int g = c.getInt(2);
                    int b = c.getInt(3);
                    view.setBackgroundColor(Color.rgb(r,g, b));
                    break;
                }
            c.moveToNext();
            }
            c.close();
            return view;
        }
    }
}
