package com.note.gestion.gestionnotes;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatList;
import com.note.gestion.vat.VatListAdapter;

public class VatActivity extends AppCompatActivity
        implements DoubleAddDialog.NoticeDialogListener {
    private AppDatabase m_dataBase;

    private VatList m_vatList;
    private VatListAdapter m_vatListAdpter;

    private static final String VAT = "com.note.gestion.VAT";

    private int m_editedVatId = -1;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_vat );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        m_dataBase = AppDatabase.getInstance( getApplicationContext() );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                DialogFragment addTableDialog = DoubleAddDialog.newInstance(
                        R.string.add_vat_dialog_title,
                        R.string.add_vat_dialog_message,
                        R.string.add_vat_dialog_percent
                );
                addTableDialog.show( getFragmentManager(), VAT );
            }
        } );

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                m_vatList = new VatList( m_dataBase.vatDAO().getAll() );
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                m_vatListAdpter = new VatListAdapter(VatActivity.this,
                        R.layout.item_list_vat, m_vatList.getVats() );

                //ListView pour les tables
                ListView listView = findViewById( R.id.table_list );
                listView.setAdapter( m_vatListAdpter );
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
                        Vat vat = m_vatList.getVat( position );
                        m_editedVatId = position;
                        DialogFragment addTableDialog = DoubleAddDialog.newInstance(
                                R.string.edit_vat_dialog_title,
                                R.string.add_vat_dialog_message,
                                R.string.add_vat_dialog_percent,
                                vat.getDesignation(), vat.getPercent()
                        );
                        addTableDialog.show( getFragmentManager(), VAT );
                    }
                } );
            }
        }.execute();
    }

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newVatEdt = dialog.getDialog().findViewById( R.id.edit_text );
        final String des = newVatEdt.getText().toString();

        EditText newVatEdtc = dialog.getDialog().findViewById( R.id.edit_decimal );
        final Double percent = Double.valueOf(newVatEdtc.getText().toString());

        if( m_editedVatId > -1 ) {
            m_vatList.editVat( m_editedVatId, des, percent );
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    m_dataBase.vatDAO().update( m_vatList.getVat( m_editedVatId ) );
                    return null;
                }
                @Override
                protected void onPostExecute(Integer result) {
                    m_editedVatId = -1;
                }
            }.execute();
        } else {
            m_vatList.createVat( des, percent);
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... voids) {
                    m_dataBase.vatDAO().insert( m_vatList.getLasVat() );
                    return null;
                }
            }.execute();
        }

        m_vatListAdpter.notifyDataSetChanged();
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
