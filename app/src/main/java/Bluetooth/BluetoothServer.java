package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;

/**
 * Created by Mads on 30-03-2016.
 */
public class BluetoothServer {
    private serverThread thread;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket btsocket = null;
    private Context context = null;


    public BluetoothServer(Context context, BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.context = context;

        thread = new serverThread(context, bluetoothAdapter);
    }


    //Start the server
    public void RunServer() {
        bluetoothAdapter.cancelDiscovery();
        thread.run();
    }
    public void stopServer() {
        //Stop the server
        thread.stopServer();
    }
    //Send data
    public void write(String s) throws IOException {
        thread.write(s);
    }
}
