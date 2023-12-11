package com.byoon.lastminuteflix;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

import com.byoon.lastminuteflix.entity.User;
import com.byoon.lastminuteflix.ui.LoginActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginActivityInstrumentedTest {

  @Test
  public void givenAppContext_whenGettingPackageName_thenVerifyCorrectPackageName() {
    // Given - Context of the app under test
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // When - Getting the package name
    String packageName = appContext.getPackageName();

    // Then - Verify the correct package name
    assertEquals("com.byoon.lastminuteflix", packageName);
  }

  @Test
  public void givenLoginActivity_whenInitialized_thenDatabaseNotNull() {
    // Given - IDE says to use try-with-resources to auto-close ActivityScenario.
    try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
      // When - Nothing to trigger as it's about initialization.

      // Then - Execute test on main thread per long debugging conversation with ChatGPT.
      scenario.onActivity(activity -> assertNotNull(activity.getUserDao()));
    }
  }

  @Test
  public void givenLoginActivity_whenInitialized_thenDatabaseHasUsers() {
    // Given - IDE says to use try-with-resources to auto-close ActivityScenario.
    try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
      // When - Nothing to trigger as it's about initialization.

      // Then - Database should have predefined users.
      scenario.onActivity(activity -> assertTrue(activity.getUserDao().getAllUsers().size() > 0));
    }
  }

  @Test
  public void givenLoginActivity_whenInitialized_thenDatabaseHasAdminUser() {
    // Given - IDE says to use try-with-resources to auto-close ActivityScenario.
    try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
      // When - Nothing to trigger as it's about initialization.

      // Then - Database should have predefined admin user per project use case.
      scenario.onActivity(activity -> {
        boolean hasAdminUser = false;
        for (User user : activity.getUserDao().getAllUsers()) {
          if (user.isAdmin()) {
            hasAdminUser = true;
            break;
          }
        }
        assertTrue(hasAdminUser);
      });
    }
  }
}