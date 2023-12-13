package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
        indices = {@Index(value = {"mUserId", "mTheaterId"})})
public class ShoppingCart {
  @PrimaryKey(autoGenerate = true)
  private long shoppingCartId;

  // Foreign keys
  private long mUserId;
  private long mTheaterId;

  public ShoppingCart(long userId, long theaterId) {
    mUserId = userId;
    mTheaterId = theaterId;
  }

  public long getShoppingCartId() {
    return shoppingCartId;
  }

  public void setShoppingCartId(long shoppingCartId) {
    this.shoppingCartId = shoppingCartId;
  }

  public long getUserId() {
    return mUserId;
  }

  public void setUserId(long userId) {
    mUserId = userId;
  }

  public long getTheaterId() {
    return mTheaterId;
  }

  public void setTheaterId(long theaterId) {
    mTheaterId = theaterId;
  }
}
