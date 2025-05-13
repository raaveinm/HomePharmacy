package com.raaveinm.homepharmacy.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE) void insert (Item item);
    @Delete void delete (Item item);
    @Query ("SELECT * FROM pharmacy_items") LiveData <List<Item>> getAllItems();
    @Update void update (Item item);
}
