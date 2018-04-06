package com.note.gestion.carte;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.note.gestion.vat.Vat;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

// TODO: 05/04/2018 Revoir entièrement l'entité Dish en ajoutant une notion de parenté au group
// Quand on liste on va recuperer les dish du group courant
// Ajouter une class d'association des groups et des dish pour permettre l'affichage ( avoir si on ne modifie pas juste le carte Adapter avec juste deux tableau )

@Entity
@Getter
@Setter
public class Dish {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;

    @ColumnInfo( name = "designation" )
    private String m_designation;

    @Ignore
    private Vat m_vat;

    @ColumnInfo( name = "price" )
    private Double m_price;

    @ColumnInfo( name = "group_id" )
    private int m_groupId;

    public Dish() {}
    public Dish( String designation, Double price, Vat vat, int groupId ) {
        m_designation = designation;
        m_price = price;
        m_vat = vat;
        m_groupId = groupId;
    }

    /*
    *   GETTER
    */
    public int getId() { return m_id; }
    public String getDesignation() { return m_designation; }
    public Vat getVat() { return m_vat; }
    public Double getPrice() { return m_price; }
    public int getGroupId() { return m_groupId; }

    /*
    *   SETTER
    */
    public void setId( int id ) { m_id = id; }
    public void setDesignation( String designation ) { m_designation = designation; }
    public void setVat( Vat vat ) { m_vat = vat; }
    public void setPrice( Double price ) { m_price = price; }
    public void setGroupId( int groupId ) { m_groupId = groupId; }
}
