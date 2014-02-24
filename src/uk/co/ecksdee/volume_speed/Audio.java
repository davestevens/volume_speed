package uk.co.ecksdee.volume_speed;

import android.media.AudioManager;
import android.content.Context;

public class Audio {
  private AudioManager audioManager;
  private static Integer stream = AudioManager.STREAM_MUSIC;
  private Integer step;
  
  public Audio(MainActivity main_activity) {
    audioManager = (AudioManager) main_activity
        .getSystemService(Context.AUDIO_SERVICE);
  }
  
  public void set_step(Integer step) {
    this.step = step;
  }
  
  public Integer up() {
    return alter(step);
  }
  
  public Integer down() {
    return alter(-step);
  }
  
  public Integer volume_percentage() {
    float volume = ((float) current_volume() / audioManager
        .getStreamMaxVolume(stream)) * 100;
    return Math.round(volume);
  }
  
  private Integer alter(Integer diff) {
    audioManager.setStreamVolume(stream, current_volume() + diff, 0);
    return current_volume();
  }
  
  public Integer current_volume() {
    return audioManager.getStreamVolume(stream);
  }
}
