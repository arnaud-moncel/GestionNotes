package com.note.gestion.gestionnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.note.gestion.carte.CarteAdapter;
import com.note.gestion.carte.Dish;
import com.note.gestion.carte.Group;
import com.note.gestion.table.Table;
import com.note.gestion.table.TableAdapter;

import java.util.ArrayDeque;
import java.util.Deque;

public class TableActivity extends AppCompatActivity {

    private Table m_table;

    private TableAdapter m_tableAdapter;

    private Group m_carte;
    private Deque<Group> m_previousGroups = new ArrayDeque<>();

    private CarteAdapter m_carteAdapter;
    private GridView m_gridView;

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

        m_tableAdapter = new TableAdapter(this,
                android.R.layout.simple_list_item_2, m_table.getDishes() );

        //ListView pour les tables
        ListView listView = findViewById( R.id.dish_list );
        listView.setAdapter( m_tableAdapter );

        m_carte = ( Group ) getIntent().getSerializableExtra( MainActivity.CARTE );
        m_carteAdapter = new CarteAdapter(this,
                R.layout.grid_view_item_carte, m_carte.getItems() );

        m_gridView = findViewById( R.id.carte_grid );
        m_gridView.setAdapter( m_carteAdapter );
        m_gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
                if( m_carte.getItem( position ) instanceof Group ) {
                    m_previousGroups.push( m_carte );
                    m_carte = ( Group ) m_carte.getItem( position );
                    m_carteAdapter = new CarteAdapter( view.getContext(), R.layout.grid_view_item_carte, m_carte.getItems() );
                    m_gridView.setAdapter( m_carteAdapter );
                    m_carteAdapter.notifyDataSetChanged();
                } else {
                    m_table.addDish( (Dish) m_carte.getItem( position ) );
                    m_tableAdapter.notifyDataSetChanged();
                }
            }
        } );
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
        if( m_previousGroups.size() != 0 ) {
            m_carte =  m_previousGroups.pop();
            m_carteAdapter = new CarteAdapter( this, R.layout.grid_view_item_carte, m_carte.getItems() );
            m_gridView.setAdapter( m_carteAdapter );
            m_carteAdapter.notifyDataSetChanged();
        } else {
            sendIntent();
        }
    }
}
