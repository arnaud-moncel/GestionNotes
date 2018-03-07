package com.note.gestion.gestionnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.note.gestion.table.Table;

public class TableActivity extends AppCompatActivity {

    Table m_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        m_table = (Table) intent.getSerializableExtra( MainActivity.TABLE );
        setTitle( getTitle() + " " + m_table.getId() );
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.putExtra( MainActivity.TABLE, m_table );
        setResult( Activity.RESULT_OK, intent );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                sendIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendIntent();
    }
}
