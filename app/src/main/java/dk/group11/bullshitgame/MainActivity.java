package dk.group11.bullshitgame;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import Bluetooth.BluetoothController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    BluetoothController bluetoothController = new BluetoothController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(bluetoothController.startBluetooth()) {
            int REQUEST_ENABLE_BT = 1;
            Intent bluetooth_intent = bluetoothController.checkBluetoothEnable();
            if (bluetooth_intent != null) {
                startActivityForResult(bluetooth_intent, REQUEST_ENABLE_BT);
            }
        }



    }
    //OnClick listener for all buttons
    public void onClick(View v) {
        if(v.getId() == R.id.hostGameBtn) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_CONNECTION_TYPE, "server");
            startActivity(intent);
        } else if(v.getId() == R.id.findGameBtn) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_CONNECTION_TYPE, "client");
            startActivity(intent);
        } else if(v.getId() == R.id.settingsBtn) {
            //TODO start settings
        } else if(v.getId() == R.id.closeBtn) {
            //TODO close
        }
    }
}
