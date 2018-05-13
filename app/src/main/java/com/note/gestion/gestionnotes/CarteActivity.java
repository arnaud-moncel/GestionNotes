package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.os.AsyncTask;
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
import com.note.gestion.carte.Dish;
import com.note.gestion.carte.DishList;
import com.note.gestion.carte.Group;
import com.note.gestion.carte.GroupList;
import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatList;

import java.util.ArrayDeque;
import java.util.Deque;

public class CarteActivity extends AppCompatActivity
        implements SpinnerAddDialog.NoticeDialogListener,
        DoubleAddDialog.NoticeDialogListener,
        SimpleAddDialog.NoticeDialogListener {
    private AppDatabase m_dataBase;

    private VatList m_vatList;

    private Group m_currentGroup;
    private GroupList m_groupList;
    private DishList m_dishList;
    private Deque<Group> m_previousGroups = new ArrayDeque<>();

    private int m_editedDishId = -1;

    private CarteAdapter m_carteAdapter;
    private GridView m_gridView;

    private Boolean m_isFabOpen = false;

    private FloatingActionButton m_fab;
    private FloatingActionButton m_fabAddItem;
    private FloatingActionButton m_fabAddGroup;
    private FloatingActionButton m_fabAddCallItem;

    private Animation m_animClose;
    private Animation m_animOpen;
    private Animation m_animRotBack;
    private Animation m_animRotFor;

    private static final String CARTE_GROUP = "com.note.gestion.CARTE.GROUP";
    private static final String CARTE_DISH = "com.note.gestion.CARTE.DISH";
    private static final String CARTE_CALL_DISH = "com.note.gestion.CARTE_CALL_DISH";

    private void animateflocationBtn(){
        if( m_isFabOpen ){
            m_fab.startAnimation( m_animRotBack );
            m_fabAddGroup.startAnimation( m_animClose );
            m_fabAddItem.startAnimation( m_animClose );
            m_fabAddCallItem.startAnimation( m_animClose );
            m_fabAddGroup.setClickable( false );
            m_fabAddItem.setClickable( false );
            m_fabAddCallItem.setClickable( false );
            m_isFabOpen = false;
        } else {
            m_fab.startAnimation( m_animRotFor );
            m_fabAddGroup.startAnimation( m_animOpen );
            m_fabAddItem.startAnimation( m_animOpen );
            m_fabAddCallItem.startAnimation( m_animOpen );
            m_fabAddGroup.setClickable( true );
            m_fabAddItem.setClickable( true );
            m_fabAddCallItem.setClickable( true );
            m_isFabOpen = true;
        }
    }

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
                m_carteAdapter = new CarteAdapter( CarteActivity.this, R.layout.grid_view_item_carte, m_groupList.getGroups(), m_dishList.getDishes() );
                m_gridView.setAdapter( m_carteAdapter );
            }
        }.execute();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_carte );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        m_dataBase = AppDatabase.getInstance( getApplicationContext() );

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_vatList = new VatList( m_dataBase.vatDAO().getAll() );
                m_currentGroup = m_dataBase.groupDAO().getById( 1 );
                m_currentGroup.setVat( m_vatList.getVat( m_currentGroup.getVatId() - 1 ) );

                getCurrentLists();
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                m_carteAdapter = new CarteAdapter(CarteActivity.this,
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
                        } else {
                            m_editedDishId = position - m_groupList.getGroups().size();
                            Dish dish = m_dishList.getDish( m_editedDishId );
                            if( dish.getPrice() != null ) {
                                DialogFragment addDishDialog = DoubleAddDialog.newInstance(
                                        R.string.edit_dish_dialog_title,
                                        R.string.add_dish_dialog_message,
                                        R.string.add_dish_price_dialog_message,
                                        dish.getDesignation(), dish.getPrice()
                                );
                                addDishDialog.show( getFragmentManager(), CARTE_DISH );
                            } else {
                                DialogFragment addCallDishDialog = SimpleAddDialog.newInstance(
                                        R.string.edit_dish_dialog_title,
                                        R.string.add_dish_dialog_message,
                                        dish.getDesignation()
                                );
                                addCallDishDialog.show( getFragmentManager(), CARTE_CALL_DISH );
                            }
                        }
                    }
                } );
            }
        }.execute();

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

        m_fabAddCallItem = findViewById( R.id.fab_add_call_item );
        m_fabAddCallItem.setOnClickListener( new  View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                animateflocationBtn();
                DialogFragment addCallDishDialog = SimpleAddDialog.newInstance( R.string.add_dish_dialog_title, R.string.add_dish_dialog_message );
                addCallDishDialog.show( getFragmentManager(), CARTE_CALL_DISH );
            }
        } );

        m_animOpen = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fab_open );
        m_animClose = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fab_close );
        m_animRotFor = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_forward );
        m_animRotBack = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.rotate_backward );
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newEdt = dialog.getDialog().findViewById( R.id.edit_text );
        String des = newEdt.getText().toString();

        if( dialog.getTag().equals( CARTE_GROUP ) ) {
            Spinner newSpinner = dialog.getDialog().findViewById( R.id.vat_spinner );

            Vat vat = m_vatList.getVat( newSpinner.getSelectedItemPosition() );
            m_groupList.createGroup( des, vat );
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    m_dataBase.groupDAO().insert( m_groupList.getLastGroup() );
                    m_groupList.getLastGroup().setId( m_dataBase.groupDAO().getLastId() );
                    return null;
                }
            }.execute();
        } else if( dialog.getTag().equals( CARTE_DISH ) ) {
            EditText newEdtc = dialog.getDialog().findViewById( R.id.edit_decimal );
            Double price = Double.valueOf( newEdtc.getText().toString() );
            if( m_editedDishId > -1 ) {
                m_dishList.editDish( m_editedDishId, des, price );
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... voids) {
                        m_dataBase.dishDAO().update( m_dishList.getDish( m_editedDishId ) );
                        return null;
                    }

                    protected void onPostExecute(Integer result) {
                        m_editedDishId = -1;
                    }
                }.execute();
            } else {
                m_dishList.createDish( des, price );
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... voids) {
                        m_dataBase.dishDAO().insert( m_dishList.getLastDish() );
                        return null;
                    }
                }.execute();
            }
        } else if( dialog.getTag().equals( CARTE_CALL_DISH ) ) {
            if( m_editedDishId > -1 ) {
                m_dishList.editCallDish( m_editedDishId, des );
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... voids) {
                        m_dataBase.dishDAO().update( m_dishList.getDish( m_editedDishId ) );
                        return null;
                    }

                    protected void onPostExecute(Integer result) {
                        m_editedDishId = -1;
                    }
                }.execute();
            } else {
                m_dishList.createCallDish( des );
                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... voids) {
                        m_dataBase.dishDAO().insert( m_dishList.getLastDish() );
                        return null;
                    }
                }.execute();
            }
        }

        m_carteAdapter.changeData();
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
}
