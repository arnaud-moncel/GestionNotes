package com.note.gestion.table;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Arnaud Moncel on 14/04/2018.
 */

@Dao
public interface TableDishDAO {
    @Query( "Select * from Tabledish where table_id=:tableId" )
    List<TableDish> getAllByTableId( int tableId );

    @Query( "Select max( id ) from Tabledish" )
    int getLastId();

    @Insert
    void insert( TableDish tableDish );

    @Update()
    void update( TableDish tableDish );
}
