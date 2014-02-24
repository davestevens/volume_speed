package uk.co.ecksdee.volume_speed;

import uk.co.ecksdee.volume_speed.utils.Audio;
import uk.co.ecksdee.volume_speed.utils.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {
  public final static String TAG = "MAIN";
  private SharedPreferences prefs;
  private Location location;
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
    
    location = new Location(this);
    _gps_on();
    
    audio = new Audio(this);
    audio.set_step(prefs.getInt("pref_volume_step",
        Integer.parseInt(getString(R.string.pref_volume_steps_default))));
    
    set_volume_bar(audio.volume_percentage());
    set_status(getString(R.string.initialized));
    
    // TODO: on change of Preferences, update view
  }
  
  public void gps_on(View view) {
    _gps_on();
  }
  
  public void _gps_on() {
    if (!location.is_enabled()) {
      no_gps();
    } else {
      location.initialize();
      RadioButton on = (RadioButton) findViewById(R.id.speed_on);
      on.setChecked(true);
      set_status(getString(R.string.initialized));
    }
  }
  
  public void gps_off(View view) {
    location.pause();
    no_gps();
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
    RadioButton off = (RadioButton) findViewById(R.id.speed_off);
    off.setChecked(true);
  }
  
  public void change_in_speed(float speed) {
    float converted_speed = convert_speed(speed);
    
    float diff = converted_speed - previous_speed;
    
    // TODO: get prefs_speed_step as int
    Integer times = Math.round(Math.abs(diff) / 2);
    if (times > 0) {
      if (diff > 0) {
        set_status("Increasing " + times);
        audio.up(times);
      } else {
        set_status("Decreasing " + times);
        audio.down(times);
      }
      previous_speed = converted_speed;
    }
    
    set_current_speed(converted_speed);
    set_volume_bar(audio.volume_percentage());
  }
  
  private float convert_speed(float meters_per_second) {
    String units = prefs.getString("pref_speed_units",
        getString(R.string.pref_speed_units_default));
    if (units.equals("MPH")) {
      return meters_per_second * (float) 2.23693629;
    } else if (units.equals("KPH")) {
      return meters_per_second * (float) 3.6;
    } else {
      return (float) -1.0;
    }
  }
}
