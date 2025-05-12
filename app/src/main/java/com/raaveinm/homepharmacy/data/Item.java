package com.raaveinm.homepharmacy.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pharmacy_items")
public class Item {
    @PrimaryKey (autoGenerate = true) public int id;
    @ColumnInfo (name = "name") public String itemName;
    @ColumnInfo (name = "company") public String companyName;
    @ColumnInfo (name = "quantity") public int itemQuantity;
    @ColumnInfo (name = "price") public int itemPrice;
}
