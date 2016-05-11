package dk.group11.bullshitgame;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
            //TODO close
        }
    }
}
