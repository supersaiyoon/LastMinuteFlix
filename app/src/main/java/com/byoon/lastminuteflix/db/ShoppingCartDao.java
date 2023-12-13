package com.byoon.lastminuteflix.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.byoon.lastminuteflix.entity.ShoppingCart;

@Dao
public interface ShoppingCartDao {
  @Insert
  long insert(ShoppingCart shoppingCart);

  @Update
  void update(ShoppingCart... shoppingCarts);

  @Delete
  void delete(ShoppingCart... shoppingCarts);

  @Query("SELECT * FROM " + AppDatabase.SHOPPING_CART_TABLE + " WHERE shoppingCartId = :shoppingCartId")
  ShoppingCart getShoppingCartById(long shoppingCartId);

  @Query("SELECT * FROM " + AppDatabase.SHOPPING_CART_TABLE + " WHERE mUserId = :userId")
  ShoppingCart getShoppingCartByUserId(long userId);
}
