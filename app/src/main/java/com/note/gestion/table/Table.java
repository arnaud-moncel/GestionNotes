package com.note.gestion.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

@Entity
@Getter
@Setter
public class Table {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;

    @ColumnInfo( name = "designation" )
    private String m_designation;

    public Table() {}
    public Table( int id, String designation ) {
        m_id = id;
        m_designation = designation;
    }
    public Table( String designation ) {
        m_designation = designation;
    }

    /**
     *   GETTER
     */
    public int getId() { return m_id; }
    public String getDesignation() { return m_designation; }

    /**
     * SETTER
     */
    public void setId( int id ) { m_id = id; }
    public void setDesignation( String designation ) { m_designation = designation; }
}
