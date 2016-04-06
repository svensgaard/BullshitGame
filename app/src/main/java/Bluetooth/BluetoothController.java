package Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mads on 30-03-2016.
 */
public class BluetoothController {
    BluetoothAdapter mBluetoothAdapter;
    Context context;

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

}
