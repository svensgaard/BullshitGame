package dk.group11.bullshitgame;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {
    int BT_ENABLE = 1;
    private final int NOTIFY_TIME = 30000; //30 seconds
    private final String NOTIFY_TOAST = "You will be notified in " + NOTIFY_TIME+ " milliseconds";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        int BT_ENABLE = 1;
        //Check for bluetooth
        if(btAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Bluetooth not available!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            this.finishAffinity();
        }
        if(!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, BT_ENABLE);
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
            //Start discovarable
            Intent discoverableIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        } else if(v.getId() == R.id.closeBtn) {
            this.finishAffinity();
        } else if(v.getId() == R.id.historyBtn) {
            Intent intent = new Intent(this, DisplayHistory.class);
            startActivity(intent);
        } else if(v.getId() == R.id.notifyBtn) {
            NotifyService.startActionCount(this, NOTIFY_TIME);
            Toast.makeText(this, NOTIFY_TOAST,
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BT_ENABLE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //All is good
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Bluetooth is not enabled")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                this.finishAffinity();
            }
        }
    }
}
