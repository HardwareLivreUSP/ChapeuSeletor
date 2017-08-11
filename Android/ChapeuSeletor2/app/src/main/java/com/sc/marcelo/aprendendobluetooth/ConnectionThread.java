package com.sc.marcelo.aprendendobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by marcelo on 25/07/17.
 */

public class ConnectionThread extends Thread {

    BluetoothSocket btSocket = null;
    BluetoothServerSocket btServerSocket = null;
    String btDevAddress = null;
    String myUUID = "00001101-0000-1000-8000-00805F9B34FB";
    InputStream input = null;
    OutputStream output = null;
    boolean server;
    boolean running = false;


    /**
     * Este construtor prepara o dispositivo para atuar como servidor.
     */
    public ConnectionThread() {
        this.server = true;
    }

    /**
     *  Este construtor prepara o dispositivo para atuar como cliente.
     *  Tem como argumento um string contendo o endereço MAC do dispositivo Bluetooh para o qual
     *  deve solicitar conexão.
     */
    public  ConnectionThread(String btDevAddress) {
        this.server = false;
        this.btDevAddress = btDevAddress;
    }

    @Override
    public void run() {


        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(this.server) {
            //Servidor
            try {
                //Cria um socket de servidor.
                // O socket servidor será usado apenas para iniciar a conexão.
                //Permanece em estado de espera até que algum cliente estabeleça uma conexão.

                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("AprendendoBluetooh", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();

                //Se a conexão foi estabelecida corretamente o socket do servidor pode ser liberado.
                if (btSocket != null) {
                    btServerSocket.close();
                }

            } catch (IOException ioe) {
                //Caso ocorra alguma exceção, exibe a stack trace para debug e envia um código de
                //erro à Activity principal informando que a conexão falhou.
                ioe.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        } else {
            //Cliente
            try {
                //Obtem uma representação do dispositivo Bluetooth com endereço btAddress.
                //Cria um socket bluetooth.
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));

                //Envia ao sistema um comando para cancelar qualquer processo de descoberta de
                //em execução.
                btAdapter.cancelDiscovery();

                //Solicita uma conexão ao dispositivo cujo endereço é btDevAddress.
                //Permanece em estado de espera até que a conexão seja estabelecida.
                if (btSocket != null) {
                    btSocket.connect();
                }
            } catch (IOException ioe) {
                //Caso ocorra alguma exceção, exibe a stack trace para debug e envia um código de
                //erro à Activity principal informando que a conexão falhou.
                ioe.printStackTrace();
                toMainActivity("---N".getBytes());
            }

        }
        //Pronto. Dispositivos conectados. Agora falta gerenciar a conexão.

        if (btSocket != null) {
            //Envia uma mensagem à Activity principal (na thread principal) informando que a
            //conexão foi bem sucedida.

            toMainActivity("---S".getBytes());

            try {
                //Obtendo as referẽncias para os fluxos de entrada e saída do socket bluetooth

                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();

                //Cria um array de bytes temporário para armazenar os dados recebidos.
                byte[] buffer = new byte[1024];
                int bytes; //número de bytes recebidos na última mensagem.

                //Permanece em "espera" (lendo o socket) até que uma mensagem seja recebida.
                //Armazena a mensagem no buffer.
                //Envia para a Activity principal o que leu do socket bluetooth.
                while (running) {
                    bytes = input.read(buffer);
                    toMainActivity(Arrays.copyOfRange(buffer, 0, bytes));
                }


            } catch (IOException ioe) {
                ioe.printStackTrace();
                toMainActivity("---N".getBytes());
            }

        }

    }

    /**
     * Utiliza um handler para enviar um byte array à Activity principal.
     * O byte array é encapsulado em um Bundle e posteriormente em um Message antes de ser enviado.
     * @param data
     */
    public void toMainActivity(byte[] data) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        message.setData(bundle);
        MainBluetoothActivity.handler.sendMessage(message);
    }

    /**
     * Método utilizado pela Activity principal para transmitir uma mensagem ao outro lado da
     * conexão. A mensagem (data) deve ser um array de byte.
     * @param data
     */
    public void write(byte[] data) {
        if (output != null) {
            try {
                //Transmite os dados
                output.write(data);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        } else {
            toMainActivity("---N".getBytes());
        }
    }

    /**
     * Método usado pela activity principal para encerrar a conexão.
     */
    public void cancel() {
        try {
            running = false;
            if (btSocket != null) {
                btSocket.close();
            }
            if (btServerSocket != null) {
                btServerSocket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        running = false;
    }

}
