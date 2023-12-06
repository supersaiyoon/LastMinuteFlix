package com.byoon.lastminuteflix.utils;

/**
 * Keys for intent extras to pass data between activities.
 * @author Brian Yoon
 * @since 2023-12-05
 */
public enum IntentKeys {
  USER_ID_KEY("USER_ID_KEY"),
  USERNAME_KEY("USERNAME_KEY"),
  PREFERENCES_KEY("PREFERENCES_KEY");

  private final String key;

  IntentKeys(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
