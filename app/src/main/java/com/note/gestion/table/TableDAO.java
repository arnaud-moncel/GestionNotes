package com.note.gestion.table;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Arnaud Moncel on 14/04/2018.
 */

@Dao
public interface TableDAO {
    @Query( "Select * from `Table`" )
    List<Table> getAll();

    @Query( "Select * from `Table` where id=:id" )
    Table getById( int id );

    @Query( "Select max( id ) from `Table`" )
    int getLastId();

    @Insert
    void insert( Table table );

    @Delete
    void delete( Table table );
}
