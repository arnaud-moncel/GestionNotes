package com.note.gestion.vat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

public class VatList implements Serializable {
    private List<Vat> m_vats = new ArrayList<>();

    public void createVat( String des, double percent ) { m_vats.add( new Vat( des, percent ) ); }

    public List<Vat> getVats() { return m_vats; }

    public Vat getVat( int position ) { return m_vats.get( position ); }

    public void editVat( int position, String des, double percent ) { m_vats.get( position ).editVat( des, percent ); }
}
