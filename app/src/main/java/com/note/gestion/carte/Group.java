package com.note.gestion.carte;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.note.gestion.vat.Vat;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

// TODO: 05/04/2018 Revoir entierement l'entité group Ajouter une notion de parenté au group
// Ajouter un Group avec comme parent le group current
// Quand on liste on va recuperer les group du group courant
// Ajouter une class d'association des groups et des dish pour permettre l'affichage ( avoir si on ne modifie pas juste le carte Adapter avec juste deux tableau )

@Entity
@Getter
@Setter
public class Group extends Item {
    /*@PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;*/

    @Relation( entity = Dish.class, parentColumn = "item_id", entityColumn = "item_id" )
    private List<Dish> m_dishes;

    //@Relation( entity = Group.class, parentColumn = "item_id", entityColumn = "item_id" )
    @Ignore
    private List<Group> m_groups;

    @Ignore
    private List<Item> m_items;

    public Group() {}
    public Group(String designation, Vat vat) {
        super( designation, vat );
        m_dishes = new ArrayList<>();
        m_groups = new ArrayList<>();
        m_items = new ArrayList<>();
    }

    public void addGroup( String designation, Vat vat ) { m_groups.add( new Group( designation, vat ) ); }
    public void addDish( String designation, Double price ) { m_dishes.add( new Dish( designation, price, m_vat ) ); }

    public List<Item> getItems() {
        m_items.clear();
        m_items.addAll( m_groups );
        m_items.addAll( m_dishes );
        return m_items;
    }
    public Item getItem( int position ) {
        if( m_groups.size() > position ) {
            return m_groups.get( position );
        } else {
            return m_dishes.get( position );
        }
    }

    public List<Group> getGroups() { return m_groups; }
    public List<Dish> getDishes() { return m_dishes; }

    public void setGroups( List<Group> groups ) { m_groups = groups; }
    public void setDishes( List<Dish> dishes ) { m_dishes = dishes; }
}
