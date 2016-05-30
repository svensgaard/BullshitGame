package dk.group11.bullshitgame;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import Persistence.BullshitDatabaseHelper;
import Persistence.HistoryCursorAdapter;

public class DisplayHistory extends ListActivity {
    private SQLiteDatabase database;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listHistory = getListView();

        SQLiteOpenHelper bullshitDatabaseHelper = new BullshitDatabaseHelper(this);
        database = bullshitDatabaseHelper.getReadableDatabase();

        cursor = database.query("GAMES", new String[]{"_id", "OPPONENT", "WON"}, null, null, null, null, null);

//        CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"_id", "OPPONENT", "WON"}, new int[] {android.R.id.text1, android.R.id.text1,android.R.id.text1}, 0);
        HistoryCursorAdapter adapter = new HistoryCursorAdapter(this, cursor, 0);
        listHistory.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }
}
