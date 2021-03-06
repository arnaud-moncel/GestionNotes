package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.epson.epos2.printer.Printer;
import com.note.gestion.carte.CarteAdapter;
import com.note.gestion.carte.Dish;
import com.note.gestion.carte.DishList;
import com.note.gestion.carte.Group;
import com.note.gestion.carte.GroupList;
import com.note.gestion.table.Table;
import com.note.gestion.table.TableDish;
import com.note.gestion.table.TableDishList;
import com.note.gestion.table.TableDishListAdapter;
import com.note.gestion.vat.VatList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TableActivity extends AppCompatActivity implements SimpleDoubleAddDialog.NoticeDialogListener {
    private AppDatabase m_dataBase;

    private Table m_table;
    private TableDishList m_tableDishList;

    private TableDishListAdapter m_tableDishListAdapter;

    private VatList m_vatList;

    private Group m_currentGroup;
    private GroupList m_groupList;
    private DishList m_dishList;
    private Deque<Group> m_previousGroups = new ArrayDeque<>();

    private CarteAdapter m_carteAdapter;
    private GridView m_gridView;

    private Dish m_callDish;

    private static final String TABLE_CALL_DISH = "tableCallDish";

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

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_table = m_dataBase.tableDAO().getById( getIntent().getIntExtra( MainActivity.TABLE, 1 ) );

                m_vatList = new VatList( m_dataBase.vatDAO().getAll() );
                m_currentGroup = m_dataBase.groupDAO().getById( 1 );
                m_currentGroup.setVat( m_vatList.getVat( m_currentGroup.getVatId() - 1 ) );

                List<Dish> dishList = m_dataBase.dishDAO().getAll();
                for( Dish dish : dishList ) {
                    dish.setVat( m_vatList.getVat( dish.getVatId() -1 ) );
                }

                m_tableDishList = new TableDishList( m_table.getId(), m_dataBase.tableDishDAO().getAllByTableId( m_table.getId() ) );
                for( TableDish tableDish : m_tableDishList.getTableDishes() ) {
                    tableDish.setDish( dishList.get( tableDish.getDishId() - 1 ) );
                }

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
                            Dish dish = m_dishList.getDish( position - m_groupList.getGroups().size() );
                            if( dish.getPrice() != null ) {
                                final int tableDishId = m_tableDishList.createTableDish( dish, null, null );
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... voids) {
                                        if( tableDishId >= 0 ) {
                                            m_dataBase.tableDishDAO().update( m_tableDishList.getTableDish( tableDishId ) );
                                        } else {
                                            m_dataBase.tableDishDAO().insert( m_tableDishList.getLastTableDish() );
                                            m_tableDishList.getLastTableDish().setId( m_dataBase.tableDishDAO().getLastId() );
                                        }
                                        return null;
                                    }
                                }.execute();

                                m_tableDishListAdapter.notifyDataSetChanged();
                            } else {
                                m_callDish = dish;
                                DialogFragment addCallDishDialog = SimpleDoubleAddDialog.newInstance( R.string.add_dish_dialog_title, R.string.add_dish_price_dialog_message, R.string.add_dish_qty_message );
                                addCallDishDialog.show( getFragmentManager(), TABLE_CALL_DISH );
                            }
                        }
                    }
                } );

                m_tableDishListAdapter = new TableDishListAdapter(TableActivity.this,
                        R.layout.item_list_table_dish, m_tableDishList.getTableDishes() );

                //ListView pour les tables
                ListView listView = findViewById( R.id.dish_list );
                listView.setAdapter( m_tableDishListAdapter );
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                        new AsyncTask<Void, Void, Integer>() {
                            @Override
                            protected Integer doInBackground(Void... voids) {
                                m_dataBase.tableDishDAO().delete(m_tableDishList.getTableDish(i));
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Integer result) {
                                m_tableDishList.deleteTableDish( i );
                                m_tableDishListAdapter.notifyDataSetChanged();
                            }
                        }.execute();

                        return false;
                    }
                });
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.activity_table, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_print:
                new PrintAdapter().doPrint( getApplicationContext(), m_table.getId(), m_tableDishList.getTableDishes() );
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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if( dialog.getTag().equals( TABLE_CALL_DISH ) ) {
            EditText newTableDishEdtd = dialog.getDialog().findViewById( R.id.edit_price );
            Double price = Double.valueOf(newTableDishEdtd.getText().toString());

            EditText newTableDishEdtq = dialog.getDialog().findViewById( R.id.edit_qty );
            Integer qty = Integer.valueOf(newTableDishEdtq.getText().toString());

            m_tableDishList.createTableDish( m_callDish, price, qty );

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {

                    m_dataBase.tableDishDAO().insert( m_tableDishList.getLastTableDish() );
                    m_tableDishList.getLastTableDish().setId( m_dataBase.tableDishDAO().getLastId() );

                    return null;
                }

                @Override
                protected void onPostExecute(Integer result) {
                    m_callDish = null;
                }
            }.execute();

            m_tableDishListAdapter.notifyDataSetChanged();
        }
    }
}
