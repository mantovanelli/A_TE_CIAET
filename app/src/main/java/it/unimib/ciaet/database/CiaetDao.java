package it.unimib.ciaet.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.ciaet.model.CriptoCurrency;

/**
 * Data Access Object (DAO) that provides methods that can be used to query,
 * update, insert, and delete data in the database.
 * https://developer.android.com/training/data-storage/room/accessing-data
 */
@Dao
public interface CiaetDao {

    @Query("SELECT * FROM criptocurrency")
    List<CriptoCurrency> getAll();

    @Query("SELECT * FROM criptocurrency WHERE identificativo = :identificativo")
    CriptoCurrency getNews(long identificativo);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertNewsList(List<CriptoCurrency> newsList);

    @Insert
    void insertAll(CriptoCurrency... news);

    @Delete
    void delete(CriptoCurrency news);

    @Query("DELETE FROM criptocurrency")
    void deleteAll();


    @Delete
    void deleteAllWithoutQuery(CriptoCurrency... news);

    @Update
    void updateSingleFavoriteNews(CriptoCurrency news);

    @Update
    void updateListFavoriteNews(List<CriptoCurrency> news);
}
