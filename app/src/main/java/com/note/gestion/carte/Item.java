package com.note.gestion.carte;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.note.gestion.vat.Vat;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

// TODO: 05/04/2018 Supprimer cette entité en la mergeant dans dish et group

@Entity
abstract class Item {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "item_id" )
    protected int m_id;

    @ColumnInfo( name = "item_designation" )
    protected String m_designation;

    @Embedded
    protected Vat m_vat;

    protected Item() {}
    protected Item( String designation, Vat vat ) { m_designation = designation; m_vat = vat; }

    public String getDesignation() { return m_designation; }
    public Vat getVat() { return m_vat; }
}
