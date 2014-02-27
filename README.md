# Volume/Speed

My first attempt at Android development.
Change the volume of your Android device based on your speed.
I've been wanting something like this for on my motorbike so I don't have to worry about chaging the volume when I slow down / speed up.

## Settings

### GPS
- Active
 - `on` or `off`
- Update Frequency
 - On change it resets `requestLocationUpdates` time

### Volume
- Maximum
 - Maximum threshold, Volume will not be increased above this
- Steps
 - Number of volume steps to increase for Speed change

### Speed
- Minimum
 - Minimum threshold, nothing will be changed until this Speed is acheived
- Steps
 - Number of mph to register a Volume change

## TODO
- Incrementally increase/decrease the volume
 - Need to look in `Handler` or `Thread` to perform task (loop with time delay) with synchronisation.
- Design
