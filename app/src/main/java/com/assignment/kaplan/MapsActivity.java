package com.assignment.kaplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Place place;
    private TextView txtCity, txtTemp, txtWeather, txtWind, txtHumidity;
    private MaterialButton materialButtonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyBsL5db7ma8C7LhBPDavZngHMOBsbtpQR0");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.e("TAG,", "An error occurred: hs " + place.getName());
                    Log.e("TAG,", "An error occurred: HAHAHAHAHA " + place.getLatLng());

                    MapsActivity.this.place = place;

                    if (place.getLatLng() != null) {
                        mMap.clear();

                        mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));

                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                place.getLatLng(), 15);

                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                        mMap.animateCamera(location);
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.e("TAG,", "An error occurred: " + status);
                }
            });
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.e("MARKER",  " " + marker.getPosition());

                showWeather(marker.getPosition());

                return false;
            }
        });
    }

    private void showWeather(LatLng position) {

        APIInterface apiService =
                APIClient.getClient().create(APIInterface.class);

        Single<ForecastModel> call = apiService.getResponse(position.latitude, position.longitude, "285f5106688d130985a9d7f0faf8d500");
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWeatherResponse());

    }

    public DisposableSingleObserver<ForecastModel> getWeatherResponse() {
        return new DisposableSingleObserver<ForecastModel>() {
            @Override
            public void onSuccess(ForecastModel value) {

                try {
                    Log.e("Val ", "WEATHER " + value);
                    Log.e("Val ", "Temperature  " + value.getMain().getTemp());
                    Log.e("Val ", "Weather Condition " + value.getWeather().get(0).getDescription());
                    Log.e("Val ", "Wind Speed " + value.getWind().getSpeed());
                    Log.e("Val ", "Humidity " + value.getMain().getHumidity());

                    LayoutInflater inflater = LayoutInflater.from(MapsActivity.this);
                    MaterialCardView cardView = (MaterialCardView) inflater.inflate(R.layout.lay_weather_popup, null, false);

                    txtCity = cardView.findViewById(R.id.txtCity);
                    txtTemp = cardView.findViewById(R.id.txtTemp);
                    txtWeather = cardView.findViewById(R.id.txtWeather);
                    txtWind = cardView.findViewById(R.id.txtWind);
                    txtHumidity = cardView.findViewById(R.id.txtHumidity);
                    materialButtonOk = cardView.findViewById(R.id.materialButtonOk);

                    txtCity.setText(MapsActivity.this.place.getName());
                    txtTemp.setText("Temperature : " + value.getMain().getTemp());
                    txtWeather.setText("Weather : " + value.getWeather().get(0).getDescription());
                    txtWind.setText("Wind Speed : " + value.getWind().getSpeed());
                    txtHumidity.setText("Humidity : " + value.getMain().getHumidity());

                    final AlertDialog materialDialogs = new MaterialAlertDialogBuilder(MapsActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                            .setView(cardView)
                            .setCancelable(true)
                            .show();

                    materialButtonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialogs.dismiss();

                        }
                    });

//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable e) {

                try {

//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }

                    Log.e("ERROR", " " + e);

                    if (e instanceof UndeliverableException) {
                        e = e.getCause();
                        Log.w("Undeliverable excep rec", "not sure what to do", e);
                        return;
                    } else if ((e instanceof IOException) || (e instanceof SocketException)) {
                        Log.w("IOException", "SocketException", e);
                        // fine, irrelevant network problem or API that throws on cancellation
                        return;
                    } else if (e instanceof InterruptedException) {
                        Log.w("Undeliverable excep rec", "InterruptedException", e);
                        // fine, some blocking code was interrupted by a dispose call
                        return;
                    } else if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                        // that's likely a bug in the application
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), e);

                        Log.w("NullPointerException", "IllegalArgumentException", e);

                        return;
                    } else if (e instanceof IllegalStateException) {
                        // that's a bug in RxJava or in a custom operator
                        Thread.currentThread().getUncaughtExceptionHandler()
                                .uncaughtException(Thread.currentThread(), e);

                        Log.w("Undeliverable excep rec", "IllegalStateException", e);

                        return;
                    } else {
                        HttpException error = (HttpException) e;

                        Log.e("ERRORCODE", " " + error.code());

                    }

                } catch (Exception e1) {
                    //e1.printStackTrace();
                }

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
