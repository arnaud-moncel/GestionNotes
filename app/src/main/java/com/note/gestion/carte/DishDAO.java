package com.note.gestion.carte;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Dao
public interface DishDAO {
    @Insert
    void insertAll( List<Dish> dishes );
}
