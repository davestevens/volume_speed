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
  
  private float previous_speed;
  
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
    set_status(getString(R.string.initialized));
    
    // TODO: on change of Preferences, update view
  }
  
  private void set_volume_bar(Integer volume) {
    ProgressBar volume_bar = (ProgressBar) findViewById(R.id.volume_bar);
    volume_bar.setProgress(volume);
  }
  
  private void set_current_speed(Float speed) {
    TextView current_speed = (TextView) findViewById(R.id.current_speed);
    current_speed.setText(speed.toString());
  }
  
  private void set_status(String string) {
    TextView status = (TextView) findViewById(R.id.status);
    status.setText(string);
  }
  
  private void set_speed_units(String string) {
    TextView speed_units = (TextView) findViewById(R.id.speed_units);
    speed_units.setText(string);
  }
  
  public void initialize_view() {
    set_status(getString(R.string.initializing));
    set_speed_units(prefs.getString("pref_speed_units",
        getString(R.string.pref_speed_units_default)));
  }
  
  public void no_gps() {
    TextView status = (TextView) findViewById(R.id.status);
    status.setText(getString(R.string.no_gps));
  }
  
  public void change_in_speed(float speed) {
    // TODO: convert to correct speed units
    // TODO: check if it has changed passed a threshold
    set_current_speed(speed);
    // TODO: save speed
    previous_speed = speed;
  }
}
