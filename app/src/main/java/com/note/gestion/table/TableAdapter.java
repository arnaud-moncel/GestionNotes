package com.note.gestion.table;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.note.gestion.carte.Dish;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Arnaud Moncel on 19/03/2018.
 */

public class TableAdapter extends BaseAdapter {

    private Context m_context;
    private int m_layoutResourceId;

    private Map<Dish, Integer> m_dishes;

    public TableAdapter(Context context, int resourceId, Map<Dish, Integer> dishes ) {
        m_dishes = dishes;
        m_context = context;
        m_layoutResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return m_dishes.size();
    }

    @Override
    public Map.Entry<Dish, Integer> getItem( int position ) {
        ArrayList<Map.Entry<Dish, Integer>> ar = new ArrayList<>();
        ar.addAll( m_dishes.entrySet() );
        return ar.get( position );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( (Activity) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        Map.Entry<Dish, Integer> item = getItem( position );
        ( ( TextView ) convertView.findViewById(android.R.id.text1)).setText( item.getKey().getDesignation() );
        ( ( TextView ) convertView.findViewById(android.R.id.text2)).setText( String.valueOf( item.getValue() ) );

        return convertView;
    }
}
