package uk.co.ecksdee.volume_speed;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;

public class Speed implements LocationListener {
  private MainActivity main_activity;
  private LocationManager locationManager;
  
  private static float MIN_DISTANCE = 10; // Meters
  private static long MIN_TIME = 1000; // Milliseconds
  
  public Speed(MainActivity ma) {
    main_activity = ma;
    locationManager = (LocationManager) main_activity
        .getSystemService(Context.LOCATION_SERVICE);
    
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        MIN_TIME, MIN_DISTANCE, this);
  }
  
  public Boolean is_enabled() {
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }
  
  public void pause() {
    locationManager.removeUpdates(this);
  }
  
  @Override
  public void onLocationChanged(Location location) {
    // Do nothing
  }
  
  @Override
  public void onProviderDisabled(String provider) {
    main_activity.no_gps();
  }
  
  @Override
  public void onProviderEnabled(String provider) {
    // Do nothing
  }
  
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    main_activity.change_in_speed(get_speed());
  }
  
  private float get_speed() {
    Location location;
    
    location = locationManager
        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
    
    if (location.hasSpeed()) {
      return location.getSpeed();
    }
    
    return (float) 0.0;
  }
}
