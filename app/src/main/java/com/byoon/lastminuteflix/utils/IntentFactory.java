package com.byoon.lastminuteflix.utils;

import android.content.Context;
import android.content.Intent;

import com.byoon.lastminuteflix.ui.LoginActivity;
import com.byoon.lastminuteflix.ui.MainActivity;

public class IntentFactory {
  // Prevent instantiation of this class.
  private IntentFactory() {
    // Empty private constructor to make this class non-instantiable.
  }

  public static Intent createLoginActivityIntent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  public static Intent createMainActivityIntent(Context context) {
    return new Intent(context, MainActivity.class);
  }
}
