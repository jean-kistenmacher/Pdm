package com.unisc.pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class Aula5Ex2ctivity extends AppCompatActivity {

    private TextView txtNome;
    private Spinner spinnerUF;
    private ListView listViewCidade;

    String[] uf = {"Rio Grande do Sul","Santa Catarina","Paraná","São Paulo","Minas Gerais"};
    String [][] cidades = {
            new String []{"Porto Alegre","Santa Maria","Caxias do Sul","Santa Cruz do Sul","Pelotas","Rio Grande"},
            new String []{"Florianópolis","Joinville","Criciúma","Chapecó","Blumenau"},
            new String []{"Curitiba","Londrina","Maringá","Foz do Iguaçu"},
            new String []{"São Paulo","Campinas","Osasco"},
            new String []{"Belo Horizonte", "Uberaba"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aula5_ex2ctivity);

        txtNome = findViewById(R.id.txtNome);
        spinnerUF = findViewById(R.id.spinnerUF);
        listViewCidade = findViewById(R.id.listViewCidade);

        Intent intent = getIntent();
        txtNome.setText(intent.getStringExtra("nome"));

        ArrayAdapter<CharSequence> adapterSpinner = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, uf);
        spinnerUF.setAdapter(adapterSpinner);
        spinnerUF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String [] selecao = getRow(i);
                ArrayAdapter<String> adapterListView =
                        new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, Arrays.asList(selecao));
                listViewCidade.setAdapter(adapterListView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {  }
        });

    }

    public String[] getRow(int index)  {
        String[] row = new String[cidades[index].length];
        for (int i = 0; i < row.length; i++)
            row[i] = cidades[index][i];
        return row;
    }

}
