## references

### adding the map to fragment
https://www.geeksforgeeks.org/how-to-implement-google-map-inside-fragment-in-android/

### listening on user action on google map
*********************** code start *************************************


googleMap.setOnMapClickListener(latLng -> {
// When clicked on map
// Initialize marker options
MarkerOptions markerOptions=new MarkerOptions();
// Set position of marker
markerOptions.position(latLng);
// Set title of marker
markerOptions.title(latLng.latitude+" : "+latLng.longitude);
// Remove all marker
googleMap.clear();
// Animating to zoom the marker
googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
// Add marker on map
googleMap.addMarker(markerOptions);
});


*********************** code end *************************************