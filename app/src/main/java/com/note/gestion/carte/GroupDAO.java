package com.note.gestion.carte;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Dao
public interface GroupDAO {
    @Query( "Select * from `Group` where parent_id=:groupParentId" )
    List<Group> getAllByParentGroup( int groupParentId );

    @Query( "Select * from `Group` where id=:id" )
    Group getById( int id );

    @Query( "Select max( id ) from `Group`" )
    int getLastId();

    @Insert
    void insert( Group group );
}
