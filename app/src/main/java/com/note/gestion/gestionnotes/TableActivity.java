package com.note.gestion.gestionnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.note.gestion.carte.DishList;
import com.note.gestion.carte.Group;
import com.note.gestion.carte.GroupList;
import com.note.gestion.table.Table;
import com.note.gestion.table.TableAdapter;
import com.note.gestion.vat.VatList;

import java.util.ArrayDeque;
import java.util.Deque;

public class TableActivity extends AppCompatActivity {
    private AppDatabase m_dataBase;

    private Table m_table;

    private TableAdapter m_tableAdapter;

    private VatList m_vatList;

    private Group m_currentGroup;
    private GroupList m_groupList;
    private DishList m_dishList;
    private Deque<Group> m_previousGroups = new ArrayDeque<>();

    private CarteAdapter m_carteAdapter;
    private GridView m_gridView;

    private void getCurrentLists() {
        m_groupList = new GroupList( m_currentGroup.getId(), m_dataBase.groupDAO().getAllByParentGroup( m_currentGroup.getId() ) );
        m_dishList = new DishList( m_currentGroup, m_dataBase.dishDAO().getAllByGroup( m_currentGroup.getId() ) );

        //For nested object
        for( Group group : m_groupList.getGroups() ) {
            group.setVat( m_vatList.getVat( group.getVatId() - 1 ) );
        }

        for( Dish dish : m_dishList.getDishes() ) {
            dish.setVat( m_vatList.getVat( dish.getVatId() - 1 ) );
        }
    }

    private void hardReloadLists() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                getCurrentLists();
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                m_carteAdapter = new CarteAdapter( TableActivity.this, R.layout.grid_view_item_carte, m_groupList.getGroups(), m_dishList.getDishes() );
                m_gridView.setAdapter( m_carteAdapter );
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_dataBase = AppDatabase.getInstance( getApplicationContext() );

        //TODO changer la recuperation de l'intent avec les DAOs
        //  Creer une table d'association pour lier les dish avec la table
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_table = m_dataBase.tableDAO().getById( getIntent().getIntExtra( MainActivity.TABLE, 1 ) );

                m_vatList = new VatList( m_dataBase.vatDAO().getAll() );
                m_currentGroup = m_dataBase.groupDAO().getById( 1 );
                m_currentGroup.setVat( m_vatList.getVat( m_currentGroup.getVatId() - 1 ) );

                getCurrentLists();
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                setTitle( getTitle() + " " + m_table.getDesignation() );

                m_carteAdapter = new CarteAdapter(TableActivity.this,
                        R.layout.grid_view_item_carte, m_groupList.getGroups(), m_dishList.getDishes() );

                m_gridView = findViewById( R.id.carte_grid );
                m_gridView.setAdapter( m_carteAdapter );
                m_gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id ) {
                        if( position < m_groupList.getGroups().size() ) {
                            m_previousGroups.push( m_currentGroup );
                            m_currentGroup = m_groupList.getGroup( position );

                            hardReloadLists();
                        }  else {
                            //TODO ajouter un dish a la table
                        }
                    }
                } );
            }
        }.execute();

        /*m_tableAdapter = new TableAdapter(this,
                android.R.layout.simple_list_item_2, m_table.getDishes() );

        //ListView pour les tables
        ListView listView = findViewById( R.id.dish_list );
        listView.setAdapter( m_tableAdapter );*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if( m_previousGroups.size() != 0 ) {
            m_currentGroup =  m_previousGroups.pop();
            hardReloadLists();
        } else {
            finish();
        }
    }
}
