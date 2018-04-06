package com.note.gestion.carte;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.note.gestion.vat.Vat;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

// TODO: 05/04/2018 Revoir entierement l'entité group Ajouter une notion de parenté au group
// Ajouter un Group avec comme parent le group current
// Quand on liste on va recuperer les group du group courant
// Ajouter une class d'association des groups et des dish pour permettre l'affichage ( avoir si on ne modifie pas juste le carte Adapter avec juste deux tableau )

@Entity
@Getter
@Setter
public class Group {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;

    @ColumnInfo( name = "designation" )
    private String m_designation;

    @ColumnInfo( name = "vat_id" )
    private int m_vatId;

    @Ignore
    private Vat m_vat;

    @ColumnInfo( name = "parent_id" )
    private int m_parentGroupId;

    public Group() {}
    public Group( String designation ) {
        m_designation = designation;
    }
    public Group( String designation, int vatId, int parentGroupId ) {
        m_designation = designation;
        m_vatId = vatId;
        m_parentGroupId = parentGroupId;
    }
    public Group( String designation, Vat vat, int parentGroupId ) {
        m_designation = designation;
        m_vatId = vat.getId();
        m_vat = vat;
        m_parentGroupId = parentGroupId;
    }

    /*
    *   GETTER
    */
    public int getId() { return m_id; }
    public String getDesignation() { return m_designation; }
    public int getVatId() { return m_vatId; }
    public Vat getVat() { return m_vat; }
    public int getParentGroupId() { return m_parentGroupId; }

    /*
    *   SETTER
    */
    public void setId( int id ) { m_id = id; }
    public void setDesignation( String designation ) { m_designation = designation; }
    public void setVatId( int vatId ) { m_vatId = vatId; }
    public void setVat( Vat vat ) { m_vat = vat; }
    public void setParentGroupId( int parentGroupId ) { m_parentGroupId = parentGroupId; }
}
