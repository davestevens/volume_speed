package uk.co.ecksdee.volume_speed.utils;

import uk.co.ecksdee.volume_speed.MainActivity;
import android.media.AudioManager;
import android.content.Context;

public class Audio {
  private AudioManager audioManager;
  private static Integer stream = AudioManager.STREAM_MUSIC;
  
  public Audio(MainActivity main_activity) {
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
    float volume = ((float) current_volume() / audioManager
        .getStreamMaxVolume(stream)) * 100;
    return Math.round(volume);
  }
  
  private int alter(int diff) {
    audioManager.setStreamVolume(stream, current_volume() + diff, 0);
    return current_volume();
  }
  
  private int current_volume() {
    return audioManager.getStreamVolume(stream);
  }
}
