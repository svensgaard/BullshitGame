package Persistence;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import dk.group11.bullshitgame.R;

/**
 * Created by Mads on 16-05-2016.
 */
public class HistoryCursorAdapter extends CursorAdapter {
    public HistoryCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Get views to code
        TextView gameIDTextView = (TextView) view.findViewById(R.id.gameIDTextView);
        TextView wonTextView = (TextView) view.findViewById(R.id.wonTextView);
        TextView opponentTextView = (TextView) view.findViewById(R.id.opponentTextView);
        //Get data from cursor
        int gameID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int won = cursor.getInt(cursor.getColumnIndexOrThrow("WON"));
        String opponent = cursor.getString(cursor.getColumnIndexOrThrow("OPPONENT"));
        //Set texts
        gameIDTextView.setText(String.valueOf(gameID));
        wonTextView.setText(String.valueOf(won));
        opponentTextView.setText(opponent);
    }
}
