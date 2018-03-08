package com.note.gestion.carte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class Group extends Item {
    private List<Item> m_items = new ArrayList<>();

    public Group( String designation ) { super( designation ); }
}
