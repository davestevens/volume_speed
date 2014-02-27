package uk.co.ecksdee.volume_speed;

import android.os.Bundle;
import android.app.Activity;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);
    // Show the Up button in the action bar.
    setupActionBar();
    
    TextView email = (TextView) findViewById(R.id.about_email);
    String emailText = "<a href=\"mailto:" + getString(R.string.about_email)
        + "?subject=Volume/Speed App\">Email me</a>";
    email.setText(Html.fromHtml(emailText));
    email.setMovementMethod(LinkMovementMethod.getInstance());
  }
  
  /**
   * Set up the {@link android.app.ActionBar}, if the API is available.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void setupActionBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }
}
