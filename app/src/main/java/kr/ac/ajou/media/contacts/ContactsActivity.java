package kr.ac.ajou.media.contacts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class ContactsActivity extends AppCompatActivity
{
    private ArrayList<Contact> contacts;
    private static final int NEW_CONTACT_ACTIVITY = 1;
    private static final int CONTACT_ACTIVITY = 2;
    private DBHelper dbHelper;
    private ContactCursorAdapter cursorAdapter;
    private BaseAdapter listAdapter;
    private boolean isChange;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contacts = new ArrayList<>();
        dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.readAllContacts();
        cursorAdapter = new ContactCursorAdapter(this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        isChange = false;

        if(cursor.moveToFirst())
            do
            {
                contacts.add(new Contact(cursor.getString(1), cursor.getString(2)));
            }while(cursor.moveToNext());


        ListView listView = (ListView)findViewById(R.id.list);
        listAdapter = new BaseAdapter()
        {
            @Override
            public int getCount(){return contacts.size();}

            @Override
            public Object getItem(int position){return contacts.get(position);}

            @Override
            public long getItemId(int position){return position;}

            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if(convertView == null || isChange)
                {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                    ((TextView)convertView.findViewById(R.id.name_item)).setText(contacts.get(position).getName());
                    ((TextView)convertView.findViewById(R.id.phone_item)).setText(contacts.get(position).getPhone());
                }
                return convertView;
            }
        };
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ContactsActivity.this, ContactActivity.class);
                intent.putExtra("name", contacts.get(position).getName());
                intent.putExtra("phone", contacts.get(position).getPhone());
                intent.putExtra("position", position);
                startActivityForResult(intent, CONTACT_ACTIVITY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Toast.makeText(this, "추가", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(this, NewContactActivity.class), NEW_CONTACT_ACTIVITY);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == NEW_CONTACT_ACTIVITY) // 추가
            {
                contacts.add(new Contact(data.getStringExtra("name"), data.getStringExtra("phone")));
                dbHelper.addContact(data.getStringExtra("name"), data.getStringExtra("phone"));
                Toast.makeText(this, "추가완료", Toast.LENGTH_LONG).show();
            }
            else if(requestCode == CONTACT_ACTIVITY)
            {
                if(data.getStringExtra("name").equals(contacts.get(data.getIntExtra("position", -1)).getName())
                        && data.getStringExtra("phone").equals(contacts.get(data.getIntExtra("position", -1)).getPhone())) // 삭제
                {
                    contacts.remove(data.getIntExtra("position", -1));
                    dbHelper.deleteContact(data.getStringExtra("name"), data.getStringExtra("phone"));
                    Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show();
                    isChange = true;
                    listAdapter.notifyDataSetChanged();
                }
                else // 수정
                    contacts.set(data.getIntExtra("position", -1), new Contact(data.getStringExtra("name"), data.getStringExtra("phone")));
            }
            Cursor newCursor = dbHelper.readAllContacts();
            Cursor oldCursor = cursorAdapter.swapCursor(newCursor);
            oldCursor.close();
        }
    }

    class ContactCursorAdapter extends CursorAdapter
    {

        public ContactCursorAdapter(Context context, Cursor c, int flags)
        {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            ((TextView)view.findViewById(R.id.name_item)).setText(cursor.getString(1));
            ((TextView)view.findViewById(R.id.phone_item)).setText(cursor.getString(2));
        }
    }
}
