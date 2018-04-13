package com.note.gestion.table;

import java.util.List;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

public class TableList {
    private List<Table> m_tables;

    public TableList( List<Table> tables ) { m_tables = tables; }

    public void createTable( String id ) {
        m_tables.add( new Table( id ) );
    }

    public List<Table> getTables() { return m_tables; }
    public Table getTable( int position ) { return m_tables.get( position ); }
}