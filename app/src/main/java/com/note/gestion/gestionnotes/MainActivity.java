package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.content.Intent;
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

import com.note.gestion.table.Table;
import com.note.gestion.table.TableList;
import com.note.gestion.table.TableListAdapter;
import com.note.gestion.vat.VatList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SimpleAddDialog.NoticeDialogListener {

    private TableList m_tableList = new TableList();
    private TableListAdapter m_tableListAdpter;

    private VatList m_vatList = new VatList();

    public static final String TABLE = "com.note.gestion.TABLE";
    public static final String CARTE = "com.note.gestion.CASTE";
    public static final String VAT = "com.note.gestion.VAT";

    public static final int REQ_TABLE = 1;
    public static final int REQ_CARTE = 2;
    public static final int REQ_VAT = 3;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

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

        m_tableListAdpter = new TableListAdapter(this,
                android.R.layout.simple_list_item_2, m_tableList.getTables() );

        //ListView pour les tables
        ListView listView = findViewById( R.id.table_list );
        listView.setAdapter( m_tableListAdpter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
                Intent intent = new Intent( MainActivity.this, TableActivity.class );
                intent.putExtra( TABLE, m_tableList.getTable( position ) );
                MainActivity.this.startActivityForResult( intent, REQ_TABLE );
            }
        } );
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

    @SuppressWarnings( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if ( id == R.id.nav_carte ) {
            Intent intent = new Intent( MainActivity.this, CarteActivity.class );
            MainActivity.this.startActivityForResult( intent, REQ_CARTE );
        } else if ( id == R.id.nav_VAT ) {
            Intent intent = new Intent( MainActivity.this, VatActivity.class );
            intent.putExtra( VAT, m_vatList );
            MainActivity.this.startActivityForResult( intent, REQ_VAT );
        }

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch ( requestCode ) {
            case REQ_TABLE:
                m_tableList.seTable( ( Table ) data.getSerializableExtra( TABLE ) );
                break;

            case REQ_CARTE:
                break;

            case REQ_VAT:
                m_vatList = ( VatList ) data.getSerializableExtra( VAT );
                break;
        }
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newTableEdt = dialog.getDialog().findViewById( R.id.edit_text );
        m_tableList.createTable( newTableEdt.getText().toString() );
        m_tableListAdpter.notifyDataSetChanged();
    }
}