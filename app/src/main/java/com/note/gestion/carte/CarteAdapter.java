package com.note.gestion.carte;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.note.gestion.gestionnotes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class CarteAdapter extends ArrayAdapter<String> {
    private List<Group> m_groups;
    private List<Dish> m_dishes;
    private Context m_context;
    private int m_layoutResourceId;

    public CarteAdapter( Context context, int resourceId,  List<Group> groups, List<Dish> dishes ) {
        super( context, resourceId );
        m_groups = groups;
        m_dishes = dishes;

        transposeArray();

        m_context = context;
        m_layoutResourceId = resourceId;
    }

    private void transposeArray() {
        List<String> array = new ArrayList<>();
        for( Group group : m_groups ) {
            array.add( group.getDesignation() );
        }
        for( Dish dish : m_dishes ) {
            array.add( dish.getDesignation() );
        }

        this.addAll( array );
    }

    public void changeData() {
        this.clear();

        transposeArray();
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( ( Activity ) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        TextView txtView = convertView.findViewById( R.id.text_view );
        if( position < m_groups.size() ) {
            Group group = m_groups.get( position );
            txtView.setText( group.getDesignation() );

            convertView.findViewById( R.id.grid_item ).setBackgroundResource( R.drawable.group_background );
            ((ImageView) convertView.findViewById( R.id.img_view )).setImageResource( R.drawable.ic_group );
        } else {
            Dish dish = m_dishes.get( position - m_groups.size() );
            txtView.setText( dish.getDesignation() );

            if( dish.getPrice() != null ) {
                convertView.findViewById( R.id.grid_item ).setBackgroundResource( R.drawable.dish_background );
            } else {
                convertView.findViewById( R.id.grid_item ).setBackgroundResource( R.drawable.call_dish_background );
            }
            convertView.findViewById( R.id.img_view ).setVisibility( View.GONE );
        }

        return convertView;
    }
}
