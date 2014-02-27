package uk.co.ecksdee.volume_speed;

import java.text.DecimalFormat;

import uk.co.ecksdee.volume_speed.utils.Audio;
import uk.co.ecksdee.volume_speed.utils.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
  public SharedPreferences prefs;
  private Location location;
  private Audio audio;
  private DecimalFormat decimal_format;
  private float previous_speed;
  
  public enum Activity {
    SETTINGS, ABOUT;
  };
  
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
    case R.id.action_settings: {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivityForResult(intent, Activity.SETTINGS.ordinal());
      return true;
    }
    case R.id.action_about: {
      Intent intent = new Intent(this, AboutActivity.class);
      startActivityForResult(intent, Activity.ABOUT.ordinal());
      return true;
    }
    default:
      return false;
    }
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch ((Activity) Activity.values()[requestCode]) {
    case SETTINGS:
      gps_on();
      break;
    default:
      break;
    }
  }
  
  private void initialize() {
    load_preferences();
    
    location = new Location(this);
    audio = new Audio(this);
    decimal_format = new DecimalFormat("#0.0");
    
    gps_on();
    set_volume_bar(audio.volume_percentage());
  }
  
  /*
   * View settings
   */
  private void set_volume_bar(Integer volume) {
    ProgressBar volume_bar = (ProgressBar) findViewById(R.id.volume_bar);
    volume_bar.setProgress(volume);
  }
  
  private void set_current_speed(Float speed) {
    TextView current_speed = (TextView) findViewById(R.id.current_speed);
    current_speed.setText(decimal_format.format(speed).toString());
  }
  
  /*
   * GPS statuses
   */
  public void gps_on() {
    if (!location.is_enabled() || !gps_active()) {
      gps_off();
    } else {
      location.initialize(update_frequency());
    }
  }
  
  public void gps_off() {
    location.pause();
    
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean("pref_gps_active", false);
    editor.commit();
    
    alert(getString(R.string.no_gps_title), getString(R.string.no_gps_message));
  }
  
  /*
   * Speed calculations
   */
  public void change_in_speed(float speed) {
    float speed_difference = normalize_speed(speed) - previous_speed;
    
    int step = speed_step();
    int times = (int) Math.floor(Math.abs(speed_difference) / step);
    int change = 0;
    
    if (times >= 1) {
      if (speed_difference > 0) {
        change = audio.up(volume_step() * times);
      } else {
        change = audio.down(volume_step() * times);
      }
    }
    previous_speed += step * (change / volume_step());
    set_current_speed(speed);
    set_volume_bar(audio.volume_percentage());
  }
  
  private float normalize_speed(float speed) {
    return (speed < speed_minimum()) ? 0 : speed - speed_minimum();
  }
  
  /*
   * Prefs gets
   */
  private void load_preferences() {
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    prefs = PreferenceManager.getDefaultSharedPreferences(this);
  }
  
  private Boolean gps_active() {
    return prefs.getBoolean("pref_gps_active",
        Boolean.parseBoolean(getString(R.string.pref_gps_active)));
  }
  
  private int speed_step() {
    return Integer.parseInt(prefs.getString("pref_speed_step",
        getString(R.string.pref_speed_step_default)));
  }
  
  private int volume_step() {
    return Integer.parseInt(prefs.getString("pref_volume_step",
        getString(R.string.pref_volume_step_default)));
  }
  
  private int speed_minimum() {
    return Integer.parseInt(prefs.getString("pref_speed_minimum",
        getString(R.string.pref_speed_minimum_default)));
  }
  
  private int update_frequency() {
    return Integer.parseInt(prefs.getString("pref_gps_update_frequency",
        getString(R.string.pref_gps_update_frequency_default)));
  }
  
  /*
   * Alert dialog
   */
  private void alert(String title, String message) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle(title);
    
    alertDialogBuilder.setMessage(message).setCancelable(false)
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });
    
    AlertDialog alertDialog = alertDialogBuilder.create();
    
    alertDialog.show();
  }
}
