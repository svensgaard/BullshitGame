package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Created by Mads on 30-03-2016.
 */
public class ServerThread extends Thread{
    private Boolean runThread = true;
    private BluetoothAdapter adapter;
    private BluetoothSocket btsocket;
    private Context context;

    private String serverName = "bullshitServer";
    public static final UUID RFCOMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    OutputStream outputstream;
    InputStream inputstream;

    public ServerThread(Context context, BluetoothAdapter adapter) {

        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();


        BluetoothServerSocket btServer = null;

        // Create BT server
        try {
            btServer = adapter.listenUsingInsecureRfcommWithServiceRecord(serverName, RFCOMM_UUID);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }



        // Listen for connections
        try {
            Log.d("ServerInfo", "Waiting for connection");
            //Blocking
            btsocket = btServer.accept();
            Log.d("ServerInfo", "Connection accepted");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }



        try {
            outputstream = btsocket.getOutputStream();
            inputstream = btsocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        String input;

        while ( this.runThread == true ) {
            try {
                Log.d("ServerInfo", "Waiting for input");
                input = bufferedreader.readLine(); //Blocking call

                //TODO something when player connects... Start game?
                // broadcast the received data
                Intent i = new Intent(BluetoothController.BT_SEND_DATA_INTENT);
                i.putExtra(BluetoothController.BT_SEND_DATA_INTENT_EXTRA_DATA, input);
                context.sendBroadcast(i);


            } catch (IOException e) {
                e.printStackTrace();
                this.runThread = false;
            }

        }

        //Close all connections and streams
        try {
            inputstream.close();
            outputstream.close();
            btsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stopServer() {
        runThread = false;
    }

    public void write(String string) throws IOException {
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputstream));
        bufferedwriter.write(string);
        bufferedwriter.flush();
    }
}
