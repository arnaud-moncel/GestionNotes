package com.note.gestion.gestionnotes;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.note.gestion.carte.Dish;
import com.note.gestion.carte.DishDAO;
import com.note.gestion.carte.Group;
import com.note.gestion.carte.GroupDAO;
import com.note.gestion.table.Table;
import com.note.gestion.table.TableDAO;
import com.note.gestion.table.TableDish;
import com.note.gestion.table.TableDishDAO;
import com.note.gestion.vat.Vat;
import com.note.gestion.vat.VatDAO;

import java.util.concurrent.Executors;

/**
 * Created by Arnaud Moncel on 21/03/2018.
 */

@Database( entities = { Vat.class, Group.class, Dish.class, Table.class, TableDish.class }, version = 1 )
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract VatDAO vatDAO();
    public abstract DishDAO dishDAO();
    public abstract GroupDAO groupDAO();
    public abstract TableDAO tableDAO();
    public abstract TableDishDAO tableDishDAO();

    public synchronized static AppDatabase getInstance( Context context ) {
        if ( INSTANCE == null ) {
            INSTANCE = buildDatabase( context );
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase( final Context context ) {
        return Room.databaseBuilder( context,
            AppDatabase.class,
            "gestion-note" )
            .addCallback( new Callback() {
                @Override
                public void onCreate( @NonNull SupportSQLiteDatabase db ) {
                    super.onCreate( db );
                    Executors.newSingleThreadScheduledExecutor().execute( new Runnable() {
                        @Override
                        public void run() {
                            getInstance( context ).vatDAO().insert( new Vat( "Global", 20.0 ) );
                            getInstance( context ).groupDAO().insert( new Group( "Global", 1 ) );
                        }
                    } );
                }
            } )
            .build();
    }
}
