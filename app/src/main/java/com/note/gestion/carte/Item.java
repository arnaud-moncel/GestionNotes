package com.note.gestion.carte;

import com.note.gestion.vat.Vat;

import java.io.Serializable;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

abstract class Item implements Serializable{
    protected String m_designation;
    protected Vat m_vat;

    protected Item( String designation, Vat vat ) { m_designation = designation; m_vat = vat; }

    public String getDesignation() { return m_designation; }
    public Vat getVat() { return m_vat; }
}
