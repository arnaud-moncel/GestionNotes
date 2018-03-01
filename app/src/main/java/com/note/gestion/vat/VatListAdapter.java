package com.note.gestion.vat;

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

public class VatListAdapter extends ArrayAdapter<Vat> {
    private List<Vat> m_vats;
    Context m_context;
    int m_layoutResourceId;

    public VatListAdapter( Context context, int resourceId, List<Vat> tables ) {
        super( context, resourceId, tables );
        m_vats = tables;
        m_context = context;
        m_layoutResourceId = resourceId;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        if ( convertView == null ) {
            convertView = ( ( Activity ) m_context ).getLayoutInflater().inflate( m_layoutResourceId, parent, false );
        }

        // TODO: 01/03/2018 faire en sorte de pouvoir modiifier le pourcentage des taux de TVA 
        
        Vat vat = m_vats.get( position );
        ( ( TextView ) convertView.findViewById( R.id.item_vat_text_view ) ).setText( vat.getDesignation() );
        ( ( TextView ) convertView.findViewById( R.id.item_vat_edt ) ).setText( String.valueOf( vat.getPercent() ) );

        return convertView;
    }
}
