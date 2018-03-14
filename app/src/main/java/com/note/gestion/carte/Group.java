package com.note.gestion.carte;

import com.note.gestion.vat.Vat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class Group extends Item implements Serializable{
    private List<Item> m_items = new ArrayList<>();

    public Group(String designation, Vat vat) { super( designation, vat ); }

    public void addGroup( String designation, Vat vat ) { m_items.add( new Group( designation, vat ) ); }
    public void addDish( String designation, Double price ) { m_items.add( new Dish( designation, price, m_vat ) ); }

    public List<Item> getItems() { return m_items; }
    public Item getItem( int position ) {
        return m_items.get( position );
    }
}
