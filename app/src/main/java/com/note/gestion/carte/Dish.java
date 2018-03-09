package com.note.gestion.carte;

import com.note.gestion.vat.Vat;

/**
 * Created by Arnaud Moncel on 08/03/2018.
 */

public class Dish extends Item {

    private Double m_price;

    public Dish(String designation, Double price, Vat vat) { super( designation, vat ); m_price = price; }
}
