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
 * Created by Arnaud Moncel on 24/04/2018.
 */

public class TableDishListAdapter extends ArrayAdapter<TableDish> {
    private List<TableDish> m_tableDishes;
    private Context m_context;
    private int m_layoutResourceId;

    public TableDishListAdapter( Context context, int resourceId, List<TableDish> tableDishes ) {
        super( context, resourceId, tableDishes );
        m_tableDishes = tableDishes;
        m_context = context;
        m_layoutResourceId = resourceId;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( ( Activity ) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        ( ( TextView ) convertView.findViewById(android.R.id.text1)).setText( m_tableDishes.get( position ).getDish().getDesignation() );
        if( m_tableDishes.get( position ).getDish().getPrice() != null ) {
            ( ( TextView ) convertView.findViewById(android.R.id.text2)).setText( String.valueOf( m_tableDishes.get( position ).getQty() ) );
        } else {
            ( ( TextView ) convertView.findViewById(android.R.id.text2)).setText( String.valueOf( m_tableDishes.get( position ).getPrice() ) + " €" );
        }

        return convertView;
    }
}
