package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;

import com.note.gestion.carte.Carte;
import com.note.gestion.carte.CarteAdapter;

public class CarteActivity extends AppCompatActivity
        implements SimpleAddDialog.NoticeDialogListener {

    private Carte m_carte = new Carte();
    private CarteAdapter m_carteAdapter;

    private boolean m_isFabOpen = false;

    private FloatingActionButton m_fab;
    private FloatingActionButton m_fabAddItem;
    private FloatingActionButton m_fabAddGroup;

    private Animation m_animClose;
    private Animation m_animOpen;
    private Animation m_animRotBack;
    private Animation m_animRotFor;

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
                DialogFragment addTableDialog = SimpleAddDialog.newInstance( R.string.add_group_dialog_title, R.string.add_group_dialog_message );
                addTableDialog.show( getFragmentManager(), MainActivity.CARTE );
            }
        } );

        m_fabAddItem = findViewById( R.id.fab_add_item );
        m_fabAddItem.setOnClickListener( new  View.OnClickListener() {
            @Override
            public void onClick( View view ) {
            }
        } );

        m_animOpen = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fab_open );
        m_animClose = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fab_close );
        m_animRotFor = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_forward );
        m_animRotBack = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_backward );

        m_carteAdapter = new CarteAdapter(this,
                android.R.layout.simple_list_item_1, m_carte.getItems() );

        GridView gridView = findViewById( R.id.carte_grid );
        gridView.setAdapter( m_carteAdapter );
        /*gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
                Intent intent = new Intent( MainActivity.this, TableActivity.class );
                intent.putExtra( TABLE, m_tableList.getTable( position ) );
                MainActivity.this.startActivityForResult( intent, REQ_TABLE );
            }
        } );*/
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newGroupEdt = dialog.getDialog().findViewById( R.id.edit_text );
        m_carte.addGroup( newGroupEdt.getText().toString() );
        m_carteAdapter.notifyDataSetChanged();
    }
}
