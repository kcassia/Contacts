package kr.ac.ajou.media.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity
{
    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");

        ((EditText)findViewById(R.id.name_edit)).setText(name);
        ((EditText)findViewById(R.id.phone_edit)).setText(phone);

        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null)
            actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = new Intent();
        if(item.getItemId() == R.id.edit_ok_button) // 수정
        {
            Toast.makeText(this, "수정", Toast.LENGTH_SHORT).show();
            name = ((EditText)findViewById(R.id.name_edit)).getText().toString();
            phone = ((EditText)findViewById(R.id.phone_edit)).getText().toString();
        }
        else if(item.getItemId() == R.id.delete_button) // 삭제
            Toast.makeText(this, "삭제", Toast.LENGTH_SHORT).show();
        else if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        setResult(RESULT_OK, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
