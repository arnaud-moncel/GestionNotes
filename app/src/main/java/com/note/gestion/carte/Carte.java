package com.note.gestion.carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class Carte {
    private List<Item> m_items = new ArrayList<>();

    public void addGroup( String designation ) { m_items.add( new Group( designation ) ); }

    public List<Item> getItems() { return m_items; }
}
