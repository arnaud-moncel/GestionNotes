package com.note.gestion.table;

import com.note.gestion.carte.Dish;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

public class Table implements Serializable {
    private String m_id;
    private Map<Dish, Integer> m_dishes;

    public Table( String num ) {
        m_id = num;
        m_dishes = new HashMap<>();
    }

    public String getId() { return m_id; }
    public Map<Dish, Integer> getDishes() { return m_dishes; }

    public void addDish( Dish dish ) {
        Integer dishCount = m_dishes.get( dish );
        if( dishCount == null ) {
            m_dishes.put( dish, 1 );
        } else {
            m_dishes.put( dish, dishCount + 1);
        }
    }
}
