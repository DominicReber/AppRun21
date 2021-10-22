package ch.apprun.schatzkarte;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

import java.util.List;

public class LocationListenerImpl implements LocationListener {
    private IMapController mapController;
    private double currentLatitude;
    private double currentLongitude;

    public LocationListenerImpl(IMapController mapController) {
        this.mapController = mapController;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        GeoPoint point = new GeoPoint(currentLatitude, currentLongitude);
        mapController.setCenter(point);
        System.out.println("Current location: " + currentLatitude + ", " + currentLongitude);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
    }

    @Override
    public void onFlushComplete(int requestCode) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double latitude) {
        currentLatitude = latitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double longitude) {
        currentLongitude = longitude;
    }
}
