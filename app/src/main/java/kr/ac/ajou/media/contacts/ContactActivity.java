package kr.ac.ajou.media.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity
{
    private String name;
    private String phone;
    private int position;
    private static final int EDIT_CONTACT_ACTIVITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        if(savedInstanceState == null)
        {
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            position = getIntent().getIntExtra("position", -1);
        }
        else
        {
            name = savedInstanceState.getString("name");
            phone = savedInstanceState.getString("phone");
            position = savedInstanceState.getInt("position");
        }

        ((TextView)findViewById(R.id.name_text)).setText(name);
        ((TextView)findViewById(R.id.phone_text)).setText(phone);

        findViewById(R.id.phone_text).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
            }
        });

        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null)
            actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.edit_button)
        {
            Toast.makeText(this, "수정/삭제", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, EditContactActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            startActivityForResult(intent, EDIT_CONTACT_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == EDIT_CONTACT_ACTIVITY)
            {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                if(name.equals(data.getStringExtra("name")) && phone.equals(data.getStringExtra("phone"))) // 삭제
                {
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else // 수정, Back
                {
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.updateContact(name, phone, data.getStringExtra("name"), data.getStringExtra("phone"));
                    name = data.getStringExtra("name");
                    phone = data.getStringExtra("phone");
                    ((TextView)findViewById(R.id.name_text)).setText(name);
                    ((TextView)findViewById(R.id.phone_text)).setText(phone);
                    Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
        outState.putString("phone", phone);
        outState.putInt("position", position);
    }
}
