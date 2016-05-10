package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Mads on 30-03-2016.
 */
public class BluetoothClient {
    public static final String BT_NEW_GUESS_INTENT = "new_guess";

    private ClientThread clientThread;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket btsocket = null;
    private Context context = null;
    private String MACAddress;

    public BluetoothClient(Context context, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;

    }

    //Find device, if none paired then start discovery.
    public void findDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {

            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                //TODO make arrayAdapter
                // Add the name and address to an array adapter to show in a ListView
                // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                MACAddress = device.getAddress();
            }

        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver, filter);
            bluetoothAdapter.startDiscovery();
        }
    }

    public void connect(String MACAddress) throws IOException {
        Log.d("Info", "Trying to connect");
        Log.d("Info","MAC" + MACAddress);
        try {
            btsocket = bluetoothAdapter.getRemoteDevice(MACAddress).createInsecureRfcommSocketToServiceRecord(ServerThread.RFCOMM_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientThread = new ClientThread(context, btsocket);
        bluetoothAdapter.cancelDiscovery();
        //Run thread
        clientThread.start();
    }

    public void disconnect() {
        clientThread.runThread = false;
    }


    public void setServerMAC(String s) {
        MACAddress = s;
    }
    //Send data
    public void write(String s) throws IOException {
        clientThread.write(s);
    }
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //TODO make array adapter
                // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                MACAddress=device.getAddress();
            }
        }
    };
}
