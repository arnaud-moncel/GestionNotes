package com.note.gestion.carte;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class CarteAdapter extends ArrayAdapter<Item> {
    private List<Item> m_items;
    private Context m_context;
    private int m_layoutResourceId;

    public CarteAdapter( Context context, int resourceId,  List<Item> items ) {
        super( context, resourceId, items );
        m_items = items;
        m_context = context;
        m_layoutResourceId = resourceId;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( ( Activity ) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        Item item = m_items.get( position );
        ( (TextView) convertView.findViewById( android.R.id.text1 ) ).setText( item.getDesignation() );

        return convertView;
    }
}
