package it.unimib.ciaet.database;



import static it.unimib.ciaet.util.Constants.DATABASE_NAME;
import static it.unimib.ciaet.util.Constants.DATABASE_VERSION;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.ciaet.model.CriptoCurrency;

/**
 * Main access point for the underlying connection to the local database.
 * https://developer.android.com/reference/kotlin/androidx/room/Database
 */

@Database(entities = {CriptoCurrency.class}, version = DATABASE_VERSION)

public abstract class CiaetRoomDatabase extends RoomDatabase {

    public abstract CiaetDao newsDao();

    private static volatile CiaetRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CiaetRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CiaetRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CiaetRoomDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
