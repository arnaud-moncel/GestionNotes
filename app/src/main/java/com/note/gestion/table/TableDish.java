package com.note.gestion.table;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.note.gestion.carte.Dish;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Arnaud Moncel on 18/04/2018.
 */

@Entity
@Getter
@Setter
public class TableDish {
    @PrimaryKey( autoGenerate = true )
    @ColumnInfo( name = "id" )
    private int m_id;

    @ColumnInfo( name = "table_id" )
    private int m_tableId;

    @ColumnInfo( name = "dish_id" )
    private int m_dishId;

    @ColumnInfo( name = "qty" )
    private Integer m_qty;

    @ColumnInfo( name = "price" )
    private Double m_price;

    @Ignore
    private Dish m_dish;

    public TableDish() {}
    public TableDish( int tableId, int dishId, int qty ) {
        m_tableId = tableId;
        m_dishId = dishId;
        m_qty = qty;
    }
    public TableDish( int tableId, Dish dish, int qty ) {
        m_tableId = tableId;
        m_dishId = dish.getId();
        m_qty = qty;
        m_dish = dish;
    }
    public TableDish ( int tableId, Dish dish, Double price, int qty ) {
        m_tableId = tableId;
        m_dishId = dish.getId();
        m_qty = qty;
        m_dish = dish;
        m_price = price;
    }

    /**
     *  GETTER
     */
    public int getId() { return m_id; }
    public int getTableId() { return m_tableId; }
    public int getDishId() { return m_dishId; }
    public Integer getQty() { return  m_qty; }
    public Dish getDish() { return m_dish; }
    public Double getPrice() { return m_price; }

    /**
     *  SETTER
     */
    public void setId( int id ) { m_id = id; }
    public void setTableId( int tableId ) { m_tableId = tableId; }
    public void setDishId( int dishId ) { m_dishId = dishId; }
    public void setQty( Integer qty ) { m_qty = qty; }
    public void setDish( Dish dish ) { m_dish = dish; }
    public void setPrice( Double price ) { m_price = price; }

    public void incrementQty() { m_qty++; }
}
