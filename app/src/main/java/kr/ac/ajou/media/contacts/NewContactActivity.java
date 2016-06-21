package kr.ac.ajou.media.contacts;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NewContactActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null)
            actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.new_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String name = ((EditText)findViewById(R.id.name_new)).getText().toString();
        String phone = ((EditText)findViewById(R.id.phone_new)).getText().toString();

        if(item.getItemId() == R.id.new_ok_button)
            Toast.makeText(this, "추가", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
