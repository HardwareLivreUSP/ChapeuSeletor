package com.sc.marcelo.aprendendobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainBluetoothActivity extends AppCompatActivity {

    //marca página 5. Transferência de dados bidirecional
    //Blynk project http://www.blynk.cc/

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    ConnectionThread connect;
    static TextView statusMessage;
    static TextView mensagens;

    private int qGrifinoria;
    private int qCorvinal;
    private int qSonserina;
    private int qLufalufa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bluetooth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        statusMessage = (TextView) findViewById(R.id.statusMessage);
        mensagens = (TextView) findViewById(R.id.mensagens_recebidas);

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

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null) {
            statusMessage.setText("Merda, demonho! O hardware bluetooth não está funcionando.");
        } else {
            statusMessage.setText("Ótimo. O hardware bluetooth está funcionando.");
            if(!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
                statusMessage.setText("Solicitando ativação do bluetooth");
            } else {
                statusMessage.setText("Bluetooth já ativado :)  ");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado :D ");
            } else {
                statusMessage.setText("Bluetooth não ativado :( ");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + " " +
                        data.getStringExtra("btDevAddress"));
                connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                connect.start();
            } else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
    }

    public void buscaDispositivosPareados(View view) {
        Intent buscaDispositivosPareados = new Intent(this, DispositivosPareados.class);
        startActivityForResult(buscaDispositivosPareados, SELECT_PAIRED_DEVICE);
    }

    public void descobrirDispositivos(View view) {
        Intent descobreDispositivosIntent = new Intent(this, DispositivosDescobertos.class);
        startActivityForResult(descobreDispositivosIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void habilitarVisibilidade(View view) {
        Intent descobrivelIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        descobrivelIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(descobrivelIntent);
    }

    public void encerrarConexao(View view) {
        if (connect != null) {
            connect.cancel();
            statusMessage.setText("Conexão encerrada");
        } else {
            statusMessage.setText("Não existe conexão ativa. Nada foi feito.");
        }
    }

    public void modoServidor(View view) {
        connect = new ConnectionThread();
        connect.start();
        statusMessage.setText("Aguardando conexão ...");
    }

    /*
    public void enviarMensagem(View view) {
        EditText mensagem = (EditText) findViewById(R.id.editText_mensagem);
        String stringMensagem = mensagem.getText().toString();
        byte[] bytesMensagem = stringMensagem.getBytes();
        if (connect != null) {
            connect.write(bytesMensagem);
        } else {
            statusMessage.setText("Nenhuma conexão estabelecida. Mensagem não pode ser enviada.");
        }
    }
    */

    public void grifinoria(View view) {
        if (connect != null) {
            connect.write("g".getBytes());
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public void corvinal(View view) {
        if (connect != null) {
            connect.write("c".getBytes());
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public void sonserina(View view) {
        if (connect != null) {
            connect.write("s".getBytes());
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public void lufalufa(View view) {
        if (connect != null) {
            connect.write("l".getBytes());
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public void aleatoria(View view) {
        if (connect != null) {
            Random random = new Random();
            switch (random.nextInt(4)) {
                case 0:
                    connect.write("g".getBytes());
                    break;
                case 1:
                    connect.write("c".getBytes());
                    break;
                case 2:
                    connect.write("s".getBytes());
                    break;
                case 3:
                    connect.write("l".getBytes());
                    break;
            }
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public void conjuntoAleatorio(View view) {
        if (connect != null) {
            EditText editConjunto = (EditText) findViewById(R.id.editText_conjunto);
            String stringConjunto = editConjunto.getText().toString();
            if (stringConjunto.length() > 0) {
                int bruxos = Integer.parseInt(editConjunto.getText().toString());
                //TODO terminar o método
                int bruxosCasa = bruxos / 4;
                qGrifinoria = bruxosCasa;
                qCorvinal = bruxosCasa;
                qSonserina = bruxosCasa;
                qLufalufa = bruxosCasa;
                if (bruxosCasa * 4 < bruxos) {
                    //Não fechou número igual de integrantes / bruxos em cada casa.
                    //Vamos designar / sortear os (entre 1 e 3) bruxos restantes de modo a que cada
                    //um ocupe uma casa diferente.
                    sorteiaResto(bruxos - bruxosCasa * 4);
                }
                //connect.write(editConjunto.getText().toString().getBytes());
            }
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    /**
     * Sorteia o número de bruxos restantes de modo que cada um ocupe uma casa diferente.
     * Os bruxos restantes são colocados em suas casas incrementando a variáveis de objeto
     * qGrifinória, qCorvinal, qSonserina, e qLufalufa.
     *
     * @param resto - o número de bruxos restantes (resto) deve ser 1, 2, ou 3.
     */
    private void sorteiaResto(int resto) {
        Random random = new Random();
        if (resto == 1) {
            switch (random.nextInt(4)) {
                case 0:
                    qGrifinoria++;
                    break;
                case 1:
                    qCorvinal++;
                    break;
                case 2:
                    qSonserina++;
                    break;
                case 3:
                    qLufalufa++;
                    break;
            }
        } else if (resto == 3) {
            switch (random.nextInt(4)) {
                case 0:
                    qCorvinal++;
                    qSonserina++;
                    qLufalufa++;
                    break;
                case 1:
                    qGrifinoria++;
                    qSonserina++;
                    qLufalufa++;
                    break;
                case 2:
                    qGrifinoria++;
                    qCorvinal++;
                    qLufalufa++;
                    break;
                case 3:
                    qGrifinoria++;
                    qCorvinal++;
                    qSonserina++;
                    break;
            }
        } else { //resto == 2
            int a = random.nextInt(4);
            int b = random.nextInt(4);
            while (a == b) {
                a = random.nextInt(4);
                b = random.nextInt(4);
            }
            switch (a) {
                case 0:
                    qGrifinoria++;
                    break;
                case 1:
                    qCorvinal++;
                    break;
                case 2:
                    qSonserina++;
                    break;
                case 3:
                    qLufalufa++;
                    break;
            }
            switch (b) {
                case 0:
                    qGrifinoria++;
                    break;
                case 1:
                    qCorvinal++;
                    break;
                case 2:
                    qSonserina++;
                    break;
                case 3:
                    qLufalufa++;
                    break;
            }
        }
    }

    public void sortearProximo(View view) {
        if (connect != null) {
            Random random = new Random();
            switch (random.nextInt(4)) {
                case 0:
                    if (qGrifinoria == 0) break;
                    connect.write("g".getBytes());
                    qGrifinoria--;
                    return;
                case 1:
                    if (qCorvinal == 0) break;
                    connect.write("c".getBytes());
                    qCorvinal--;
                    return;
                case 2:
                    if (qSonserina == 0) break;
                    connect.write("s".getBytes());
                    qSonserina--;
                    return;
                case 3:
                    if (qLufalufa == 0) break;
                    connect.write("l".getBytes());
                    qLufalufa--;
                    return;
            }
            if (qGrifinoria == 0 && qCorvinal == 0 && qSonserina == 0 && qLufalufa == 0) return;
            sortearProximo(view);
        } else {
            statusMessage.setText("Não foi estabelecida conexão com o chapéu.\nMensagem não pode ser enviada.");
        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString = new String(data);

            if (dataString.equals("---N")) {
                statusMessage.setText("Ocorreu um erro durante a conexão =/");
            } else if (dataString.equals("---S")) {
                statusMessage.setText("Conectado");
            } else {
                mensagens.setText(mensagens.getText() + dataString);
            }
        }
    };

}
