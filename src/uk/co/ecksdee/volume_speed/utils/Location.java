package uk.co.ecksdee.volume_speed.utils;

import uk.co.ecksdee.volume_speed.MainActivity;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Location implements LocationListener {
  private MainActivity main_activity;
  private LocationManager locationManager;
  
  private static float MIN_DISTANCE = 10; // Meters
  private static long MIN_TIME = 1000; // Milliseconds
  
  public Location(MainActivity ma) {
    main_activity = ma;
    locationManager = (LocationManager) main_activity
        .getSystemService(Context.LOCATION_SERVICE);
  }
  
  public void initialize() {
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
  public void onLocationChanged(android.location.Location location) {
    main_activity.change_in_speed(speed());
  }
  
  @Override
  public void onProviderDisabled(String provider) {
    main_activity.no_gps();
  }
  
  @Override
  public void onProviderEnabled(String provider) {
    main_activity._gps_on();
  }
  
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // Do nothing
  }
  
  private float speed() {
    /*
     * Convert from meters/second to Miles/Hour
     */
    return get_speed() * (float) 2.23693629;
  }
  
  private float get_speed() {
    android.location.Location location;
    
    location = locationManager
        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
    
    if (location.hasSpeed()) {
      return location.getSpeed();
    } else {
      // TODO: Calculate speed manually?
    }
    
    return (float) 0.0;
  }
}
