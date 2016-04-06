package Bluetooth;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Mads on 30-03-2016.
 */
public class BluetoothServer {
    private ServerThread thread = new ServerThread();
    private BluetoothAdapter bluetoothAdapter;


    public BluetoothServer(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

}
