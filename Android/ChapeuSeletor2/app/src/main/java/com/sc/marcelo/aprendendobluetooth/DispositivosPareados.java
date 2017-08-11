package com.sc.marcelo.aprendendobluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class DispositivosPareados extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dispositivos_pareados);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Adiciona um título à lista de dispositivos pareados utilizando o layout text_header.xml
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.text_header, lv, false);
        ((TextView) header.findViewById(R.id.textView)).setText("\nDispositivos Pareados");
        lv.addHeaderView(header, null, false);

        //Usa o adaptador bluetooth para obter uma lista de dispositivos pareados.
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> dispositivosPareados = btAdapter.getBondedDevices();

        //Cria um modelo para a lista e o adiciona à tela.
        //Se houver dispositivos pareados, adiciona cada um à lista.

        //https://stackoverflow.com/questions/3663745/what-is-android-r-layout-simple-list-item-1
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        setListAdapter(adapter);
        if(dispositivosPareados.size() > 0) {
            for(BluetoothDevice device : dispositivosPareados) {
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Botão ou não botão, eis a questão.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //Extrai nome e endereço a partir do conteúdo do elemento selecionado.
        //Nota: position-1 é utilizado pois adicionamos um título à lista e o
        //valor de position recebido pelo método é deslocado em uma unidade.
        String item = (String) getListAdapter().getItem(position-1);
        String devName = item.substring(0, item.indexOf("\n"));
        String devAddress = item.substring(item.indexOf("\n")+1, item.length());

        //Utiliza um Intent para encapsular as informações de nome e endereço.
        //Informa à Activity principal que tudo foi um sucesso!
        //Finaliza e retorna à Activity principal.
        Intent returnIntent = new Intent();
        returnIntent.putExtra("btDevName", devName);
        returnIntent.putExtra("btDevAddress", devAddress);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //TODO falta R.menu.menu_paired_devices aqui
        //getMenuInflater().inflate(, menu);

        return super.onCreateOptionsMenu(menu);
        //return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Sem inspeção
        //TODO falta o action_settings aqui
        //if (id == R.id.) {
        //    return true;
        //}
        return  super.onOptionsItemSelected(item);
    }
}