package Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Mads on 30-03-2016.
 */
public class ClientThread extends Thread {
    public Boolean runThread = true;
    private BluetoothSocket btSocket = null;
    private Context context;
    BufferedWriter bufferedWriter;
    OutputStream outputStream = null;

    public ClientThread(Context context, BluetoothSocket btSocket) {
        this.context = context;
        this.btSocket = btSocket;
    }
    @Override
    public void run() {
        super.run();



        try {
            btSocket.connect();
            write("Client connected");
        } catch (IOException e) {

            return;
        }


        InputStream inputstream = null;


        try {
            inputstream = btSocket.getInputStream();
            outputStream = btSocket.getOutputStream();
        } catch (IOException e) {

            return;
        }

        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        String line;

        while ( this.runThread == true ) {
            try {

                line = bufferedreader.readLine(); //blocking until a whole line has been read.

                // broadcast the received data
                Intent i = new Intent(BluetoothController.BT_SEND_DATA_INTENT);
                i.putExtra(BluetoothController.BT_SEND_DATA_INTENT_EXTRA_DATA, line);
                context.sendBroadcast(i);

            } catch (IOException e) {
                return;
            }

        }

        // close down nicely,
        try {
            bufferedreader.close();
            inputstream.close();
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void write(String s) throws IOException {
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedwriter.write(s);
        bufferedwriter.flush();
    }

}
