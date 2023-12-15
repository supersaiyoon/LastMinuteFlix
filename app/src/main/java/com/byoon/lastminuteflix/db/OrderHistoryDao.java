package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.OrderHistory;

import java.util.List;

@Dao
public interface OrderHistoryDao {
  @Insert
  long insert(OrderHistory orderHistory);

  @Update
  void update(OrderHistory... orderHistories);

  @Delete
  void delete(OrderHistory... orderHistories);

  @Query("SELECT * FROM " + AppDatabase.ORDER_HISTORY_TABLE)
  List<OrderHistory> getAllOrderHistory();

  @Query("SELECT * FROM " + AppDatabase.ORDER_HISTORY_TABLE + " WHERE mOrderHistoryId = :orderHistoryId")
  OrderHistory getOrderHistoryByOrderId(long orderHistoryId);

  @Query("SELECT * FROM " + AppDatabase.ORDER_HISTORY_TABLE + " WHERE mUserId = :userId")
  OrderHistory getOrderHistoryByUserId(long userId);
}
