package kr.ac.ajou.media.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kcassia on 2016-06-21.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE CONTACT (_id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PHONE TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS CONTACT");
        onCreate(db);
    }

    public Cursor readAllContacts()
    {
        return getReadableDatabase().rawQuery("SELECT * FROM CONTACT", null);
    }

    public void addContact(String name, String phone)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CONTACT(NAME, PHONE) VALUES ('" + name + "', '" + phone + "');");
        db.close();
    }

    public void deleteContact(String name, String phone)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CONTACT WHERE NAME = '" + name + "' AND PHONE = '" + phone + "';");
        db.close();
    }

    public void updateContact(String oldName, String oldPhone, String newName, String newPhone)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE CONTACT SET NAME = '" + newName + "', PHONE = '" + newPhone + "' WHERE NAME = '" + oldName + "' AND PHONE = '" + oldPhone + "';");
        db.close();
    }
}
