package uk.co.ecksdee.volume_speed;

import uk.co.ecksdee.volume_speed.utils.Audio;
import uk.co.ecksdee.volume_speed.utils.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
    
    double[] myArray = { 0.0, 1.0, 2.0, 2.5, 4.0, 1.5, 4.0, 0.5 };
    
    for (int i = 0; i < myArray.length; i++) {
      change_in_speed((float) myArray[i]);
    }
  }
  
  public void gps_on(View view) {
    _gps_on();
  }
  
  public void _gps_on() {
    if (!location.is_enabled()) {
      no_gps();
    } else {
      location.initialize();
      RadioButton on = (RadioButton) findViewById(R.id.location_on);
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
  }
  
  public void no_gps() {
    TextView status = (TextView) findViewById(R.id.status);
    status.setText(getString(R.string.no_gps));
    RadioButton off = (RadioButton) findViewById(R.id.location_off);
    off.setChecked(true);
  }
  
  public void change_in_speed(float speed) {
    float converted_speed = speed;
    
    float diff = speed - previous_speed;
    Log.i(TAG, "converted_speed: " + speed);
    Log.i(TAG, "previous_speed: " + previous_speed);
    Log.i(TAG, "diff: " + diff);
    
    Integer step = Integer.parseInt(prefs.getString("pref_speed_steps",
        getString(R.string.pref_speed_steps_default)));
    Log.i(TAG, "step: " + step);
    Log.i(TAG, "times: " + (int) Math.floor(Math.abs(diff) / step));
    Integer times = (int) Math.floor(Math.abs(diff) / step);
    if (times >= 1) {
      Log.i(TAG, "TIMES > 0");
      if (diff > 0) {
        Log.i(TAG, "increase: " + times);
        set_status("Increasing " + times);
        audio.up(times);
        previous_speed += step * times;
      } else {
        Log.i(TAG, "decrease: " + times);
        set_status("Decreasing " + times);
        audio.down(times);
        previous_speed -= step * times;
      }
    }
    
    set_current_speed(speed);
    set_volume_bar(audio.volume_percentage());
  }
}
