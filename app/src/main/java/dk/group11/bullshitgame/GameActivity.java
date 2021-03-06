package dk.group11.bullshitgame;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import BluetoothV2.BluetoothService;
import BluetoothV2.Constants;
import Persistence.BullshitDatabaseHelper;
import ShakeDetector.ShakeDetector;

public class GameActivity extends AppCompatActivity {

    Button mButton_Quit, mButton_Guess, mButton_Bullshit;
    ImageView die_1, die_2, die_3, die_4, die_5, die_6;
    ImageView opp_die_1, opp_die_2, opp_die_3, opp_die_4, opp_die_5, opp_die_6;
    AnimationDrawable animation1, animation2, animation3, animation4, animation5, animation6;
    AnimationDrawable opp_animation1, opp_animation2, opp_animation3, opp_animation4, opp_animation5, opp_animation6;
    ArrayList<Drawable> dice_drawables;
    ArrayList<ImageView> playerDices, opponentDices;
    Spinner spinner_guess_amount;
    Spinner spinner_guess_dice;
    boolean shakeEnabled;
    boolean myTurn;
    Random rand;
    int currentGuessAmount;
    int currentGuessValue;

    Boolean dicesReceived = false;
    String MACaddress;

    BluetoothService btService;
    BluetoothAdapter btAdapter;

    public static final String EXTRA_CONNECTION_TYPE = "connection type";
    public static final int BT_DEVICE_REQUEST = 1;
    String connectionType = "";

    int player_score;
    int opponent_score;

    int player_remaining_dice;
    int opponent_remaining_dice;

    int remaining_dice;

    private ShakeDetector mShakeDetector;

    TextView mOpponent_score, mPlayer_score, mGuess, mRemaining_dice, mTurn_label;

    //Database
    SQLiteOpenHelper bullshitDatabaseHelper = new BullshitDatabaseHelper(this);
    SQLiteDatabase database = bullshitDatabaseHelper.getWritableDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btService = new BluetoothService(this, mHandler);

