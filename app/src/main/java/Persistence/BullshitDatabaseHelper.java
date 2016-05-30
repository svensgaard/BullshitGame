package Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mads on 16-05-2016.
 */
public class BullshitDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "localWins";
    private static final int DATABASE_VERSION = 1;
    public BullshitDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 1) {
            //Create database
            db.execSQL("CREATE TABLE GAMES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "WON INTEGER, " + "OPPONENT TEXT);"
            );
            //Insert some testData
            insertGame(db, 1, "00:A0:C9:14:C8:29");
            insertGame(db, 1, "00:A0:C9:14:C8:29");
            insertGame(db, 0, "00:A0:C9:14:C8:29");
        }
        if(oldVersion < 2) {
            //Update 1 to database IE add a new column or table
            //Continue oldVersion < 3 when updating the database
        }
    }

    private void insertGame(SQLiteDatabase db, int won, String opponent) {
        ContentValues values = new ContentValues();
        values.put("WON", won);
        values.put("OPPONENT", opponent);
        db.insert("GAMES", null, values);
    }
}
