package uk.co.ecksdee.volume_speed.utils;

import uk.co.ecksdee.volume_speed.MainActivity;
import uk.co.ecksdee.volume_speed.R;
import android.media.AudioManager;
import android.app.Activity;
import android.content.Context;

public class Audio extends Activity {
  private MainActivity main_activity;
  private AudioManager audioManager;
  private static Integer stream = AudioManager.STREAM_MUSIC;
  
  public Audio(MainActivity main_activity) {
    this.main_activity = main_activity;
    audioManager = (AudioManager) main_activity
        .getSystemService(Context.AUDIO_SERVICE);
  }
  
  public int up(int steps) {
    return alter(steps);
  }
  
  public int down(int steps) {
    return alter(-steps);
  }
  
  public int volume_percentage() {
    float volume = ((float) current_volume() / max()) * 100;
    return Math.round(volume);
  }
  
  private int alter(int diff) {
    int before = current_volume();
    if (before + diff > volume_maximum()) {
      return 0;
    }
    audioManager.setStreamVolume(stream, current_volume() + diff, 0);
    return current_volume() - before;
  }
  
  private int current_volume() {
    return audioManager.getStreamVolume(stream);
  }
  
  private int max() {
    return audioManager.getStreamMaxVolume(stream);
  }
  
  private int volume_maximum() {
    return Integer.parseInt(main_activity.prefs.getString(
        "pref_volume_maximum",
        main_activity.getString(R.string.pref_volume_maximum_default)));
  }
}
