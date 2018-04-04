package com.note.gestion.gestionnotes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.note.gestion.carte.Dish;
import com.note.gestion.carte.DishDAO;
import com.note.gestion.carte.Group;
import com.note.gestion.table.Table;
import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatDAO;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Database( entities = { Vat.class, Dish.class, Group.class/*, Table.class*/}, version = 1 )
public abstract class AppDatabase extends RoomDatabase {

    public abstract VatDAO vatDAO();
    public abstract DishDAO dishDAO();
}
