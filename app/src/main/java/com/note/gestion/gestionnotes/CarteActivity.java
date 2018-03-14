package com.note.gestion.gestionnotes;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.note.gestion.carte.CarteAdapter;
import com.note.gestion.carte.Group;
import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatList;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;


public class CarteActivity extends AppCompatActivity
        implements SpinnerAddDialog.NoticeDialogListener,
        DoubleAddDialog.NoticeDialogListener {

    private VatList m_vatList;

    private Group m_carte;
    private Deque<Group> m_previousGroups = new ArrayDeque<>();

    private CarteAdapter m_carteAdapter;
    private GridView m_gridView;

    private Boolean m_isFabOpen = false;

    private FloatingActionButton m_fab;
    private FloatingActionButton m_fabAddItem;
    private FloatingActionButton m_fabAddGroup;

    private Animation m_animClose;
    private Animation m_animOpen;
    private Animation m_animRotBack;
    private Animation m_animRotFor;

    private static final String CARTE_GROUP = "com.note.gestion.CARTE.GROUP";
    private static final String CARTE_DISH = "com.note.gestion.CARTE.DISH";

    private void animateflocationBtn(){
        if( m_isFabOpen ){
            m_fab.startAnimation( m_animRotBack );
            m_fabAddGroup.startAnimation( m_animClose );
            m_fabAddItem.startAnimation( m_animClose );
            m_fabAddGroup.setClickable( false );
            m_fabAddItem.setClickable( false );
            m_isFabOpen = false;
        } else {
            m_fab.startAnimation( m_animRotFor );
            m_fabAddGroup.startAnimation( m_animOpen );
            m_fabAddItem.startAnimation( m_animOpen );
            m_fabAddGroup.setClickable( true );
            m_fabAddItem.setClickable( true );
            m_isFabOpen = true;
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_carte );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        m_vatList = (VatList) getIntent().getSerializableExtra( MainActivity.VAT );
        m_carte = ( Group ) getIntent().getSerializableExtra( MainActivity.CARTE );

        m_fab = findViewById( R.id.fab );
        m_fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                animateflocationBtn();
            }
        } );

        m_fabAddGroup = findViewById( R.id.fab_add_group );
        m_fabAddGroup.setOnClickListener( new  View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                animateflocationBtn();
                DialogFragment addGroupDialog = SpinnerAddDialog.newInstance(
                        R.string.add_group_dialog_title,
                        R.string.add_group_dialog_message,
                        R.string.add_group_vat_dialog_message,
                        m_vatList.toStringArray()
                );
                addGroupDialog.show( getFragmentManager(), CARTE_GROUP );
            }
        } );

        m_fabAddItem = findViewById( R.id.fab_add_item );
        m_fabAddItem.setOnClickListener( new  View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                animateflocationBtn();
                DialogFragment addDishDialog = DoubleAddDialog.newInstance( R.string.add_dish_dialog_title, R.string.add_dish_dialog_message, R.string.add_dish_price_dialog_message );
                addDishDialog.show( getFragmentManager(), CARTE_DISH );
            }
        } );

        m_animOpen = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fab_open );
        m_animClose = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fab_close );
        m_animRotFor = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_forward );
        m_animRotBack = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_backward );

        m_carteAdapter = new CarteAdapter(this,
                R.layout.grid_view_carte, m_carte.getItems() );

        m_gridView = findViewById( R.id.carte_grid );
        m_gridView.setAdapter( m_carteAdapter );
        m_gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
                if( m_carte.getItem( position ) instanceof Group ) {
                    m_previousGroups.push( m_carte );
                    m_carte = ( Group ) m_carte.getItem( position );
                    m_carteAdapter = new CarteAdapter( view.getContext(), R.layout.grid_view_carte, m_carte.getItems() );
                    m_gridView.setAdapter( m_carteAdapter );
                    m_carteAdapter.notifyDataSetChanged();
                }
            }
        } );
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newEdt = dialog.getDialog().findViewById( R.id.edit_text );

        if( dialog.getTag().equals( CARTE_GROUP ) ) {
            Spinner newSpinner = dialog.getDialog().findViewById( R.id.vat_spinner );

            Vat vat = m_vatList.getVat( newSpinner.getSelectedItemPosition() );
            m_carte.addGroup( newEdt.getText().toString(), vat );
        } else {
            EditText newEdtc = dialog.getDialog().findViewById( R.id.edit_decimal );
            m_carte.addDish( newEdt.getText().toString(), Double.valueOf( newEdtc.getText().toString() ) );
        }

        m_carteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if( m_previousGroups.size() != 0 ) {
            m_carte =  m_previousGroups.pop();
            m_carteAdapter = new CarteAdapter( this, R.layout.grid_view_carte, m_carte.getItems() );
            m_gridView.setAdapter( m_carteAdapter );
            m_carteAdapter.notifyDataSetChanged();
        } else {
            sendIntent();
        }
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.putExtra( MainActivity.CARTE, m_carte );
        setResult( Activity.RESULT_OK, intent );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //get the main group
                while ( !m_previousGroups.isEmpty() ) {
                    m_carte = m_previousGroups.pop();
                }
                sendIntent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();

        //get the main group
        while ( !m_previousGroups.isEmpty() ) {
            m_carte = m_previousGroups.pop();
        }

        try {
            FileOutputStream fos = openFileOutput( MainActivity.CARTE_FILE_NAME, Context.MODE_PRIVATE );
            ObjectOutputStream os = new ObjectOutputStream( fos );
            os.writeObject( m_carte );
            os.close();
            fos.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
