package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.note.gestion.table.TableList;
import com.note.gestion.table.TableListAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SimpleAddDialog.NoticeDialogListener {
    private AppDatabase m_dataBase;

    private TableList m_tableList;
    private TableListAdapter m_tableListAdpter;

    public static final String TABLE = "com.note.gestion.TABLE";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        m_dataBase = AppDatabase.getInstance( getApplicationContext() );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                DialogFragment addTableDialog = SimpleAddDialog.newInstance( R.string.add_table_dialog_title, R.string.add_table_dialog_message );
                addTableDialog.show( getFragmentManager(), TABLE );
            }
        } );

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_tableList = new TableList( m_dataBase.tableDAO().getAll() );
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                m_tableListAdpter = new TableListAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_2, m_tableList.getTables() );

                //ListView pour les tables
                ListView listView = findViewById( R.id.table_list );
                listView.setAdapter( m_tableListAdpter );
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
                        Intent intent = new Intent( MainActivity.this, TableActivity.class );
                        intent.putExtra( TABLE, m_tableList.getTable( position ).getId() );
                        MainActivity.this.startActivity( intent );
                    }
                } );
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if ( id == R.id.nav_carte ) {
            MainActivity.this.startActivity( new Intent( MainActivity.this, CarteActivity.class ) );
        } else if ( id == R.id.nav_VAT ) {
            MainActivity.this.startActivity( new Intent( MainActivity.this, VatActivity.class ) );
        }

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }


    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newTableEdt = dialog.getDialog().findViewById( R.id.edit_text );
        m_tableList.createTable( newTableEdt.getText().toString() );

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_dataBase.tableDAO().insert( m_tableList.getLastTable() );
                return null;
            }
        }.execute();

        m_tableListAdpter.notifyDataSetChanged();
    }
}
