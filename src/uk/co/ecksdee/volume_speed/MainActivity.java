package uk.co.ecksdee.volume_speed;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
  public final static String TAG = "MAIN";
  private SharedPreferences prefs;
  private Speed speed;
  private Audio audio;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    initialize();
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
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
    
    initialize_view();
    
    speed = new Speed(this);
    if (!speed.is_enabled()) {
      no_gps();
    }
    
    audio = new Audio(this);
    audio.set_step(prefs.getInt("pref_volume_step",
        Integer.parseInt(getString(R.string.pref_volume_steps_default))));
    
    set_volume_bar(audio.volume_percentage());
    // TODO: on change of Preferences, update view
  }
  
  private void set_volume_bar(Integer v) {
    ProgressBar volume = (ProgressBar) findViewById(R.id.volume_bar);
    volume.setProgress(v);
  }
  
  public void initialize_view() {
    TextView speed_units, status;
    
    status = (TextView) findViewById(R.id.status);
    status.setText(getString(R.string.initializing));
    
    speed_units = (TextView) findViewById(R.id.speed_units);
    speed_units.setText(prefs.getString("pref_speed_units",
        getString(R.string.pref_speed_units_default)));
  }
  
  public void no_gps() {
    TextView status = (TextView) findViewById(R.id.status);
    status.setText(getString(R.string.no_gps));
  }
  
  public void change_in_speed(float speed) {
    // Update here
    // TODO: convert to correct speed units
    // TODO: check if it has changed passed a threshold
    Log.i(TAG, "Change in speed: " + speed);
    // TODO: save speed
  }
}
