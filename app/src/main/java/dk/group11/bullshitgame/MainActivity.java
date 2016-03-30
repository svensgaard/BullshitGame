package dk.group11.bullshitgame;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import Bluetooth.BluetoothController;

public class MainActivity extends AppCompatActivity {
    BluetoothController bluetoothController = new BluetoothController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(bluetoothController.startBluetooth()) {
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(bluetoothController.checkBluetoothEnable(), REQUEST_ENABLE_BT);

        }



    }
    //OnClick listener for all buttons
    public void onClick(View v) {
        if(v.getId() == R.id.hostGameBtn) {
            //TODO start hosting a game
        } else if(v.getId() == R.id.findGameBtn) {
            //TODO search for a game
        } else if(v.getId() == R.id.settingsBtn) {
            //TODO start settings
        } else if(v.getId() == R.id.closeBtn) {
            //TODO close
        }
    }
}
