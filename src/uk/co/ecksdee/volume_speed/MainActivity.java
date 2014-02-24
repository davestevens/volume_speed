package uk.co.ecksdee.volume_speed;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    initialize();
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_settings:
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    default:
      return false;
    }
  }
  
  private void initialize() {
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    // TODO: setup Speed
    // TODO: setup Volume
    // TODO: initialize view (speed_units, volume_bar, status)
    // TODO: on change of Preferences, update view
  }
}
