package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.SHOPPING_CART_TABLE,
        foreignKeys = {
            @ForeignKey(entity = User.class,
                parentColumns = "mUserId",
                childColumns = "mUserId",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Theater.class,
                parentColumns = "mTheaterId",
                childColumns = "mTheaterId",
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = {"mUserId"})})
public class ShoppingCart {
}
