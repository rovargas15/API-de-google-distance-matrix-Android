package com.example.empresariales.empresariales.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.empresariales.empresariales.R;
import com.example.empresariales.empresariales.adapter.PlaceAutocompleteAdapter;
import com.example.empresariales.empresariales.model.DataModel;
import com.example.empresariales.empresariales.presenter.MainPresenter;
import com.example.empresariales.empresariales.util.AppDialogs;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    protected GeoDataClient mGeoDataClient;
    private PlaceAutocompleteAdapter autocompleteAdapter;
    private Place start, finish;
    private MainPresenter presenter;
    private ProgressDialog loading;
    AutoCompleteTextView txt_start,txt_finish;
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGeoData();
        initPresent();
        initAutocomplete();

        Button btn_calDistance = findViewById(R.id.btn_send);
        btn_calDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start != null && finish != null)
                    presenter.calcDistance(start, finish);
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_start.setText("");
                txt_finish.setText("");
            }
        });


    }

    private void initGeoData(){
        mGeoDataClient = Places.getGeoDataClient(this, null);
    }

    private void initPresent() {
        presenter = new MainPresenter();
        presenter.setView(this);
    }

    private void initAutocomplete() {
        txt_start = findViewById(R.id.txt_start);
        txt_finish = findViewById(R.id.txt_finish);

        txt_start.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = autocompleteAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
                placeResult.addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                        PlaceBufferResponse places = task.getResult();
                        final Place place = places.get(0);
                        start = place;
                    }
                });
            }
        });
        txt_finish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = autocompleteAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
                placeResult.addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                        PlaceBufferResponse places = task.getResult();
                        final Place place = places.get(0);
                        finish = place;
                    }
                });
            }
        });

        autocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS, null);

        txt_start.setAdapter(autocompleteAdapter);
        txt_finish.setAdapter(autocompleteAdapter);

    }

    @Override
    public void show(List<DataModel> dataModels) {
        String message = "Distancia: " + dataModels.get(0).rows.get(0).elements.get(0).distance.text;
        AppDialogs.createDialog(this, message).show();

    }

    @Override
    public void showLoading() {
        loading = AppDialogs.createLoading(this);
    }

    @Override
    public void hideLoading() {
        loading.dismiss();
    }

}
