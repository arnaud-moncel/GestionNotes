package com.note.gestion.carte;

import java.util.List;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

public class DishList {
    private Group m_group;

    private List<Dish> m_dishes;

    public DishList( Group group ) { m_group = group; }
    public DishList( Group group, List<Dish> dishes ) {
        m_group = group;
        m_dishes = dishes;
    }

    public void createDish( String designation, Double price ) { m_dishes.add( new Dish( designation, price, m_group.getVat(), m_group.getId() ) ); }

    public List<Dish> getDishes() { return m_dishes; }
    public Dish getDish( int position ) { return m_dishes.get( position ); }
    public Dish getLastDish() { return m_dishes.get( m_dishes.size() -1 ); }
}
