package com.note.gestion.table;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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

        ( ( TextView ) convertView.findViewById(R.id.item_dish_des)).setText( m_tableDishes.get( position ).getDish().getDesignation() );
        ( ( TextView ) convertView.findViewById(R.id.item_dish_qty)).setText( String.valueOf( m_tableDishes.get( position ).getQty() ) );
        if( m_tableDishes.get( position ).getDish().getPrice() != null ) {
            ( ( TextView ) convertView.findViewById(R.id.item_dish_price)).setText( String.valueOf( m_tableDishes.get( position ).getDish().getPrice() ) );
        } else {
            ( ( TextView ) convertView.findViewById(R.id.item_dish_price)).setText( String.valueOf( m_tableDishes.get( position ).getPrice() ) );
        }

        return convertView;
    }
}
