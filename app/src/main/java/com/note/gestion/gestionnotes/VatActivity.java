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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatList;
import com.note.gestion.vat.VatListAdapter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class VatActivity extends AppCompatActivity
        implements DoubleAddDialog.NoticeDialogListener {

    private VatList m_vatList;
    private VatListAdapter m_vatListAdpter;

    private static final String VAT = "vat";

    private int m_editedVatId = -1;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_vat );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

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

        m_vatList = (VatList) getIntent().getSerializableExtra( MainActivity.VAT );

        m_vatListAdpter = new VatListAdapter(this,
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

    @Override
    public void onDialogPositiveClick( DialogFragment dialog ) {
        EditText newVatEdt = dialog.getDialog().findViewById( R.id.edit_text );
        String des = newVatEdt.getText().toString();

        EditText newVatEdtc = dialog.getDialog().findViewById( R.id.edit_decimal );
        double percent = Double.valueOf(newVatEdtc.getText().toString());

        if( m_editedVatId > -1 ) {
            m_vatList.editVat( m_editedVatId, des, percent );
            m_editedVatId = -1;
        } else {
            m_vatList.createVat( des, percent);
        }

        m_vatListAdpter.notifyDataSetChanged();
    }

    private void sendIntent() {
        Intent intent = new Intent();
        intent.putExtra( MainActivity.VAT, m_vatList );
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
        sendIntent();
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            FileOutputStream fos = openFileOutput( MainActivity.VAT_FILE_NAME, Context.MODE_PRIVATE );
            ObjectOutputStream os = new ObjectOutputStream( fos );
            os.writeObject( m_vatList );
            os.close();
            fos.close();
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
