package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.webkit.ClientCertRequest;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Mads on 30-03-2016.
 */
public class BluetoothController {
    BluetoothAdapter mBluetoothAdapter;
    Context context;
    BluetoothServer btServer;
    BluetoothClient btClient;

    //Boolean for what type this application is when communicating.
    Boolean isServer;

    //Unique so that other parts of the system wont try to process
    public static String BT_SEND_DATA_INTENT = "dk.group11.bullshitgame.INTENT";
    public static String BT_SEND_DATA_INTENT_EXTRA_DATA = "dk.group11.bullshitgame.DATA";

    public BluetoothController(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        this.context = context;
    }
    public boolean startBluetooth() {
        //Check for bluetooth availability
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public Intent checkBluetoothEnable() {
        //Check if bluetooth is enabled if not request that it is.
        if (!mBluetoothAdapter.isEnabled()) {
            return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        } else {
            return null;
        }
    }
    public void startHostingServer() {
        btServer = new BluetoothServer(context,mBluetoothAdapter);
        btServer.RunServer();
        isServer = true;
    }
    public boolean connectToServer() throws IOException {
        btClient = new BluetoothClient(context, mBluetoothAdapter);
        try {
            btClient.connect();
            isServer = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void sendData(String s) throws IOException {
        if(isServer) {
            btServer.write(s);
        } else {
            btClient.write(s);
        }
    }

}
