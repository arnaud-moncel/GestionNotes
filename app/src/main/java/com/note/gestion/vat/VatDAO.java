package com.note.gestion.vat;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Dao
public interface VatDAO {
    @Query( "Select * from Vat" )
    List<Vat> getAll();

    @Insert
    void insert( Vat vat );

    @Update
    void update( Vat vat );
}
