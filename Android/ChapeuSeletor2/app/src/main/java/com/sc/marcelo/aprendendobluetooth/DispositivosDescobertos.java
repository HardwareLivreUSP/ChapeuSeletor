package com.sc.marcelo.aprendendobluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DispositivosDescobertos extends ListActivity {

    ArrayAdapter<String> arrayAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        //Este método é executado sempre que um novo dispositivo for descoberto
        @Override
        public void onReceive(Context context, Intent intent) {
            //Obtem o Intent que gerou a ação.
            //Verifica se a ação correspondente à descoberta de um novo dispositivo.
            //Obtem um objeto que representa o dispositivo Bluetooth descoberto.
            //Exibe seu nome e endereço na lista.
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dispositivos_descobertos);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Adiciona um título à lista de dispositivos pareados utilizando o layout text_header.xml.
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.text_header, lv, false);
        ((TextView) header.findViewById(R.id.textView)).setText("\nDispositivos próximos\n");
        lv.addHeaderView(header, null, false);

        //Cria um modelo para a lista e o adiciona à tela.
        //Para adicionar um elemento à lista, usa-se arrayAdapter.add().
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        setListAdapter(arrayAdapter);

        //Usa o adaptador Bluetooth padrão para iniciar o processo de descoberta.
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();

        //Cria um filtro que captura o memento em que um dispositivo é descoberto.
        //Registra o filtro e define um receptor para o evento de descoberta.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);


        //dfkjgkfhgdkjf
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Extrai nome e endereço a partir do contúdo do elemento da lista.
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
    protected void onDestroy() {
        super.onDestroy();
        //remove o filtro de descoberta de dispositivos do registro
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
        //TODO R.menu.menu_paired_devices
        //getMenuInflater().inflate(R.menu.menu_paired_devices, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO action_settings
        //int id = item.getItemId();

        //if (id == R.id.ac)

        return super.onOptionsItemSelected(item);
    }
}
