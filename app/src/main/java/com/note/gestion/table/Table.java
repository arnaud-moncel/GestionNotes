package com.note.gestion.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.note.gestion.carte.Dish;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

@Entity
@Getter
@Setter
public class Table implements Serializable {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private String m_id;

    @Embedded
    private Map<Dish, Integer> m_dishes;

    public Table() {}

    public Table( String num ) {
        m_id = num;
        m_dishes = new HashMap<>();
    }

    public String getId() { return m_id; }
    public Map<Dish, Integer> getDishes() { return m_dishes; }

    public void setId( String id ) { m_id = id; }
    public void setDishes( Map<Dish, Integer> dishes ) { m_dishes = dishes; }

    public void addDish( Dish dish ) {
        Integer dishCount = m_dishes.get( dish );
        if( dishCount == null ) {
            m_dishes.put( dish, 1 );
        } else {
            m_dishes.put( dish, dishCount + 1);
        }
    }
}
