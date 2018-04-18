package com.note.gestion.carte;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Dao
public interface DishDAO {
    @Query( "Select * from `Dish` where group_id=:groupId" )
    List<Dish> getAllByGroup( int groupId );

    @Insert
    void insert( Dish dish );
}