        mShakeDetector = new ShakeDetector(GameActivity.this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            public void onShake(int count) {
                if (shakeEnabled) {
                    roll();
                }
            }
        });

        mShakeDetector.disable();

        Intent i = getIntent();

        connectionType = i.getStringExtra(EXTRA_CONNECTION_TYPE);
        myTurn = false;
        Log.d("ConnectionType: ", connectionType);

            btService.start();
            if (connectionType.equals("client")) {
                Intent intent = new Intent(this, BT_Device.class);
                startActivityForResult(intent, BT_DEVICE_REQUEST);
                myTurn = true;
            }



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Log.d("INFO", "Result cancelled :(");
            // Cancelled
        }
        else if (requestCode == BT_DEVICE_REQUEST) {
            MACaddress = data.getExtras().getString(BT_Device.EXTRA_DEVICE_ADDRESS);
            BluetoothDevice device = btAdapter.getRemoteDevice(MACaddress);
            btService.connect(device, false);
        }
    }

    private void intialize() {
        mTurn_label = (TextView) findViewById(R.id.textView_turn_label);
        mGuess = (TextView) findViewById(R.id.textView_guess);
        mButton_Bullshit = (Button) findViewById(R.id.button_bullshit);
        mButton_Guess = (Button) findViewById(R.id.button_guess);
        mButton_Quit = (Button) findViewById(R.id.button_quit);

        mOpponent_score = (TextView) findViewById(R.id.textView_opponent_score);
        mPlayer_score = (TextView) findViewById(R.id.textView_player_score);
        mRemaining_dice = (TextView) findViewById(R.id.textView_remaining_dice_number);

        mOpponent_score.setText("0");
        mPlayer_score.setText("0");
        mGuess.setText("");

        player_score = 0;
        opponent_score = 0;

        die_1 = (ImageView) findViewById(R.id.die_place_1);
        die_2 = (ImageView) findViewById(R.id.die_place_2);
        die_3 = (ImageView) findViewById(R.id.die_place_3);
        die_4 = (ImageView) findViewById(R.id.die_place_4);
        die_5 = (ImageView) findViewById(R.id.die_place_5);
        die_6 = (ImageView) findViewById(R.id.die_place_6);

        opp_die_1 = (ImageView) findViewById(R.id.imageView_opponent_die_place_1);
        opp_die_2 = (ImageView) findViewById(R.id.imageView_opponent_die_place_2);
        opp_die_3 = (ImageView) findViewById(R.id.imageView_opponent_die_place_3);
        opp_die_4 = (ImageView) findViewById(R.id.imageView_opponent_die_place_4);
        opp_die_5 = (ImageView) findViewById(R.id.imageView_opponent_die_place_5);
        opp_die_6 = (ImageView) findViewById(R.id.imageView_opponent_die_place_6);

//        animation1 = (AnimationDrawable) die_1.getBackground();
//        animation2 = (AnimationDrawable) die_2.getBackground();
//        animation3 = (AnimationDrawable) die_3.getBackground();
//        animation4 = (AnimationDrawable) die_4.getBackground();
//        animation5 = (AnimationDrawable) die_5.getBackground();
//        animation6 = (AnimationDrawable) die_6.getBackground();
//
//        opp_animation1 = (AnimationDrawable) opp_die_1.getBackground();
//        opp_animation2 = (AnimationDrawable) opp_die_2.getBackground();
//        opp_animation3 = (AnimationDrawable) opp_die_3.getBackground();
//        opp_animation4 = (AnimationDrawable) opp_die_4.getBackground();
//        opp_animation5 = (AnimationDrawable) opp_die_5.getBackground();
//        opp_animation6 = (AnimationDrawable) opp_die_6.getBackground();

        dice_drawables = new ArrayList<>();
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die1, null));
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die2, null));
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die3, null));
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die4, null));
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die5, null));
        dice_drawables.add(ResourcesCompat.getDrawable(getResources(), R.drawable.die6, null));
        rand = new Random();

        playerDices = new ArrayList<>();
        opponentDices = new ArrayList<>();

        playerDices.add(die_1);
        playerDices.add(die_2);
        playerDices.add(die_3);
        playerDices.add(die_4);
        playerDices.add(die_5);
        playerDices.add(die_6);
        opponentDices.add(opp_die_1);
        opponentDices.add(opp_die_2);
        opponentDices.add(opp_die_3);
        opponentDices.add(opp_die_4);
        opponentDices.add(opp_die_5);
        opponentDices.add(opp_die_6);

        remaining_dice = playerDices.size() + opponentDices.size();
        player_remaining_dice = playerDices.size();
        opponent_remaining_dice = opponentDices.size();
        mRemaining_dice.setText(String.valueOf(remaining_dice));

        // HIDE OPPONENT DICE
        hideOpponentDice();

        // READY TO PLAY
        newRound();
    }

    public void newRound() {
        enableButtons();
        mGuess.setText("");
        hideOpponentDice();

            new AlertDialog.Builder(this)
                    .setTitle("New round")
                    .setMessage("Click ok, and start shaking!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            shakeEnabled = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        if  (!myTurn) {
            disableButtons();
        }
    }

    public void roll() {

        String roll = "";

        for (ImageView d : playerDices) {
            if (d.getVisibility() != View.GONE) {
                int next = rand.nextInt(6);
                d.setBackground(dice_drawables.get(next));
                roll += next;
            }
        }

        shakeEnabled = false;
        sendMessage(Constants.SEND_ROLL + roll);
        if(dicesReceived) {
            if(myTurn) {
                mTurn_label.setText("Make a guess!");
            } else {
                mTurn_label.setText("Waiting for opponent");
                disableButtons();
            }
        } else {
            mTurn_label.setText("Waiting for opponent to roll");
        }


    }

    private void disableButtons() {
        mButton_Guess.setEnabled(false);
        mButton_Bullshit.setEnabled(false);
    }

    public void rollHandler(View view) {
//        die_1.setBackground(dice_drawables.get(rand.nextInt(6)));
//        die_2.setBackground(dice_drawables.get(rand.nextInt(6)));
//        die_3.setBackground(dice_drawables.get(rand.nextInt(6)));
//        die_4.setBackground(dice_drawables.get(rand.nextInt(6)));
//        die_5.setBackground(dice_drawables.get(rand.nextInt(6)));
//        die_6.setBackground(dice_drawables.get(rand.nextInt(6)));
//
//        opp_die_1.setBackground(dice_drawables.get(rand.nextInt(6)));
//        opp_die_2.setBackground(dice_drawables.get(rand.nextInt(6)));
//        opp_die_3.setBackground(dice_drawables.get(rand.nextInt(6)));
//        opp_die_4.setBackground(dice_drawables.get(rand.nextInt(6)));
//        opp_die_5.setBackground(dice_drawables.get(rand.nextInt(6)));
//        opp_die_6.setBackground(dice_drawables.get(rand.nextInt(6)));

        roll();
    }

    private void populateSpinner() {
        spinner_guess_amount = (Spinner) findViewById(R.id.spinner_guess_amount);
        spinner_guess_dice = (Spinner) findViewById(R.id.spinner_guess_dice);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dice, android.R.layout.simple_spinner_item);

        ArrayList<String> spinner_amount_array = new ArrayList<>();
        for (int i = 1; i < remaining_dice; i++) {
            spinner_amount_array.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter_amount = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinner_amount_array);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_amount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner_guess_amount.setAdapter(adapter_amount);
        spinner_guess_dice.setAdapter(adapter);
    }

    public void showOpponentDice() {
        LinearLayout l = (LinearLayout) findViewById(R.id.opponent_dice_holder);
        l.setVisibility(View.VISIBLE);
    }

    public void hideOpponentDice() {
        LinearLayout l = (LinearLayout) findViewById(R.id.opponent_dice_holder);
        l.setVisibility(View.GONE);
    }

    public void quitHandler(View view) {
        finish();
    }

    public void bullShitHandler(View view) {
        checkBullshit();
    }

    public void endGame() {
        if (playerDices.size() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Game over")
                    .setMessage("You won!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("WON", 1);
                            values.put("OPPONENT", MACaddress);
                            database.insert("GAMES", null, values);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Game over")
                    .setMessage("You lost!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            values.put("WON", 0);
                            values.put("OPPONENT", MACaddress);
                            database.insert("GAMES", null, values);
                            finish();
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void checkBullshit() {
        showOpponentDice();
        int amount_of_guessed_dice = 0;

        // COUNTS AMOUNT OF GUESSED DIE
        for (ImageView i : playerDices) {
            String this_dice = i.getBackground().toString();
            if (this_dice.contains(String.valueOf(currentGuessValue))) {
                amount_of_guessed_dice++;
            }
        }

        for (ImageView i : opponentDices) {
            String this_dice = i.getBackground().toString();
            if (this_dice.contains(String.valueOf(currentGuessValue))) {
                amount_of_guessed_dice++;
            }
        }

        if (currentGuessAmount < amount_of_guessed_dice) {
            lost();
            sendMessage(Constants.SEND_BULLSHIT + "1");
            new AlertDialog.Builder(this)
                    .setTitle("Round over")
                    .setMessage("You lost!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            won();
            sendMessage(Constants.SEND_BULLSHIT + "0");
            new AlertDialog.Builder(this)
                    .setTitle("Round over")
                    .setMessage("You won!")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void won() {
        if (playerDices.size() <= 0 || opponentDices.size() <= 0) {
            endGame();
        }
        myTurn = false;
        newRound();
        player_score++;
        mPlayer_score.setText(String.valueOf(player_score));
        player_remaining_dice--;
        remaining_dice--;
        playerDices.get(player_remaining_dice - 1).setVisibility(View.GONE);
        mRemaining_dice.setText(String.valueOf(remaining_dice));
        dicesReceived = false;
    }

    public void lost() {
        if (playerDices.size() <= 0 || opponentDices.size() <= 0) {
            endGame();
        }
        myTurn = true;
        newRound();
        remaining_dice--;
        opponent_remaining_dice--;
        opponentDices.get(opponent_remaining_dice - 1).setVisibility(View.GONE);
        mOpponent_score.setText(String.valueOf(opponent_score++));
        mRemaining_dice.setText(String.valueOf(remaining_dice));
        dicesReceived = false;
    }

    public void guessHandler(View view) {

        final String guess = spinner_guess_amount.getSelectedItem().toString() + "x of " +spinner_guess_dice.getSelectedItem().toString()+"'s";

        sendMessage(Constants.SEND_GUESS + spinner_guess_amount.getSelectedItem().toString() + spinner_guess_dice.getSelectedItem().toString());
        new AlertDialog.Builder(this)
                .setTitle("Your guess")
                .setMessage(guess)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mGuess.setText(guess);
                        mTurn_label.setText("Waiting...");
                        disableButtons();
                        myTurn = false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d("BT_STATE", "CONNECTED");
                            intialize();

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("BT_STATE", "connectiong");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("BT_STATE","NOT CONNECTED");
                            break;
                    }

                    break;
                case Constants.MESSAGE_WRITE:

                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("Recevied",readMessage);
                    if(readMessage.contains(Constants.SEND_ROLL)) {
                            String[] recieved = readMessage.split(",");
                            String roll = recieved[1];
                        Log.d("ROLL: ", roll);

                            // SETS OPPONENT DICES
                            for (int i = 0; i < roll.length();i++) {
                                Log.d("SIZE OPPONENT:", String.valueOf(opponentDices.size()) );
                                Log.d("SIZE DRAWABALES:", String.valueOf(dice_drawables.size()));
                            opponentDices.get(i).setBackground(dice_drawables.get(Integer.parseInt(String.valueOf(roll.charAt(i)))));
                            }
                        populateSpinner();
                        dicesReceived = true;

                        if(myTurn) {
                            mTurn_label.setText("Make a guess!");
                        } else {
                            mTurn_label.setText("Waiting for opponent");
                            disableButtons();
                        }
                    }
                    if(readMessage.contains(Constants.SEND_GUESS)) {
                        String[] recieved = readMessage.split(",");
                        String roll = recieved[1];
                        currentGuessAmount = Integer.parseInt(String.valueOf(roll.charAt(0)));
                        currentGuessValue = Integer.parseInt(String.valueOf(roll.charAt(1)));
                        mGuess.setText(currentGuessAmount + "x of " + currentGuessValue);
                        myTurn = true;
                        enableButtons();
                    }

                    if(readMessage.contains(Constants.SEND_BULLSHIT)) {
                        String[] recieved = readMessage.split(",");
                        int result = Integer.parseInt(recieved[1]);

                        if (result == 1) {
                            won();
                            Toast.makeText(GameActivity.this, "YOU WON", Toast.LENGTH_SHORT).show();
                        } else {
                            lost();
                            Toast.makeText(GameActivity.this, "YOU LOST", Toast.LENGTH_SHORT).show();

                        }
                    }


                    break;
                case Constants.MESSAGE_DEVICE_NAME:

                    break;
                case Constants.MESSAGE_TOAST:

                    break;
            }
        }
    };

    private void enableButtons() {
        mButton_Bullshit.setEnabled(true);
        mButton_Guess.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        mShakeDetector.register();


    }
    @Override
    public void onPause() {
        mShakeDetector.unregister();
        super.onPause();
    }
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (btService.getState() != btService.STATE_CONNECTED) {
            Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            btService.write(message.getBytes());
        }
    }

}
