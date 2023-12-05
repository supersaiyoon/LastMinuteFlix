package com.byoon.lastminuteflix.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.byoon.lastminuteflix.R;
import com.byoon.lastminuteflix.utils.IntentFactory;

public class LoginActivity extends AppCompatActivity {
  Button mLogInButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mLogInButton = findViewById(R.id.login_button);

    // Press login button to go to main activity.
    mLogInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Handle login button click.
        startActivity(IntentFactory.createMainActivityIntent(LoginActivity.this));
      }
    });
  }
}
