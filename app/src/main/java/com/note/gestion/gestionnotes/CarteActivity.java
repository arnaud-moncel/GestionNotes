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
import android.widget.Spinner;

import com.note.gestion.carte.CarteAdapter;
import com.note.gestion.carte.Group;
import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatList;


public class CarteActivity extends AppCompatActivity
        implements SpinnerAddDialog.NoticeDialogListener {

    private VatList m_vatList;
    private Group m_carte = new Group( "Carte", new Vat( "global", 0.0 ) );
    private CarteAdapter m_carteAdapter;

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
                DialogFragment addDishDialog = SimpleAddDialog.newInstance( R.string.add_dish_dialog_title, R.string.add_dish_dialog_message );
                addDishDialog.show( getFragmentManager(), CARTE_DISH );
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
        EditText newEdt = dialog.getDialog().findViewById( R.id.edit_text );

        if( dialog.getTag().equals( CARTE_GROUP ) ) {
            Spinner newSpinner = dialog.getDialog().findViewById( R.id.vat_spinner );

            Vat vat = m_vatList.getVat( newSpinner.getSelectedItemPosition() );
            m_carte.addGroup( newEdt.getText().toString(), vat );
        } else {
            m_carte.addDish( newEdt.getText().toString(), 15.0 );
        }

        m_carteAdapter.notifyDataSetChanged();
    }
}
