package com.note.gestion.vat;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

@Entity
@Getter
@Setter
public class Vat {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;

    @ColumnInfo( name = "designation" )
    private String m_designation;

    @ColumnInfo( name = "percent" )
    private Double m_percent;

    public Vat() {}
    public Vat( String designation, Double percent ) {
        m_designation = designation;
        m_percent = percent;
    }

    /*
    * GETTER
    */
    public int getId() { return m_id; }
    public String getDesignation() { return m_designation; }
    public Double getPercent() { return m_percent; }

    /*
    * SETTER
    */
    public void setId( int id ) { m_id = id; }
    public void setDesignation( String designation ) { m_designation = designation; }
    public void setPercent( Double percent ) { m_percent = percent; }

    public void editVat( String des, Double percent ) {  m_designation = des; m_percent = percent; }
}
