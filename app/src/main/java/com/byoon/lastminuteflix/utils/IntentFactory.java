package com.byoon.lastminuteflix.utils;

import android.content.Context;
import android.content.Intent;

import com.byoon.lastminuteflix.ui.AdminActivity;
import com.byoon.lastminuteflix.ui.CreateAccountActivity;
import com.byoon.lastminuteflix.ui.LoginActivity;
import com.byoon.lastminuteflix.ui.MainActivity;

/**
 * Creates intents to go to different activities.
 * @author Brian Yoon
 * @since 2023-12-05
 */
public class IntentFactory {
  // Prevent instantiation of this class.
  private IntentFactory() {
    // Empty private constructor to make this class non-instantiable.
  }

  public static Intent createLoginActivityIntent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  public static Intent createCreateAccountActivityIntent(Context context) {
    return new Intent(context, CreateAccountActivity.class);
  }

  public static Intent createMainActivityIntent(Context context, int userId) {
    Intent intent = new Intent(context, MainActivity.class);
    String intentExtraKey = KeyConstants.USER_ID_KEY.getKey();
    intent.putExtra(intentExtraKey, userId);
    return intent;
  }

  public static Intent createAdminActivityIntent(Context context) {
    return new Intent(context, AdminActivity.class);
  }
}
