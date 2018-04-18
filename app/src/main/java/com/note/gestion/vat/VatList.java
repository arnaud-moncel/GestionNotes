package com.note.gestion.vat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

public class VatList {
    private List<Vat> m_vats;

    public VatList( List<Vat> vats ) { m_vats = vats; }

    public void createVat( String des, Double percent ) { m_vats.add( new Vat( des, percent ) ); }

    public List<Vat> getVats() { return m_vats; }
    public Vat getVat( int position ) { return m_vats.get( position ); }
    public Vat getLasVat() { return m_vats.get( m_vats.size() - 1 ); }

    public void editVat( int position, String des, Double percent ) { m_vats.get( position ).editVat( des, percent ); }

    public ArrayList<String> toStringArray() {
        ArrayList<String> strings = new ArrayList<>();
        for( Vat vat : m_vats ) {
            strings.add( vat.getDesignation() );
        }
        return strings;
    }
}
