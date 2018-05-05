package com.note.gestion.table;

import com.note.gestion.carte.Dish;

import java.util.List;

/**
 * Created by Arnaud Moncel on 24/04/2018.
 */

public class TableDishList {
    private int m_tableId;
    private List<TableDish> m_tableDishes;

    public TableDishList( int tableId, List<TableDish> tableDishes ) {
        m_tableId = tableId;
        m_tableDishes = tableDishes;
    }

    public int createTableDish( Dish dish ) {
        int id = 0;
        while ( id < m_tableDishes.size() && m_tableDishes.get( id ).getDishId() != dish.getId() ) {
            id++;
        }

        if( id < m_tableDishes.size() ) {
            m_tableDishes.get( id ).incrementQty();
            return id;
        } else {
            m_tableDishes.add( new TableDish( m_tableId, dish, 1 ) );
            return - 1;
        }
    }

    public List<TableDish> getTableDishes() { return m_tableDishes; }
    public TableDish getTableDish( int position ) { return m_tableDishes.get( position ); }
    public TableDish getLastTableDish() { return m_tableDishes.get( m_tableDishes.size() - 1 ); }
    public void deleteTableDish( int position ) { m_tableDishes.remove( position ); }
}
