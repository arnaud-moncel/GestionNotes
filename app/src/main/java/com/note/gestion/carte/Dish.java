package com.note.gestion.carte;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.note.gestion.vat.Vat;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

// TODO: 05/04/2018 Revoir entièrement l'entité Dish en ajoutant une notion de parenté au group
// Ajouter un Dish avec comme parent le group current
// Quand on liste on va recuperer les dish du group courant
// Ajouter une class d'association des groups et des dish pour permettre l'affichage ( avoir si on ne modifie pas juste le carte Adapter avec juste deux tableau )

@Entity
@Getter
@Setter
public class Dish extends Item {
    @ColumnInfo( name = "price" )
    private Double m_price;

    public Dish() {}

    public Dish(String designation, Double price, Vat vat) { super( designation, vat ); m_price = price; }

    public int getId() { return m_id; }
    public Double getPrice() { return m_price; }

    public void setid( int id ) { m_id = id; }
    public void setPrice( Double price ) { m_price = price; }
}
