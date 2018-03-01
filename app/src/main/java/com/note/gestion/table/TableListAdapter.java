package com.note.gestion.table;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.note.gestion.gestionnotes.R;

import java.util.List;

/**
 * Created by MSI on 20/02/2018.
 */

public class TableListAdapter extends ArrayAdapter<Table> {
    private List<Table> m_tables;
    Context m_context;
    int m_layoutResourceId;

    public TableListAdapter( Context context, int resourceId,  List<Table> tables ) {
        super( context, resourceId, tables );
        m_tables = tables;
        m_context = context;
        m_layoutResourceId = resourceId;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( ( Activity ) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        ( ( TextView ) convertView.findViewById(android.R.id.text1)).setText( m_context.getString( R.string.table_item ) );
        ( ( TextView ) convertView.findViewById(android.R.id.text2)).setText( m_tables.get( position ).getId() );

        return convertView;
    }
}
