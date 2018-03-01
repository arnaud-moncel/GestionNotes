package com.note.gestion.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

public class TableList implements Serializable {
    private List<Table> m_tables = new ArrayList<Table>() {
        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < this.size(); i++ ) {
                if ( ((Table) o).getId().equals( this.get(i).getId() ) ) {
                    return i;
                }
            }
            return -1;
        }
    };

    public void createTable( String id ) {
        m_tables.add( new Table( id ) );
    }

    public List<Table> getTables() { return m_tables; }
    public Table getTable( int position ) { return m_tables.get( position ); }
    public void seTable( Table table ) {
        int id = m_tables.indexOf( table );
        m_tables.set( id, table ); }
}