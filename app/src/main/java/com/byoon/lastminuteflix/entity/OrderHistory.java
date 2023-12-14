package com.byoon.lastminuteflix.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.byoon.lastminuteflix.db.AppDatabase;

@Entity(tableName = AppDatabase.ORDER_HISTORY_TABLE,
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
public class OrderHistory {
  @PrimaryKey(autoGenerate = true)
  private long mOrderHistoryId;

  // Foreign keys
  private long mUserId;
  private long mTheaterId;

  public OrderHistory(long userId, long theaterId) {
    mUserId = userId;
    mTheaterId = theaterId;
  }

  public long getOrderHistoryId() {
    return mOrderHistoryId;
  }

  public void setOrderHistoryId(long shoppingCartId) {
    this.mOrderHistoryId = shoppingCartId;
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
